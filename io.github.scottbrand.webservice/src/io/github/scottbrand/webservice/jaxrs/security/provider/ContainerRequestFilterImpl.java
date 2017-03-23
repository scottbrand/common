package io.github.scottbrand.webservice.jaxrs.security.provider;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;



@Provider
@Priority(Priorities.AUTHENTICATION)
public class ContainerRequestFilterImpl implements ContainerRequestFilter
{

	private final SecurityAdmin securityAdmin;





	public ContainerRequestFilterImpl(SecurityAdmin securityAdmin)
	{
		this.securityAdmin = securityAdmin;
	}





	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException
	{
		if (securityAdmin != null)
		{
			SecurityContext securityContext = securityAdmin.getSecurityContext(requestContext);
			if (securityContext != null)
			{
				requestContext.setSecurityContext(securityContext);
			}
		}
	}
}
