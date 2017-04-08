package test.io.github.scottbrand.common.strings;

import java.util.Date;
import java.util.List;

import io.github.scottbrand.common.Create;
import io.github.scottbrand.common.Strings;

public class Parent
{
    int integer = 1;
    String hello = "world";
    Date  now = new Date();
    Object nullObject = null;
    
    List<Child> children = Create.list(Child.class);
    
    public String toString()
    {
        return Strings.toString(this);
    }
    
    
    
    public void addChild(Child c)
    {
        children.add(c);
    }
}
