package io.github.scottbrand.common;


/**
 * Simple wrapper object to be used in cases where you want a method to return a
 * integer status value, but also be able to inspect a Throwable that might have been
 * caught during the method call when the return value is not 0. Thus it leaves
 * the caller in charge of what to do in the case of an exception.
 * 
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public class IntegerResult
{
    private int                       result;
    private Throwable                 throwable;

    /**
     * Static convenience object to return the good (return code 0) condition without having
     * to explicitly create a "new" object each time.
     */
    public static final IntegerResult GOOD = new IntegerResult();





    /**
     * Default constructor which sets the result to <code>0</code> and the
     * throwable to <code>null</code>
     */
    public IntegerResult()
    {
        result = 0;
        throwable = null;
    }


    
    
    
    
    /**
     * Constructor which sets the result to given int value and the
     * throwable to <code>null</code>
     */
    public IntegerResult(int result)
    {
        this.result = result;
    }



    
    
    
    /**
     * Constructor which sets the result to <code>-1</code> and the
     * throwable to the given <code>throwable</code>
     */
    public IntegerResult(Throwable t)
    {
        result = -1;
        throwable = t;
    }
    
    
    
    /**
     * Constructor which sets the result to the given result value and the
     * throwable to the given <code>throwable</code>
     */
    public IntegerResult(int result, Throwable t)
    {
        this.result = result;
        throwable = t;
    }




    /**
     * Get the int result of this object
     * 
     * @return boolean value
     */
    public int getResult()
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
     * Simple boolean check to see if the current result is 0.
     * @return
     */
    public boolean isGood()
    {
        return result == 0;
    }
    

}
