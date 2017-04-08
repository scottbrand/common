package test.io.github.scottbrand.common.strings;

import io.github.scottbrand.common.Strings;

public class Child
{
    Parent parent;
    Long   longValue = 100L;
    
    public String toString()
    {
        return Strings.toString(this);
    }
    
    
    
    public void setParent(Parent p)
    {
        parent = p;
    }
}
