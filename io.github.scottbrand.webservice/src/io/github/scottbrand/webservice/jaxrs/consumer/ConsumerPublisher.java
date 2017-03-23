package io.github.scottbrand.webservice.jaxrs.consumer;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;



/**
 * <p>
 * When running in an OSGi environment this interface will be registered as OSGi service. It's usage is for creating
 * consumer objects out of <code>@Path</code> interfaces and register them as OSGi services too.
 * </p>
 * 
 * @see ConsumerFactory
 * @see Path
 * @see Provider
 */
public interface ConsumerPublisher
{

	/**
	 * <p>
	 * Creates consumer objets. See the {@link ConsumerFactory} for a detailed description. When the consumers are
	 * created they will be registered as OSGi services and can be consumed from other bundles.
	 * </p>
	 * 
	 * @see ConsumerFactory
	 */
	void publishConsumers(String baseUrl, Class<?>[] types, Object[] providers);

}
