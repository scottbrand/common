package io.github.scottbrand.webservice.jaxrs.publisher.provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import io.github.scottbrand.webservice.jaxrs.publisher.ServletConfiguration;



/**
 * <p>
 * Tracker for OSGi Services implementing the {@link ServletConfiguration} interface.
 * </p>
 */
public class ServletConfigurationTracker extends ServiceTracker<Object,Object>
{

	private final JAXRSConnector connector;





	ServletConfigurationTracker(BundleContext context, JAXRSConnector connector)
	{
		super(context, ServletConfiguration.class.getName(), null);
		this.connector = connector;
	}





	@Override
	public Object addingService(ServiceReference<Object> reference)
	{
		return connector.setServletConfiguration(reference);
	}





	@Override
	public void removedService(ServiceReference<Object> reference, Object service)
	{
		if (service instanceof ServletConfiguration)
		{
			connector.unsetServletConfiguration(reference, (ServletConfiguration) service);
		}
	}
}
