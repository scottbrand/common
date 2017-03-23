package io.github.scottbrand.webservice.jaxrs.publisher.provider;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.ws.rs.Path;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import io.github.scottbrand.webservice.jaxrs.publisher.ResourceFilter;


@Component(immediate=true)
public class PublishProvider
{

	private ServiceRegistration<?>			connectorRegistration;
	private JAXRSConnector					jaxRsConnector;
	private HttpTracker						httpTracker;
	private ResourceTracker					allTracker;
	private ServletConfigurationTracker		servletConfigurationTracker;
	private ApplicationConfigurationTracker	applicationConfigurationTracker;
	private ServiceRegistration<?>			configRegistration;





	@Activate
	public void start(BundleContext context) throws Exception
	{
		System.setProperty("javax.ws.rs.ext.RuntimeDelegate", "org.glassfish.jersey.server.internal.RuntimeDelegateImpl");
		startJerseyServer();
		jaxRsConnector = new JAXRSConnector(context);
		registerConfiguration(context);
		connectorRegistration = context.registerService(JAXRSConnector.class.getName(), jaxRsConnector, null);
		openHttpServiceTracker(context);
		openServletConfigurationTracker(context);
		openApplicationConfigurationTracker(context);
		openAllServiceTracker(context);
	}





	private void registerConfiguration(BundleContext context)
	{
		Dictionary<String, String> properties = new Hashtable<String, String>();
		properties.put(Constants.SERVICE_PID, Configuration.CONFIG_SERVICE_PID);
		configRegistration = context.registerService(ManagedService.class.getName(), new Configuration(jaxRsConnector), properties);
	}





	private void startJerseyServer() throws BundleException
	{
		Bundle bundle = getJerseyAPIBundle();
		if (bundle.getState() != Bundle.ACTIVE)
		{
			bundle.start();
		}
	}





	private void openHttpServiceTracker(BundleContext context)
	{
		httpTracker = new HttpTracker(context, jaxRsConnector);
		httpTracker.open();
	}





	private void openServletConfigurationTracker(BundleContext context)
	{
		servletConfigurationTracker = new ServletConfigurationTracker(context, jaxRsConnector);
		servletConfigurationTracker.open();
	}





	private void openApplicationConfigurationTracker(BundleContext context)
	{
		applicationConfigurationTracker = new ApplicationConfigurationTracker(context, jaxRsConnector);
		applicationConfigurationTracker.open();
	}





	private void openAllServiceTracker(BundleContext context) throws InvalidSyntaxException
	{
		ResourceFilter allResourceFilter = getResourceFilter(context);
		allTracker = new ResourceTracker(context, allResourceFilter.getFilter(), jaxRsConnector);
		allTracker.open();
	}





	private ResourceFilter getResourceFilter(BundleContext context)
	{
		ServiceReference<?> filterReference = context.getServiceReference(ResourceFilter.class.getName());
		if (filterReference != null)
		{
			return (ResourceFilter) context.getService(filterReference);
		}
		return new AllResourceFilter(context);
	}





	@Deactivate
	public void stop(BundleContext context) throws Exception
	{
		httpTracker.close();
		servletConfigurationTracker.close();
		applicationConfigurationTracker.close();
		allTracker.close();
		connectorRegistration.unregister();
		configRegistration.unregister();
	}





	// For testing purpose
	Bundle getJerseyAPIBundle()
	{
		return FrameworkUtil.getBundle(Path.class);
	}
}
