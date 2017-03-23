package io.github.scottbrand.webservice.jaxrs.security.provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;

import io.github.scottbrand.webservice.jaxrs.security.AuthenticationHandler;
import io.github.scottbrand.webservice.jaxrs.security.AuthorizationHandler;



@Component(immediate = true)
public class SecurityComponent
{

	private ServiceTracker<?, ?>	authenticationHandlerTracker;
	private ServiceTracker<?, ?>	authorizationHandlerTracker;
	private ServiceRegistration<?>	rolesAllowedDynamicFeatureRegistration;
	private ServiceRegistration<?>	containerRequestFilterRegistration;





	public AuthenticationHandler getAuthenticationHandler()
	{
		return (AuthenticationHandler) authenticationHandlerTracker.getService();
	}





	public AuthorizationHandler getAuthorizationHandler()
	{
		return (AuthorizationHandler) authorizationHandlerTracker.getService();
	}





	@Activate
	public void activate(BundleContext context) throws Exception
	{
		registerProviderServices(context);
		createHandlerTrackers(context);
	}





	private void registerProviderServices(BundleContext context)
	{
		rolesAllowedDynamicFeatureRegistration = context.registerService(RolesAllowedDynamicFeatureImpl.class.getName(), new RolesAllowedDynamicFeatureImpl(), null);
		containerRequestFilterRegistration = context.registerService(ContainerRequestFilterImpl.class.getName(), new ContainerRequestFilterImpl(new SecurityAdmin()), null);
	}





	private void createHandlerTrackers(BundleContext context)
	{
		authenticationHandlerTracker = new ServiceTracker<>(context, AuthenticationHandler.class.getName(), null);
		authenticationHandlerTracker.open();
		authorizationHandlerTracker = new ServiceTracker<>(context, AuthorizationHandler.class.getName(), null);
		authorizationHandlerTracker.open();
	}





	@Deactivate
	public void deactivate(BundleContext context) throws Exception
	{
		unregisterProviderServices();
		closeHandlerTrackers();
	}





	private void unregisterProviderServices()
	{
		if (rolesAllowedDynamicFeatureRegistration != null)
		{
			rolesAllowedDynamicFeatureRegistration.unregister();
		}
		if (containerRequestFilterRegistration != null)
		{
			containerRequestFilterRegistration.unregister();
		}
	}





	private void closeHandlerTrackers()
	{
		if (authenticationHandlerTracker != null)
		{
			authenticationHandlerTracker.close();
		}
		if (authorizationHandlerTracker != null)
		{
			authorizationHandlerTracker.close();
		}
	}

}
