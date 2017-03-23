package io.github.scottbrand.common;

/**
 * Simple wrapper object to be used in cases where you want a method to return a
 * typed value of some kind (i.e. String), but also be able to inspect a Throwable that might have been
 * caught during the method call when the return value is false. Thus it leaves
 * the caller in charge of what to do in the case of an exception.
 * 
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public final class TypedResult<T> implements AutoCloseable
{
    private T                         result;
    private Throwable                 throwable;






    /**
     * Default constructor which sets the result to <code>null</code> and the
     * throwable to <code>null</code>
     */
    protected TypedResult()
    {
        result    = null;
        throwable = null;
    }


    
    
    
    
    /**
     * Constructor which sets the result to given T value and the
     * throwable to <code>null</code>
     */
    public TypedResult(T result)
    {
        this();
        this.result = result;
    }



    
    
    
    /**
     * Constructor which sets the result to null and the
     * throwable to the given <code>throwable</code>
     */
    public TypedResult(Throwable t)
    {
        this();
        throwable = t;
    }




    /**
     * Get the T result object
     * 
     * @return T value
     */
    public T getResult()
    {
        return result;
    }




    /**
     * Get the throwable if any 
     * @return
     */
    public Throwable getThrowable()
    {
        return throwable;
    }
    
    
    
    /**
     * convenience method to easily detect that the result T is not null
     * and that the throwable is null, which would imply that we have a good
     * result type.
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        return result != null && throwable == null;
    }






    @Override
    public void close() throws Exception
    {
        if (result != null && result instanceof AutoCloseable)
            ((AutoCloseable)result).close();
    }

}
