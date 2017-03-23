package io.github.scottbrand.webservice.jaxrs.publisher;

import java.util.Map;

import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * <p>
 * Service that allows configuration of the JAX-RS {@link Application}. Multiple registrations will be tracked.
 * </p>
 * 
 * @since 4.3
 */
public interface ApplicationConfiguration
{
	
	public static final Logger log = LoggerFactory.getLogger(ApplicationConfiguration.class);

	/**
	 * <p>
	 * Will be called before the JAX-RS {@link Application} is registered. Please note that one
	 * {@link ApplicationConfiguration} can overwrite the values of other {@link ApplicationConfiguration}s. It depends
	 * on the order they are available in the OSGi container.
	 * </p>
	 * 
	 * @see Application#getProperties()
	 */
	Map<String, Object> getProperties();

}
