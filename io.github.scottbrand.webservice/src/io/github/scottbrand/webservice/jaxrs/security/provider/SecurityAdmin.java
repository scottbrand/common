package io.github.scottbrand.webservice.jaxrs.security.provider;

import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

import io.github.scottbrand.webservice.jaxrs.security.AuthenticationHandler;
import io.github.scottbrand.webservice.jaxrs.security.AuthorizationHandler;



public class SecurityAdmin
{

	public SecurityContext getSecurityContext(ContainerRequestContext requestContext)
	{
		SecurityContext result = null;
		//AuthenticationHandler authenticationHandler = Activator.getInstance().getAuthenticationHandler();
		//AuthorizationHandler authorizationHandler = Activator.getInstance().getAuthorizationHandler();
//		if (authenticationHandler != null && authorizationHandler != null)
//		{
//			result = createSecurityContext(requestContext, authenticationHandler, authorizationHandler);
//		}
		return result;
	}





	@SuppressWarnings("unused")
	private SecurityContext createSecurityContext(ContainerRequestContext requestContext, AuthenticationHandler authenticationHandler, AuthorizationHandler authorizationHandler)
	{
		Principal principal = authenticationHandler.authenticate(requestContext);
		if (principal != null)
		{
			return new SecurityContextImpl(authenticationHandler.getAuthenticationScheme(), principal, requestContext.getUriInfo().getRequestUri().getScheme().equals("https"), authorizationHandler);
		}
		return null;
	}

}
