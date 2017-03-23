package io.github.scottbrand.common;

import java.security.MessageDigest;
import java.util.Arrays;



/**
 * The SessionDigest object is a simple object that can hold a byte[]
 * and calculate a SHA-1 hash on it.
 * 
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public class SessionDigest
{
    
    public static final String HASH_ALGORITHM = "SHA-1";
    byte[] d;
    long   ttl;
    
    
    public SessionDigest(byte[] ba)
    {
        setDigest(ba);
        this.ttl = System.currentTimeMillis();
    }
    
    
    
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof SessionDigest))
            return false;
        return Arrays.equals(((SessionDigest)other).d, d);
    }
    
    
    public byte[] getDigest()
    {
        return d;
    }
    
    
    public void setDigest(byte[] ba)
    {
        try
        {
            MessageDigest md;
            md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(ba, 0, ba.length);
            byte[] temp = md.digest();
            d = Arrays.copyOf(temp,temp.length);
        }
        catch (Throwable t)
        {
            ServiceLocator.getLogger().error(t.getMessage(),t);
        }
    }
    
    
    
    public long getTTL()
    {
        return ttl;
    }

}
