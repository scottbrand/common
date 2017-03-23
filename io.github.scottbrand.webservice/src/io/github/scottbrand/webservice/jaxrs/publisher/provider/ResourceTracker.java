package io.github.scottbrand.webservice.jaxrs.publisher.provider;

import javax.ws.rs.Path;
import javax.ws.rs.core.Feature;
import javax.ws.rs.ext.Provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import io.github.scottbrand.webservice.jaxrs.publisher.ApplicationConfiguration;
import io.github.scottbrand.webservice.jaxrs.publisher.IRest;



public class ResourceTracker extends ServiceTracker<Object,Object>
{

	private final BundleContext		context;
	private final JAXRSConnector	connector;





	public ResourceTracker(BundleContext context, Filter filter, JAXRSConnector connector)
	{
		super(context, filter, null);
		this.context = context;
		this.connector = connector;
	}





	@Override
	public Object addingService(ServiceReference<Object> reference)
	{
		Object service = context.getService(reference);
		Object result = delegateAddService(reference, service);
		if (result != null)
			ApplicationConfiguration.log.debug("registering service: {} as ws, result was: {}",service,result);
		return result;
	}





	private Object delegateAddService(ServiceReference<Object> reference, Object service)
	{
		Object result;
		if (isResource(service))
		{
			result = connector.addResource(reference);
		}
		else
		{
			context.ungetService(reference);
			result = null;
		}
		return result;
	}





	@Override
	public void removedService(ServiceReference<Object> reference, Object service)
	{
		connector.removeResource(service);
		context.ungetService(reference);
	}





	@Override
	public void modifiedService(ServiceReference<Object> reference, Object service)
	{
		connector.removeResource(service);
		delegateAddService(reference, service);
	}





	private boolean isResource(Object service)
	{
		return service != null && (service instanceof IRest || hasRegisterableAnnotation(service) || service instanceof Feature);
	}





	private boolean hasRegisterableAnnotation(Object service)
	{
		boolean result = isRegisterableAnnotationPresent(service.getClass());
		if (!result)
		{
			Class<?>[] interfaces = service.getClass().getInterfaces();
			for (Class<?> type : interfaces)
			{
				result = result || isRegisterableAnnotationPresent(type);
			}
		}
		return result;
	}





	private boolean isRegisterableAnnotationPresent(Class<?> type)
	{
		return type.isAnnotationPresent(Path.class) || type.isAnnotationPresent(Provider.class);
	}
}
