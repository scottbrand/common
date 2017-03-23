package io.github.scottbrand.common.provider.jpa;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.db.jpa.IPersistenceManager;
import io.github.scottbrand.common.db.jpa.NullForNoResult;
import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.logging.ILoggerFactory;



/**
 * PersistenceManagerImpl is the solution to find an instance
 * of a class that implements the requested persistence interface.
 * This implementation will dynamically inject an EntityManager
 * if the PersistenceContext annotation is found on a field an
 * names a PU that is associated with an EntityManagerFactory.
 *  
 * 
 * @author Scott Brand
 *
 */
@Component
public class PersistenceManager implements IPersistenceManager
{
	protected volatile ILogger log;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getPersistenceService(Class<T> persistenceClass)
	throws Throwable
	{
		log.trace("PersistenceManager called to get implementation: {}",persistenceClass);
		
		T po = ServiceLocator.getInstance().getService(persistenceClass);

		if (po == null)
		{
			log.warn("persistenceObject not found for service interface: {}",persistenceClass);
			return null;
		}
		
		T persistenceObject = (T)Class.forName(po.getClass().getName(),true,po.getClass().getClassLoader()).newInstance();
		
		log.trace("persistenceObject found is: {}",persistenceObject.getClass().getName());
		
		Field[] fa = persistenceObject.getClass().getDeclaredFields();
		Field[] sfa = persistenceObject.getClass().getSuperclass().getDeclaredFields();
		
		
		List<Field> fields = new ArrayList<Field>();
		if (fa.length > 0)
			fields.addAll(Arrays.asList(fa));
		if (sfa.length > 0)
			fields.addAll(Arrays.asList(sfa));
		
		boolean foundPU = false;
		EntityManager em = null;
		for (Field f : fields)
		{

			String unitName = null;
			
			if (f.isAnnotationPresent(PersistenceContext.class))
			{
				Annotation a = f.getAnnotation(PersistenceContext.class);
				try
				{
					Method m = a.annotationType().getMethod("unitName");
					if (m != null)
					{
						unitName = (String) m.invoke(a);
						log.trace("PU name found {} attempting injection",unitName);
						EntityManagerFactory emf = getEMF(unitName);
						boolean changeAccessible = f.isAccessible();
						if (changeAccessible == false)
							f.setAccessible(true);
						em = emf.createEntityManager();
						f.set(persistenceObject, em);
						foundPU = true;
						log.trace("PU and EntityManager injected successfully");
						if (changeAccessible == false)
							f.setAccessible(false);
						break;
					}
				}
				catch (Throwable t)
				{
					log.error("Unable to set PU and EntityManager",t);
					return (T)null;
				}
			}
		}
		if (foundPU == false)
			return null;
			
		ClassLoader cl = persistenceObject.getClass().getClassLoader();
		T o = null;
		try
		{
			o = (T)Proxy.newProxyInstance(cl, new Class[] { persistenceClass }, new DelegatingPersistenceWrapper(persistenceObject,em));
		}
		catch (Throwable t)
		{
			log.error("Unable to create a DelegatingPersistenceWrapper for object: " + persistenceObject,t);
			return (T)null;
		}
		return o;
	}

	
	
	/**
	 * Method to find the EntityManagerFactory instance that is responsible for creating
	 * EntityManagers for the given PU.
	 * 
	 * @param unitName
	 * @return
	 * @throws Throwable
	 */
	private EntityManagerFactory getEMF(String unitName)
	throws Throwable
	{
		List<EntityManagerFactory> list = ServiceLocator.getInstance().getServices(EntityManagerFactory.class, "(" + EntityManagerFactoryBuilder.JPA_UNIT_NAME + "=" + unitName + ")");
		if (list == null || list.isEmpty())
			throw new Throwable("No EntityManagerFactory with pu name " + unitName + " was found");
		return list.get(0);
	}
	
	
	
	
	/**
	 * For every persistence object that comes through our service implmentation,
	 * we create a Proxy wrapper around the instance.
	 * This will allow us to dynamically wrap method calls on the object with
	 * transactional boundaries and allow us to catch exceptions and clean up the
	 * EntityManager upon return.
	 * 
	 * @author Scott Brand
	 *
	 */
	class DelegatingPersistenceWrapper implements InvocationHandler
	{
		Object	      bean;
		EntityManager em;





		DelegatingPersistenceWrapper(Object bean,EntityManager em)
		{
			this.bean = bean;
			this.em   = em;
		}





		@Override
		public Object invoke(Object arg0, Method m, Object[] args) throws Throwable
		{
			log.trace("beginning transaction on: {} method: {}",bean,m);
			log.trace("method args are: {}",args);
			
			EntityTransaction x   = em.getTransaction();
			x.begin();
			Object returnObject = null;
			try
			{
				log.trace("invoking method: {} on object: {}",m,bean);
				returnObject = m.invoke(bean, args);
				x.commit();
				log.trace("COMMIT performed on method: {} on object: {}",m,bean);
			}
			catch (Throwable t)
			{
				InvocationTargetException te = (t instanceof InvocationTargetException) ? (InvocationTargetException)t : null;
				if (te != null && (te.getTargetException() instanceof NoResultException))
				{
					if (m.isAnnotationPresent(NullForNoResult.class))
						returnObject = null;
					x.commit();
					log.trace("NullForNoResult captured.  Returning null result");
				}
				else
				{
					log.error("ROLLBACK performed on object: " + bean + " calling method: " + m,(te != null ? te : t));
					x.rollback();
				}
			}
			em.close();
			log.trace("EntityManager closed on object: {} for method: {}",bean,m);
			log.trace("returnObject is: {}",returnObject);
			
			return returnObject;
		}
	}
	
	
	@Reference
	public void setLoggerFactory(ILoggerFactory loggerFactory)
	{
		this.log = loggerFactory.getLogger();
	}
}
