package io.github.scottbrand.common;

public class Numbers
{

    public enum Operators
    {
        EQUAL,
        NOT_EQUAL,
        LESS_THAN,
        LESS_THAN_OR_EQUAL,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
    };
    
    
    public enum RangeOperators
    {
        BETWEEN,
        NOT_BETWEEN
    };
    
    
    
    
    
    public static final Integer getInteger(String value, int defaultValue, Integer min, Integer max)
    {
        if (value == null)
            return defaultValue;
        int v = defaultValue;
        try
        {
            v = Integer.parseInt(value);
            if (min != null && v < min)
                return defaultValue;
            if (max != null && v > max)
                return defaultValue;
            return v;
        }
        catch (Throwable t)
        {
            return defaultValue;
        }
    }





    public static final Long getLong(String value, long defaultValue, Long min, Long max)
    {
        if (value == null)
            return defaultValue;
        long v = defaultValue;
        try
        {
            v = Long.parseLong(value);
            if (min != null && v < min)
                return defaultValue;
            if (max != null && v > max)
                return defaultValue;
            return v;
        }
        catch (Throwable t)
        {
            return defaultValue;
        }
    }





    /**
     * Special case of getLong(String value, long defaultValue, Long min, Long max) in that this method only trys to
     * parse the object as a Long and will return Long.MAX_VALUE in any case in which it can not be parsed as a Long. If
     * the given object is a String, then it is cast to a String before parsing otherwise the value that is parsed is
     * equivalent to value.toString();
     * 
     * @param value
     * @return
     */
    public static final Long getLong(Object value)
    {
        if (value == null)
            return Long.MAX_VALUE;
        long v = Long.MAX_VALUE;
        try
        {
            v = Long.parseLong(value instanceof String ? (String) value : value.toString());
            return v;
        }
        catch (Throwable t)
        {
            return Long.MAX_VALUE;
        }
    }





    /**
     * Special case of getLong(String value, long defaultValue, Long min, Long max) in that this method only trys to
     * parse the object as a Long and will return Long.MAX_VALUE in any case in which it can not be parsed as a Long. If
     * the given object is a String, then it is cast to a String before parsing otherwise the value that is parsed is
     * equivalent to value.toString();
     * 
     * @param value
     * @return
     */
    public static final Integer getInteger(Object value)
    {
        if (value == null)
            return Integer.MAX_VALUE;
        int v = Integer.MAX_VALUE;
        try
        {
            v = Integer.parseInt(value instanceof String ? (String) value : value.toString());
            return v;
        }
        catch (Throwable t)
        {
            return Integer.MAX_VALUE;
        }
    }





    public static final Short getShort(Object value)
    {
        if (value == null)
            return Short.MAX_VALUE;
        short v = Short.MAX_VALUE;
        try
        {
            v = Short.parseShort(value instanceof String ? (String) value : value.toString());
            return v;
        }
        catch (Throwable t)
        {
            return Short.MAX_VALUE;
        }
    }





    public static final Float getFloat(Object value)
    {
        if (value == null)
            return Float.MAX_VALUE;
        float v = Float.MAX_VALUE;
        try
        {
            v = Float.parseFloat(value instanceof String ? (String) value : value.toString());
            return v;
        }
        catch (Throwable t)
        {
            return Float.MAX_VALUE;
        }
    }





    public static final Double getDouble(Object value)
    {
        if (value == null)
            return Double.MAX_VALUE;
        double v = Double.MAX_VALUE;
        try
        {
            v = Double.parseDouble(value instanceof String ? (String) value : value.toString());
            return v;
        }
        catch (Throwable t)
        {
            return Double.MAX_VALUE;
        }
    }





    public static final boolean equals(Short v1, Short v2)
    {
        if (v1 == v2)
            return true;
        if (v1 == null || v2 == null)
            return false;
        return v1.intValue() == v2.intValue();
    }





    public static final boolean equals(Integer v1, Integer v2)
    {
        return eval(v1, Operators.EQUAL, v2);
    }
    
    
    
    
    /**
     * Useful method to check if an Integer object is null or set to
     * some default value.
     * 
     * @param v1  Integer object which could be null or contain a value
     * @param v2  the primitive int value from which we want to equate to.
     * 
     * @return true if v1 is null or v1 = v2. 
     */
    public static final boolean isNullOrEquals(Integer v1, int v2)
    {
        return (v1 == null || v1.intValue() == v2);
    }


   
    
