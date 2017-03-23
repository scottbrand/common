package io.github.scottbrand.webservice.jaxrs.publisher.provider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;



public class ServiceContainer
{

	private final Set<ServiceHolder>	services;
	private final BundleContext			bundleContext;





	ServiceContainer(BundleContext bundleContext)
	{
		this.bundleContext = bundleContext;
		this.services = new HashSet<ServiceHolder>();
	}





	ServiceHolder add(ServiceReference<?> reference)
	{
		return add(bundleContext.getService(reference), reference);
	}





	void remove(Object service)
	{
		services.remove(find(service));
	}





	ServiceHolder[] getServices()
	{
		Set<ServiceHolder> result = new HashSet<ServiceHolder>();
		Iterator<ServiceHolder> iterator = services.iterator();
		while (iterator.hasNext())
		{
			result.add(iterator.next());
		}
		return result.toArray(new ServiceHolder[result.size()]);
	}





	ServiceHolder find(Object service)
	{
		Finder finder = new Finder();
		return finder.findServiceHolder(service, services);
	}





	void clear()
	{
		services.clear();
	}





	int size()
	{
		return services.size();
	}





	private ServiceHolder add(Object service, ServiceReference<?> reference)
	{
		ServiceHolder result = find(service);
		if (notFound(result))
		{
			result = new ServiceHolder(service, reference);
			services.add(result);
		}
		else if (referenceIsMissing(reference, result))
		{
			result.setServiceReference(reference);
		}
		return result;
	}





	private boolean notFound(ServiceHolder result)
	{
		return result == null;
	}





	private boolean referenceIsMissing(ServiceReference<?> reference, ServiceHolder result)
	{
		return reference != null && result.getReference() == null;
	}





	static class ServiceHolder
	{

		private ServiceReference<?>	serviceReference;
		private final Object		service;





		ServiceHolder(Object service, ServiceReference<?> serviceReference)
		{
			this.service = service;
			this.serviceReference = serviceReference;
		}





		Object getService()
		{
			return service;
		}





		ServiceReference<?> getReference()
		{
			return serviceReference;
		}





		void setServiceReference(ServiceReference<?> serviceReference)
		{
			this.serviceReference = serviceReference;
		}
	}





	static class Finder
	{

		ServiceHolder findServiceHolder(Object service, Set<ServiceHolder> collection)
		{
			Iterator<ServiceHolder> iterator = collection.iterator();
			ServiceHolder result = null;
			while (iterator.hasNext())
			{
				ServiceHolder serviceHolder = iterator.next();
				Object found = serviceHolder.getService();
				if (service.equals(found))
				{
					result = serviceHolder;
				}
			}
			return result;
		}
	}
}
