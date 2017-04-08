package test.io.github.scottbrand.common.main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.VariableReplacer;
import io.github.scottbrand.common.tuple.Pair;

public class StringTest
{




    public static void main(String[] args)
    {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("color", "brown");
        map.put("scheme", "url");
        map.put("env", "test");
        map.put("url.prod", "prod hostname");
        map.put("url.test", "test hostname");
        map.put("url.dev", "dev hostname");
        
        ServiceLocator.getLogger().debug("The quick ${color} fox jumped over the moon =" + VariableReplacer.replaces("The quick ${color} fox jumped over the moon",map));
        ServiceLocator.getLogger().debug("The quick ${color} fox jumped over the moon =" + VariableReplacer.replaces("The quick ${color} fox jumped $ over the \\$ moon from \\${user.home}",map));       
        ServiceLocator.getLogger().debug("The quick ${" + VariableReplacer.replaces("The quick ${",map));
        ServiceLocator.getLogger().debug("The quick } fox" + VariableReplacer.replaces("The quick } fox",map));
        ServiceLocator.getLogger().debug("The quick \\${ fox" + VariableReplacer.replaces("The quick \\${ fox",map));
        ServiceLocator.getLogger().debug("The quick ${url.${env}} is: " + VariableReplacer.replaces("The quick ${url.${env}}",map) + ":");
        
        
        Pair<String,String> p = new Pair<>("Hello",null);
        ServiceLocator.getLogger().debug("pair is: {}",p);
        
        Date d = new Date();

        
        //Calendar c = new GregorianCalendar();
        
        System.out.println("Date is: " + d.toString());
        //c.setTime(d);
        //c.setTimeZone(TimeZone.getTimeZone("US/Central"));
        //System.out.println("Date adjusted is: " + c.getTime().toString());
        
        long fromTimeZoneOffset = getTimeZoneUTCAndDSTOffset(d, TimeZone.getDefault());
        long toTimeZoneOffset = getTimeZoneUTCAndDSTOffset(d, TimeZone.getTimeZone("US/Central"));

        Date d2 = new Date(d.getTime() + (toTimeZoneOffset - fromTimeZoneOffset));
        System.out.println("Date adjusted is: " + d2.toString());
        
        
    }
    
    
    private static long getTimeZoneUTCAndDSTOffset(Date date, TimeZone timeZone)
    {
        long timeZoneDSTOffset = 0;
        if(timeZone.inDaylightTime(date))
        {
            timeZoneDSTOffset = timeZone.getDSTSavings();
        }

        return timeZone.getRawOffset() + timeZoneDSTOffset;
    }
}
