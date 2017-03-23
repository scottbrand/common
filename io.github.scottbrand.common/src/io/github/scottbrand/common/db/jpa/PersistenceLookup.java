package io.github.scottbrand.common.db.jpa;

import io.github.scottbrand.common.ServiceLocator;


/**
 * Simple static wrapper class to first find an {@link IPersistenceManager} service implementation
 * and then to return an instance of the requested <em>persistenceClass</em> object.
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public class PersistenceLookup
{
	public static <T extends Object> T  getService(Class<T> persistenceClass)
	{
		try
		{
			return ServiceLocator.getInstance().getService(IPersistenceManager.class).getPersistenceService(persistenceClass);
		}
		catch (Throwable t)
		{
			return null;
		}
	}
}
