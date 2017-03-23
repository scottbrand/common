package io.github.scottbrand.webservice.jaxrs.sse.provider;

import org.glassfish.jersey.media.sse.SseFeature;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;





@Component(immediate = true)
public class SSEProvider
{
	private ServiceRegistration<?> registration;





	@Activate
	private void activate(BundleContext bundleContext) throws Exception
	{
		registration = bundleContext.registerService(SseFeature.class.getName(), new SseFeature(), null);
	}





	@Deactivate
	public void deactivate(BundleContext bundleContext) throws Exception
	{
		if (registration != null)
		{
			registration.unregister();
		}
	}
}
