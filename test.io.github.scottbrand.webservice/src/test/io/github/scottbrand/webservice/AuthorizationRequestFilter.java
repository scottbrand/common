package test.io.github.scottbrand.webservice;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;



@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationRequestFilter implements ContainerRequestFilter
{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException
	{

		System.out.println("Hello from AuthorizationRequestFilter!");
	}
}