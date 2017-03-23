package io.github.scottbrand.common;

import java.util.Map;

import io.github.scottbrand.common.tuple.Pair;

/**
 * Interface to be used in the case where you want to clone an existing
 * object and keep a reference to the <b>source</b> object.
 * 
 * This type of pattern is good for usage in UI displays where for example,
 * we want to display a List of objects in a Table Control.
 * That table control allows for editing of the values and we want to be
 * able to detect if changes to any of the values have been made compared to
 * what was originally displayed in the table.
 * Therefore, if the list of objects that we are displaying in the table are
 * of class A, then we would create a class named something like <b>FacadeA</b>
 * and it would implement this interface as well.
 * For example,
 * <pre>
 * 
 * 
 * class FacadeA extends A implements ICloner<A>
 * {
 *      A source;
 *      
 *      public FacadeA(A source)
 *      {
 *          super();
 *          clone(source);
 *      }
 * }
 *  
 * </pre>
 * 
 * 
 * From the above example, our new class FacadeA, has all of the same get/set methods 
 * as defined by class <b>A</b>, our new FacadeA, has a reference to the original source
 * as well, and the clone method has initialized all of the common non-static fields in 
 * our new facade with the values from the source A.
 * 
 * <b>Note:</b>  The pattern, interface and {@link FacadeCloner} currently does not deep copy
 * reference types like Maps, Lists, Sets, etc.   It is primarily useful when the class A 
 * contains standard objects like Numbers, Strings, Date.   Other field values will be still 
 * be referencing the source object, so beware.
 * 
 * 
 * @author Scott Brand (Scott Brand)
 *
 * @param <S> - The type of Object that we are cloning.
 */
public interface ICloner<S>
{
    
    
    /**
     * Method to get the source object.
     * @return
     */
    S getSourceObject();
    
    
    
    /**
     * Method to set the source reference of the soruce object
     * @param source
     */
    void setSourceObject(S source);
    

    
    /**
     * matches field values between Source and Target to 
     * determine if any values have been changed.
     * See the {@link FacadeCloner} class for how this is implemented.
     * 
     * @return Map of fields and their values that are different.
     */
    default <T,SRC> Map<String,Pair<Object,Object>> getDifferences() 
    {
        return FacadeCloner.getDifferences(this, getSourceObject());
    }
    
    
    
    /**
     * matches field values between Source and Target to 
     * determine if any values have been changed.
     * See the {@link FacadeCloner} class for how this is implemented.
     * 
     * @return true if difference detected, false else wise.
     */
    default boolean isDifferent()
    {
        return FacadeCloner.isDifferent(this, getSourceObject());
    }
    
    
    
    default int mergeDifferences()
    {
        return FacadeCloner.mergeDifferences(this, getSourceObject());
    }
    
    
    /**
     * The intent of this method is to be called during the construction
     * of the Target object and here we will keep a reference to the Source object
     * via the {@link #setSource(Object)} method.
     * Then this method will use the {@link FacadeCloner} to initializes our 
     * Target object with data from the source.
     * 
     * @param source - the object we are wanting clone.
     * @return this instance.
     */
    default Object clone(S source)
    {
        setSourceObject(source);
        FacadeCloner.clone(this, source);
        return this;
    }
    
}