package io.github.scottbrand.webservice.jaxrs.publisher.provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;



public class HttpTracker extends ServiceTracker<Object, Object>
{

	private final JAXRSConnector connector;





	HttpTracker(BundleContext context, JAXRSConnector connector)
	{
		super(context, HttpService.class.getName(), null);
		this.connector = connector;
	}





	@Override
	public Object addingService(ServiceReference<Object> reference)
	{
		return connector.addHttpService(reference);
	}





	@Override
	public void removedService(ServiceReference<Object> reference, Object service)
	{
		if (service instanceof HttpService)
		{
			connector.removeHttpService((HttpService) service);
		}
	}
}
