package test.io.github.scottbrand.common.properties;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.Create;
import io.github.scottbrand.common.PropertyReader;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.security.IPasswordEncryptor;


@Component
public class PropertyTest
{

    private volatile IPasswordEncryptor encryptor;
    
    @Activate
    public void activate()
    {
        PropertyReader pr = new PropertyReader(encryptor);
        
        ServiceLocator.getLogger().debug("${user.home} =" + pr.getProperty("${user.home}"));
        ServiceLocator.getLogger().debug("ENC(B7096B10DB9EF0AFB47B3F174990529E) = " + pr.getProperty("ENC(B7096B10DB9EF0AFB47B3F174990529E)"));
        
        
        Map<String,Object> map = Create.map(String.class,Object.class);
        map.put("foo", "bar");
        map.put("bar.0","this is #0");
        map.put("bar.1","this is #1");
        map.put("bar.2","this is #2");
        map.put("bar.3","this is #3");
        
        pr.setVarMap(map);
        
        ServiceLocator.getLogger().debug("foo becomes ${foo} =" + pr.getProperty("foo becomes ${foo}"));
        Long v = System.currentTimeMillis() % 4;
        map.put("v", v.toString());
        ServiceLocator.getLogger().debug("${bar.${v}}, where v = " + v + " =" + pr.getProperty("${bar.${v}}"));
        ServiceLocator.getLogger().debug("escaping bar replacement: \\${bar.${v}}, where v = " + v + " =" + pr.getProperty("\\${bar.${v}}"));
        ServiceLocator.getLogger().debug("same as above but calling property reader on replaced property reader becomes:"  + pr.getProperty(pr.getProperty("\\${bar.${v}}")));
        
        System.exit(0);
    }
    
    
    
    
    
    
    @Reference
    public void setPasswordEncryptor(IPasswordEncryptor encryptor)
    {
        this.encryptor = encryptor;
    }
    

}
