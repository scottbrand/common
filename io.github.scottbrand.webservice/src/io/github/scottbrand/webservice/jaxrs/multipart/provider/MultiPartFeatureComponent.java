package io.github.scottbrand.webservice.jaxrs.multipart.provider;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;



@Component(immediate=true)
public class MultiPartFeatureComponent
{

	private volatile ServiceRegistration<?>  registration;
	
	@Activate
	private void activate(BundleContext ctx)
	{
		registration = ctx.registerService( MultiPartFeature.class.getName(), new MultiPartFeature(), null );
	}
	
	
	@Deactivate
	private void deactivate()
	{
		if (registration != null)
			registration.unregister();
	}
}
