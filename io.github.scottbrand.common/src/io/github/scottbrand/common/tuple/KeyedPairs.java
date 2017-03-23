package io.github.scottbrand.common.tuple;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * A KeyedPairs object is useful for patterns where you have a key value
 * and at that value could be a set value or a default value, or a pre-value
 * and an override value.
 * 
 * 
 * @author Scott Brand (Scott Brand)
 *
 * @param <A>
 * @param <B>
 */
public class KeyedPairs<A,B> implements Serializable
{
    private static final long serialVersionUID = 6813539288935231512L;
    
    private Map<String,Pair<A,B>> pairs = new HashMap<>();
    
    public KeyedPairs()
    {
        
    }
    
    
    public boolean contains(String key)
    {
        return pairs.containsKey(key);
    }
    
    
    public Set<String> keySet()
    {
        return pairs.keySet();
    }
    
    
    
    public Collection<Pair<A,B>> values()
    {
        return pairs.values();
    }
    
    
    
    
    
    public void put(String key, Pair<A,B> pair)
    {
        pairs.put(key, pair);
    }
    
    
    
    
    public void put(String key, A a, B b)
    {
        pairs.put(key, Pair.newInstance(a, b));
    }
    
    
    
    
    public Pair<A,B> get(String key)
    {
        return pairs.get(key);
    }
    
    
    
    
    
    
    
}
