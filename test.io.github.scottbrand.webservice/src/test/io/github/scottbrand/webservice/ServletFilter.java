package test.io.github.scottbrand.webservice;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;



@Component
@Provider
@Priority(value = 3)
public class ServletFilter implements ContainerResponseFilter
{

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
	{
		System.out.println("filter() on ServerResponseFilter3");
		System.out.println("request: " + requestContext);
		System.out.println("response: " + responseContext);
	}

}
