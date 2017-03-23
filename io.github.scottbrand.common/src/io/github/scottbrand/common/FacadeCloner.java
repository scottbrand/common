package io.github.scottbrand.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import io.github.scottbrand.common.tuple.Pair;


/**
 * Simple class used to copy the elements from an extended class
 * into the subclass instance.
 * For example, we might have a class named <code>JobFacade</code>
 * which extends the class <code>Job</code>.
 * We now want to create an instance of JobFacade and we pass in an
 * instance of <code>Job</code> into the constructor.
 * The constructor can use the <code>FacadeCloner</code> to
 * copy the elements from the <code>Job</code> object down into
 * the Facade object.
 * Note that the FacadeCloner usage is intended to copy POJO style objects
 * like those that came from the database (i.e. JPA entities).
 * Only primitives, String and Date style fields are intended to be cloned.
 * This class does not recursive into nested classes. 
 *  
 * 
 * @author Scott Brand
 *
 */
public class FacadeCloner
{
	
    
    
    /**
     * clone (really copy) the field values from <S> into
     * the equivalent named fields of <T>
     * <T> should be a subclass of <S>.
     * 
     * @param target - the subclass instance of <S> that will get a copy of <S> field values. 
     * @param source - the superclass instance of <T> and is used to get field values to be copied.
     */
    public static final <T,S> void clone(T target, S source)
    {
        if (source == null || target == null)
            return;
        
        Class<?> c = source.getClass();
        Class<?> t = target.getClass().getSuperclass();
        
        while (c != null && t != null)
        {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields)
            {
                int m = f.getModifiers();
                if (Modifier.isStatic(m))
                    continue;
                try
                {
                    Field newField = t.getDeclaredField(f.getName());
                    f.setAccessible(true);
                    newField.setAccessible(true);
                    newField.set(target, f.get(source));
                }
                catch (Throwable th)
                {
                    ServiceLocator.getLogger().error("Unable to clone facade",th);
                    continue;
                }
            }
            c = c.getSuperclass();
            if (c == Object.class)
                break;
            t = t.getSuperclass();
        }
    }

    
    
    /**
     * Compare the target object <T> to the source object <S>
     * Iterates over every field in <S> and compares the field value 
     * to the target <T> field.
     * Comparison is done via standard Object.equals(Object) method,
     * 
     * @param target
     * @param source
     * @return true if any fields contain different values, false if everything is equal.
     */
    public static final <T,S> boolean isDifferent(T target, S source)
    {
        if (source == null || target == null)
            return true;
        
        Class<?> c = source.getClass();
        Class<?> t = target.getClass().getSuperclass();
        
        while (c != null && t != null)
        {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields)
            {
                int m = f.getModifiers();
                if (Modifier.isStatic(m))
                    continue;
                try
                {
                    Field newField = t.getDeclaredField(f.getName());
                    f.setAccessible(true);
                    newField.setAccessible(true);
                    if (f.get(source) == null)
                    {
                        if (newField.get(target) != null)
                            return true;
                    }
                    else if (f.get(source).equals(newField.get(target)) == false)
                        return true;
                }
                catch (Throwable th)
                {
                    ServiceLocator.getLogger().error("Unable to get Different",th);
                    continue;
                }
            }
            c = c.getSuperclass();
            if (c == Object.class)
                break;
            t = t.getSuperclass();
        }

        return false;
    }
    

    
    
    /**
     * Compare the target object <T> to the source object <S>
     * Iterates over every field in <S> and compares the field value 
     * to the target <T> field.
     * Comparison is done via standard Object.equals(Object) method,
     *
     * Differences are collected in a Map that is keyed by the fieldName
     * and the value at that key is a Pair<A,B> where the Pair contains 
     * the values of the fields from S and T respectively. 
     * 
     * @param target
     * @param source
     * 
     * @return Map<String,Pair<Object,Object>>
     */
    public static final <T,S> Map<String,Pair<Object,Object>> getDifferences(T target, S source)
    {
        if (target == null || source == null)
            return null;
        
        Class<?> c = source.getClass();
        Class<?> t = target.getClass().getSuperclass();
        
        Map<String,Pair<Object,Object>> result = new HashMap<>();
        
        while (c != null && t != null)
        {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields)
            {
                int m = f.getModifiers();
                if (Modifier.isStatic(m))
                    continue;
                try
                {
                    Field newField = t.getDeclaredField(f.getName());
                    f.setAccessible(true);
                    newField.setAccessible(true);
                    if (f.get(source) == null)
                    {
                        if (newField.get(target) != null)
                            result.put(f.getName(),new Pair<Object, Object>(f.get(source),newField.get(target)));
                    }
                    else if (f.get(source).equals(newField.get(target)) == false)
                    {
                        result.put(f.getName(),new Pair<Object, Object>(f.get(source),newField.get(target)));
                    }
                }
                catch (Throwable th)
                {
                    ServiceLocator.getLogger().error("Unable to getDifferences",th);
                    continue;
                }
            }
            c = c.getSuperclass();
            if (c == Object.class)
                break;
            t = t.getSuperclass();
        }

        return result;
    }    
    
    
    
    /**
     * Compare the target object <T> to the source object <S>
     * Iterates over every field in <S> and compares the field value 
     * to the target <T> field.
     * Comparison is done via standard Object.equals(Object) method,
     *
     * If a difference is found then the value of the field in target is
     * pushed into the corresponding field in source.
     * 
     * @param target - The cloned object
     * @param source - The object that was the source of the target
     * 
     * @return int - the number changes merged (number of field affected in source)
     */
    public static final <T,S> int mergeDifferences(T target, S source)
    {
        if (target == null || source == null)
            return 0;
        
        Class<?> c = source.getClass();
        Class<?> t = target.getClass().getSuperclass();
        
        int change = 0;
        while (c != null && t != null)
        {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields)
            {
                int m = f.getModifiers();
                if (Modifier.isStatic(m))
                    continue;
                try
                {
                    Field newField = t.getDeclaredField(f.getName());
                    f.setAccessible(true);
                    newField.setAccessible(true);
                    if (f.get(source) == null)
                    {
                        if (newField.get(target) != null)
                        {
                            f.set(source, newField.get(target));
                            change++;
                        }
                    }
                    else if (f.get(source).equals(newField.get(target)) == false)
                    {
                        f.set(source, newField.get(target));
                        change++;
                    }
                }
                catch (Throwable th)
                {
                    ServiceLocator.getLogger().error("Unable to mergeDifferences",th);
                    continue;
                }
            }
            c = c.getSuperclass();
            if (c == Object.class)
                break;
            t = t.getSuperclass();
        }

        return change;
    }    
    
    
    
    
    
    /**
     * Compare the target object <T> to the source object <S>
     * Iterates over every field in <S> and compares the field value 
     * to the target <T> field.
     * Comparison is done via standard Object.equals(Object) method,
     *
     * If a difference is found then the value of the field in target is
     * pushed into the corresponding field in source.
     * 
     * The method differs from {@link #mergeDifferences(Object, Object)}
     * since mergeDifferences is expecting target to be a Facade object of
     * source.   In the case of the method here, we expect the objects being
     * compared to be of exactly the same type.
     * 
     * @param target - The cloned object
     * @param source - The object that was the source of the target
     * 
     * @return int - the number changes merged (number of field affected in source)
     */
    public static final <T> int mergeObjectDifferences(T target, T source)
    {
        if (target == null || source == null)
            return 0;
        
        Class<?> c = source.getClass();
        Class<?> t = target.getClass();
        
        int change = 0;
        while (c != null && t != null)
        {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields)
            {
                int m = f.getModifiers();
                if (Modifier.isStatic(m))
                    continue;
                try
                {
                    Field newField = null;
                    
                    try
                    {
                        newField = t.getDeclaredField(f.getName());
                    }
                    catch (Throwable ndf)
                    {
                        newField = null;
                    }
                    if (newField == null)
                        continue;
                    f.setAccessible(true);
                    newField.setAccessible(true);
                    if (f.get(source) == null)
                    {
                        if (newField.get(target) != null)
                        {
                            f.set(source, newField.get(target));
                            change++;
                        }
                    }
                    else if (f.get(source).equals(newField.get(target)) == false)
                    {
                        f.set(source, newField.get(target));
                        change++;
                    }
                }
                catch (Throwable th)
                {
                    ServiceLocator.getLogger().error("Unable to mergeDifferences",th);
                    continue;
                }
            }
            c = c.getSuperclass();
            if (c == Object.class)
                break;
            t = t.getSuperclass();
        }

        return change;
    }    
    
    
    
    
    /**
     * Method to use reflection to change a specific field value on the given object.
     * This method will follow the class hierarchy down looking for the requested
     * field name.
     * 
     * This method is primarily used to set the EMF object (eContainer) to null.
     * 
     * @param o
     * @param fieldName
     * @param newValue
     * @return
     */
    public static final boolean setField(Object o,String fieldName, String newValue)
    {
        if (o == null || Strings.isNullOrEmpty(fieldName))
            return false;
        
        Class<?> c = o.getClass();
        while (c != null)
        {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields)
            {
                if (f.getName().equals(fieldName))
                {
                    f.setAccessible(true);
                    try
                    {
                        f.set(o, newValue);
                        return true;
                    }
                    catch (Throwable e)
                    {
                        return false;
                    }
                }
            }
            c = c.getSuperclass();
            if (c == Object.class)
                break;
        }
        return false;
    }
    
    
    
    
    
    
    public static final boolean setObjectField(Object o,String fieldName, Object newValue)
    {
        if (o == null || Strings.isNullOrEmpty(fieldName))
            return false;
        
        Class<?> c = o.getClass();
        while (c != null)
        {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields)
            {
                if (f.getName().equals(fieldName))
                {
                    f.setAccessible(true);
                    try
                    {
                        f.set(o, newValue);
                        return true;
                    }
                    catch (Throwable e)
                    {
                        return false;
                    }
                }
            }
            c = c.getSuperclass();
            if (c == Object.class)
                break;
        }
        return false;
    }
}
