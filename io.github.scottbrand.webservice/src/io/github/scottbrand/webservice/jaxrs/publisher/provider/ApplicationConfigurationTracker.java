package io.github.scottbrand.webservice.jaxrs.publisher.provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import io.github.scottbrand.webservice.jaxrs.publisher.ApplicationConfiguration;



/**
 * <p>
 * Tracker for OSGi Services implementing the {@link ApplicationConfiguration} interface.
 * </p>
 */
public class ApplicationConfigurationTracker extends ServiceTracker<Object, Object>
{

	private final JAXRSConnector connector;





	ApplicationConfigurationTracker(BundleContext context, JAXRSConnector connector)
	{
		super(context, ApplicationConfiguration.class.getName(), null);
		this.connector = connector;
	}





	@Override
	public Object addingService(ServiceReference<Object> reference)
	{
		return connector.addApplicationConfiguration(reference);
	}





	@Override
	public void removedService(ServiceReference<Object> reference, Object service)
	{
		if (service instanceof ApplicationConfiguration)
		{
			connector.removeApplicationConfiguration(reference, (ApplicationConfiguration) service);
		}
	}
}
