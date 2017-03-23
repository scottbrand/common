package io.github.scottbrand.common.db.jpa;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import io.github.scottbrand.common.CollectionUtil;
import io.github.scottbrand.common.Patterns;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.logging.ILogger;



/**
 * AbstractEMFBuilder is an abstract class which provides the basic
 * glue to join a javax.sql.DataSource to an instance of EntityManagerFactoryBuilder
 * to enable JPA services in an OSGi runtime.
 * 
 * @author Scott Brand 
 *
 */
public abstract class AbstractEMFBuilder
{
    public static final String JAVAX_DATASOURCE      = "javax.persistence.nonJtaDataSource";
    public static final String OSGI_UNIT_NAME_FILTER = "(osgi.unit.name={0})";
    
	protected DataSource					ds;
	protected EntityManagerFactoryBuilder	emfb;




	/**
	 * This method should be called by the implementation class after its dependencies have been meet.
	 * The simplest way to call this method is from the implementation classes "@Activate" method if
	 * the class is deployed as a @Component.
	 * 
	 * @param context
	 * @param unitName
	 * @return
	 */
	protected ServiceRegistration<?> onActivate(BundleContext context, String unitName, ILogger log)
	{
		try
		{
			Map<String, Object> props = new HashMap<String, Object>();
			props.put(JAVAX_DATASOURCE, ds);
			EntityManagerFactory emf = emfb.createEntityManagerFactory(props);
			Dictionary<String, Object> emfProps = new Hashtable<String, Object>();

			String filter = Patterns.replace(OSGI_UNIT_NAME_FILTER, true, unitName);
			Collection<ServiceReference<EntityManagerFactoryBuilder>> pu = context.getServiceReferences(EntityManagerFactoryBuilder.class, filter);
			if (CollectionUtil.isListNullOrEmpty(pu) == false)
			{
				ServiceReference<EntityManagerFactoryBuilder> t = pu.iterator().next();
				emfProps.put(EntityManagerFactoryBuilder.JPA_UNIT_NAME, t.getProperty(EntityManagerFactoryBuilder.JPA_UNIT_NAME));
				emfProps.put(EntityManagerFactoryBuilder.JPA_UNIT_VERSION, t.getProperty(EntityManagerFactoryBuilder.JPA_UNIT_VERSION));
				emfProps.put(EntityManagerFactoryBuilder.JPA_UNIT_PROVIDER, t.getProperty(EntityManagerFactoryBuilder.JPA_UNIT_PROVIDER));
			}
			//
			// Register the factory with our builder and properties.
			//
			return context.registerService(EntityManagerFactory.class.getName(), emf, emfProps);
		}
		catch (Throwable t)
		{
			if (log != null)
				log.error(Strings.EXCEPTION,t);
			else
			    ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
			return null;
		}
	}





	/**
	 * Needs to be overridden in implementing class. The appropriate DataSource
	 * object should be injected, via @Reference. Example:
	 * 
	 * @Reference(target = "(ds.name=ds/postgres)")
	 * 
	 * @param ds
	 */
	protected abstract void setDataSource(DataSource ds);





	/**
	 * Needs to be overridden in implementing class. The appropriate Builder
	 * object should be injected, via @Reference. The implementing class that
	 * defines this reference will automatically get injected when the
	 * JPA_UNIT_NAME is registered via its persistence.xml file.
	 * 
	 * Example:
	 * 
	 * @Reference(target="(" + EntityManagerFactoryBuilder.JPA_UNIT_NAME + "=My_MetaData)")
	 * 
	 * @param ds
	 */
	protected abstract void setEntityManagerFactoryBuilder(EntityManagerFactoryBuilder emfb);

}
