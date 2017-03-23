package io.github.scottbrand.common;

import java.util.Comparator;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;



/**
 * Utility class that provides a comparator that uses an OSGi services ranking value
 * and then service id, to determine the ordered set of services to be returned from
 * a given set of ServiceReference objects.
 * 
 * @author Scott Brand (Scott Brand)
 *
 * @param <T> - The type contained within the ServiceReference.
 */
public class ServiceComparator<T> implements Comparator<ServiceReference<T>>
{

    @Override
    public int compare(ServiceReference<T> o1, ServiceReference<T> o2)
    {
        Object obj1 = o1.getProperty(Constants.SERVICE_RANKING);
        Object obj2 = o2.getProperty(Constants.SERVICE_RANKING);
        
        int sr1 = obj1 == null ? 0 : obj1 instanceof Number ? (Integer)obj1 : Numbers.getInteger(obj1.toString(), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        int sr2 = obj2 == null ? 0 : obj2 instanceof Number ? (Integer)obj2 : Numbers.getInteger(obj2.toString(), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        
        if (sr2 != sr1)
            return sr2-sr1;

        obj1 = o1.getProperty(Constants.SERVICE_ID);
        obj2 = o2.getProperty(Constants.SERVICE_ID);
        
        long sid1 = obj1 == null ? 0 : obj1 instanceof Number ? (Long)obj1 : Numbers.getLong(obj1.toString(), 0, Long.MIN_VALUE, Long.MAX_VALUE);
        long sid2 = obj2 == null ? 0 : obj2 instanceof Number ? (Long)obj2 : Numbers.getLong(obj2.toString(), 0, Long.MIN_VALUE, Long.MAX_VALUE);
        
        return (int)(sid2-sid1);
    }
    
}
