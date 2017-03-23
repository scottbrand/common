package io.github.scottbrand.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import io.github.scottbrand.common.l10n.ITranslationService;
import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.logging.ILoggerFactory;
import io.github.scottbrand.common.logging.LogDelegate;



/**
 * Generic class to provide easy access to Service lookups The bundle activator
 * class will call the INSTANCE of this class and set the BundleContext.
 * 
 * @author Scott Brand
 *
 */
public class ServiceLocator
{
    private static final ServiceLocator INSTANCE = new ServiceLocator();



    ILogger logger;
    

    ITranslationService translationService;
    
    /**
     * Only extending classes can create this.
     */
    protected ServiceLocator()
    {

    }





    /**
     * The only way to access the instance methods.
     * 
     * @return
     */
    public static final ServiceLocator getInstance()
    {
        return INSTANCE;
    }





    public <T> T getService(Class<T> serviceInterface)
    {
        BundleContext c = ServiceLocator.getBundleContext(serviceInterface);
        if (c == null)
            return null;
        ServiceReference<T> service = c.getServiceReference(serviceInterface);

        return (service == null) ? (T) null : (T) c.getService(service);
    }





    public <T> List<T> getServices(Class<T> serviceInterface)
    {
        try
        {
            return getServices(serviceInterface, null);
        }
        catch (Throwable t)
        {
            // Should never reach here.
            return null;
        }
    }




    
    

    public <T> List<T> getServices(Class<T> serviceInterface, String filterString) throws Throwable
    {
        BundleContext c = ServiceLocator.getBundleContext(serviceInterface);
        if (c == null)
            return null;

        Collection<ServiceReference<T>> services = c.getServiceReferences(serviceInterface, filterString);
        if (services == null || services.isEmpty())
            return null;
        List<T> list = new ArrayList<T>();
        for (ServiceReference<T> sa : services)
            list.add(c.getService(sa));
        return list;
    }





    public <T extends Object> T getService(Class<T> serviceInterface, String filterString) throws Throwable
    {
        BundleContext c = ServiceLocator.getBundleContext(serviceInterface);
        if (c == null)
            return null;

        Collection<ServiceReference<T>> services = c.getServiceReferences(serviceInterface, filterString);
        if (services == null || services.isEmpty())
            return null;
        return c.getService(services.iterator().next());
    }







    public static final BundleContext getBundleContext(Class<?> clazz)
    {
        if (FrameworkUtil.getBundle(clazz) == null)
            return null;
        return FrameworkUtil.getBundle(clazz).getBundleContext();
    }





    public static final Bundle getBundle(Class<?> clazz)
    {
        return FrameworkUtil.getBundle(clazz);
    }
    
    
    
    
    
    /**
     * Preferred way to get ServiceReferences from the OSGi Service Registry for a given
     * serviceClass object.    
     * This is the non-filtered variant of the more general filtered method {@link #getServiceReferences(Class, String)}
     * 
     * 
     * @param serviceClass service class that we want to find registered services for.  Typically this is an Interface class.
     * 
     * @return List<ServiceReference<T>> objects if any are found.   The list is sorted according to service rank and then service id.
     */   
    public <T> List<ServiceReference<T>> getServiceReferences(Class<T> serviceClass)
    {
        return getServiceReferences(serviceClass, null);
    }
    
    
    
    
    
    
    /**
     * Preferred way to get ServiceReferences from the OSGi Service Registry for a given
     * serviceClass object.
     * 
     * @param serviceClass service class that we want to find registered services for.  Typically this is an Interface class.
     * @param filter LDAP syntax form String filter
     * 
     * @return List<ServiceReference<T>> objects if any are found.   The list is sorted according to service rank and then service id.
     */
    private <T> List<ServiceReference<T>> getServiceReferences(Class<T> serviceClass, String filter)
    {
        try
        {
            BundleContext bctx = FrameworkUtil.getBundle(serviceClass).getBundleContext();
            Collection<ServiceReference<T>> services = bctx.getServiceReferences(serviceClass,filter);
            if (CollectionUtil.isListNullOrEmpty(services))
                return null;
            ServiceComparator<T> sc = new ServiceComparator<>();
            return services.stream().sorted(sc).collect(Collectors.toList());
        }
        catch (Throwable t)
        {
            logger.error(t.getMessage(),t);
            return null;
        }
    }
    
    
    
    
    /**
     * Returns an {@link ILogger} instance which can be used for logging purposes. Note that the level and configuration
     * of the logger is done by the {@link ILoggerFactory} service and its implementation provider.
     * 
     * @return ILogger
     */
    public static ILogger getLogger()
    {
        ILogger logger = INSTANCE.logger;
        if (logger == null)
        {
            ILoggerFactory loggerFactory = INSTANCE.getService(ILoggerFactory.class);
            INSTANCE.logger = (loggerFactory == null) ? new LogDelegate() : loggerFactory.getLogger();
        }
        return INSTANCE.logger;
    }
    
    
    
    
    
    /**
     * Returns an {@link ITranslationService} instance which can be used for bundle resource message look ups.
     * 
     * @return ITranslationService
     */
    public static ITranslationService getTranslationService()
    {
        ITranslationService translationService = INSTANCE.translationService;
        if (translationService == null)
        {
            INSTANCE.translationService = INSTANCE.getService(ITranslationService.class);
        }
        return INSTANCE.translationService;
    }

}