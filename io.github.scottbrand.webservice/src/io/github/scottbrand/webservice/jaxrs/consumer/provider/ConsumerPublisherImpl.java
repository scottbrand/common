package io.github.scottbrand.webservice.jaxrs.consumer.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.glassfish.jersey.client.ClientConfig;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import io.github.scottbrand.webservice.jaxrs.consumer.ConsumerFactory;
import io.github.scottbrand.webservice.jaxrs.consumer.ConsumerPublisher;



@Component
public class ConsumerPublisherImpl implements ConsumerPublisher
{

	private BundleContext					context;
	private List<ServiceRegistration<?>>	registrations;





	@Activate
	private void actvate(BundleContext context)
	{
		this.registrations = new ArrayList<ServiceRegistration<?>>();
		this.context = context;
	}





	@Override
	public void publishConsumers(String baseUrl, Class<?>[] types, Object[] providers)
	{
		for (Class<?> type : types)
		{
			ClientConfig config = new ClientConfig();
			registerProviders(config, providers);
			Object resource = ConsumerFactory.createConsumer(baseUrl, config, type);
			Dictionary<String, Object> properties = new Hashtable<String, Object>();
			properties.put("com.eclipsesource.jaxrs.publish", "false");
			ServiceRegistration<?> registration = context.registerService(type.getName(), resource, properties);
			registrations.add(registration);
		}
	}





	private void registerProviders(ClientConfig config, Object[] providers)
	{
		if (providers != null)
			Arrays.asList(providers).stream().forEach(config::register);
	}





	public void unregister()
	{
		registrations.stream().forEach(r -> r.unregister());
	}
}
