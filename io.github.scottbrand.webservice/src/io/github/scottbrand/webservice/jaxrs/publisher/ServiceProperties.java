package io.github.scottbrand.webservice.jaxrs.publisher;

public class ServiceProperties
{

	/**
	 * <p>
	 * When registering a @Path or @Provider annotated object as an OSGi service the connector does publish this
	 * resource automatically. Anyway, in some scenarios it's not wanted to publish those services. If you want a
	 * resource not publish set this property as a service property with the value <code>false</code>.
	 * </p>
	 */
	public static String PUBLISH = "io.github.scottbrand.jaxrs.publish";





	private ServiceProperties()
	{
		// prevent instantiation
	}
}
