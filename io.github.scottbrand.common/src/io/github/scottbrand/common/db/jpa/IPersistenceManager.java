package io.github.scottbrand.common.db.jpa;


/**
 * Standard interface for getting an instance of an object
 * that implements persistence for the given interface.
 * The general idea here is that the implementing class
 * will provide a Proxy object for the underlying class that 
 * contains JPA persistence logic.
 * The class which implements this interface is expected to
 * inject the appropriately annotated EntityManager object as
 * named by the  @PersistenceContext annotation.  
 * 
 * 
 * @author Scott Brand
 *
 */
public interface IPersistenceManager
{
	<T extends Object> T  getPersistenceService(Class<T> persistenceClass) throws Throwable;
}
