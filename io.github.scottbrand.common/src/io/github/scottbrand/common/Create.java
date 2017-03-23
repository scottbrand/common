package io.github.scottbrand.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Create
{
    
    
    
    
    /**
     * Creates a LinkedHashMap of K and V types.
     * 
     * @return
     */
	public static <K, V> Map<K, V> map()
	{
		return new LinkedHashMap<K, V>();
	}





	public static <K, V> Map<K, V> map(Class<K> key, Class<V> value)
	{
		return Collections.checkedMap(new LinkedHashMap<K, V>(), key, value);
	}


	
	
	




	public static <T> List<T> list()
	{
		return new ArrayList<T>();
	}

	
	public static <T> List<T> linkedList()
    {
        return new LinkedList<T>();
    }

	
	
	




	public static <T> List<T> list(Class<T> c)
	{
		return Collections.checkedList(new ArrayList<T>(), c);
	}
	
	
	
    public static <T> List<T> linkedList(Class<T> c)
    {
        return Collections.checkedList(new LinkedList<T>(), c);
    }





	public static <T> Set<T> set()
	{
		return new LinkedHashSet<T>();
	}





	public static <T> Set<T> set(Class<T> c)
	{
		return Collections.checkedSet(new LinkedHashSet<T>(), c);
	}


	
	/**
	 * Useful for creating Dictionary object instances for OSGi Service Registration calls.
	 * 
	 * @param k
	 * @return
	 */
	public static <T> Dictionary<T,Object> dictionary(Class<T> k)
	{
	    return new Hashtable<T,Object>();
	}
	
	

    /**
     * Useful for creating Dictionary object instances for OSGi Service Registration calls.
     * This method will create a new instance of a Dictionary of type <String,?>.
     * 
     * @param k
     * @return
     */
    public static Dictionary<String,Object> dictionary()
    {
        return new Hashtable<String,Object>();
    }
	
	
    
    /**
     * Useful for creating Dictionary object instances for OSGi Service Registration calls.
     * 
     * @param k
     * @return
     */
    public static <T,V> Dictionary<T,V> dictionary(Class<T> k, Class<V> v)
    {
        return new Hashtable<T,V>();
    }



	public static <T> List<T> list(T[] source)
	{
		return new ArrayList<T>(Arrays.asList(source));
	}





	public static <T> Set<T> set(T[] source)
	{
		return new HashSet<T>(Arrays.asList(source));
	}





	public static <K, V> Map<K, V> copy(Map<K, V> source)
	{
		return new LinkedHashMap<K, V>(source);
	}





	public static <T> List<T> copy(List<T> source)
	{
		return new ArrayList<T>(source);
	}





	public static <T> Set<T> copy(Collection<T> source)
	{
		if (source == null)
			return set();
		return new LinkedHashSet<T>(source);
	}
	
	
	
	
	/**
	 * Accept an object which should be serializable, and 
	 * serialize it to a byte[]
	 * 
	 * @param object
	 * @return
	 */
	public static <T> byte[] bytes(T object)
	{
        try(ByteArrayOutputStream os = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(os))
        {
            oos.writeObject(object);
            return os.toByteArray();
        }
        catch (Exception t)
        {
            ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
            return null;
        }
	}
	
	
	
	
	/**
	 * Accepts a byte[] which should be a previously serialized java object
	 * and convert that byte[] back to an object of type T.
	 * 
	 * @param objectBytes
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(byte[] objectBytes)
    {
        try(ByteArrayInputStream os = new ByteArrayInputStream(objectBytes); ObjectInputStream oos = new ObjectInputStream(os))
        {
            Object o = oos.readObject();
            return (o == null) ? null : (T)o;
        }
        catch (Exception t)
        {
            ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
            return null;
        }
    }
	
	
	
}