    /**
     * Predicate operation to evaluate parameter <code>v1</code>.
     * If v1 is null, return false;
     * Other wise, use the operator and v2, evaluate the expression
     * as being true or false.
     * 
     * @param v1  Integer object used as operand1 (if null, method evaluate to false)
     * @param op  comparison operator
     * @param v2  Integer object used as operand2 (if null, method evaluate to false)
     * @return
     * 
     */
    public static final boolean eval(Integer v1, Operators op, Integer v2)
    {
        if (v1 == null || v2 == null)
            return false;
        int o1 = v1.intValue();
        int o2 = v2.intValue(); 
        if (op == Operators.EQUAL)
            return o1 == o2;
        if (op == Operators.NOT_EQUAL)
            return o1 != o2;
        if (op == Operators.LESS_THAN)
            return o1 < o2;
        if (op == Operators.LESS_THAN_OR_EQUAL)
            return o1 <= o2;
        if (op == Operators.GREATER_THAN)
            return o1 > o2;
        if (op == Operators.GREATER_THAN_OR_EQUAL)
            return o1 >= o2;
        return false;
    }

    
    
    
    
    
    /**
     * Predicate operation to evaluate parameter <code>v1</code>.
     * If v1 is null, return false;
     * Other wise, use the range operator and lower and upper ranges to 
     * evaluate the expression as being true or false.
     * 
     * lower and upper bounds are inclusive in the comparison.
     * 
     * @param v1  Integer object used as operand1 (if null, method evaluate to false)
     * @param op  comparison operator
     * @param lower  Integer object used as lower bound value (if null, method evaluate to false)
     * @param upper  Integer object used as upper bound value (if null, method evaluate to false)
     * @return
     * 
     */
    public static final boolean eval(Integer v1, RangeOperators op, Integer lower, Integer upper)
    {
        if (v1 == null || lower == null || upper == null)
            return false;
        int ov = v1.intValue();
        int lv = lower.intValue();
        int uv = upper.intValue();
        
        return (op == RangeOperators.BETWEEN) ? ov >= lv && ov <= uv : !(ov >= lv && ov <= uv);
    }
    
    

    
    /**
     * Predicate operation to evaluate parameter <code>v1</code>.
     * If v1 is null, return false;
     * Other wise, use the range operator and lower and upper ranges to 
     * evaluate the expression as being true or false.
     * 
     * lower and upper bounds are inclusive in the comparison.
     * 
     * @param v1  Integer object used as operand1 (if null, method evaluate to false)
     * @param op  comparison operator
     * @param lower  Integer object used as lower bound value (if null, method evaluate to false)
     * @param upper  Integer object used as upper bound value (if null, method evaluate to false)
     * @return
     * 
     */
    public static final boolean eval(Long v1, RangeOperators op, Long lower, Long upper)
    {
        if (v1 == null || lower == null || upper == null)
            return false;
        long ov = v1.longValue();
        long lv = lower.longValue();
        long uv = upper.longValue();
        
        return (op == RangeOperators.BETWEEN) ? ov >= lv && ov <= uv : !(ov >= lv && ov <= uv);
    }

    
    
    
    /**
     * Predicate operation to evaluate parameter <code>v1</code>.
     * If v1 is null, return false;
     * Other wise, use the operator and v2, evaluate the expression
     * as being true or false.
     * 
     * @param v1  Integer object used as operand1 (if null, method evaluate to false)
     * @param op  comparison operator
     * @param v2  Integer object used as operand2 (if null, method evaluate to false)
     * @return
     * 
     */
    public static final boolean eval(Long v1, Operators op, Long v2)
    {
        if (v1 == null || v2 == null)
            return false;
        long o1 = v1.longValue();
        long o2 = v2.longValue(); 
        if (op == Operators.EQUAL)
            return o1 == o2;
        if (op == Operators.NOT_EQUAL)
            return o1 != o2;
        if (op == Operators.LESS_THAN)
            return o1 < o2;
        if (op == Operators.LESS_THAN_OR_EQUAL)
            return o1 <= o2;
        if (op == Operators.GREATER_THAN)
            return o1 > o2;
        if (op == Operators.GREATER_THAN_OR_EQUAL)
            return o1 >= o2;
        return false;
    }
    
    
    
    
    
    /**
     * Useful method to check if an Integer object is null or set to
     * some default value.
     * 
     * @param v1  Integer object which could be null or contain a value
     * @param v2  the primitive int value from which we want to equate to.
     * 
     * @return true if v1 is null or v1 = v2. 
     */
    public static final boolean isNullOrEquals(Long v1, long v2)
    {
        return (v1 == null || v1.longValue() == v2);
    }


   
    
    
    


    public static final boolean equals(Long v1, Long v2)
    {
        return eval(v1,Operators.EQUAL,v2);
    }





    public static final boolean equals(Float v1, Float v2)
    {
        if (v1 == v2)
            return true;
        if (v1 == null || v2 == null)
            return false;
        return v1.floatValue() == v2.floatValue();
    }





    public static final boolean equals(Double v1, Double v2)
    {
        if (v1 == v2)
            return true;
        if (v1 == null || v2 == null)
            return false;
        return v1.doubleValue() == v2.doubleValue();
    }





    public static final String encode32(Long value)
    {
        return encodeBaseX(value, 32);
    }





    public static final String encodeBaseX(Long value, int radix)
    {
        return (value == null) ? Strings.EMPTY : Long.toString(value, radix).toUpperCase();
    }





    /**
     * Convert a number to a String. If the given number object is null, then return the defaultValue. If the
     * defaultValue is null then Strings.EMPTY is returned.
     * 
     * @param someNumber
     * @param defaultValue
     * @return
     */
    public static final String toString(Number someNumber, String defaultValue)
    {
        if (defaultValue == null)
            defaultValue = Strings.EMPTY;

        return (someNumber == null) ? defaultValue : someNumber.toString();
    }





    /**
     * Convert a number to a String. If the given number object is null, then return the defaultValue. If the
     * defaultValue is null then Strings.EMPTY is returned.
     * 
     * @param someNumber
     * @param defaultValue
     * @return
     */
    public static final String toString(Number someNumber)
    {
        return toString(someNumber, Strings.EMPTY);
    }





    /**
     * Convert a number to a String. If the given number object is null, then return the defaultValue. If the
     * 
     * @param someNumber
     * @param defaultValue
     * @return
     */
    public static final String toString(Number someNumber, int defaultValue)
    {
        return someNumber == null ? String.valueOf(defaultValue) : toString(someNumber, null);
    }

}
