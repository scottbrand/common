package test.io.github.scottbrand.common.cloner;

import java.util.Date;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import io.github.scottbrand.common.CollectionUtil;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.tuple.Pair;

@Component(immediate=true)
public class ClonerTester
{
    protected ILogger log = null;
    
    @Activate
    public void activate()
    {
        log = ServiceLocator.getLogger();
        
        SourceObject source = new SourceObject(11, 10000L, new Date(), "Some String", DummyEnum.VALUE1, new Integer(10), new Long(99));
        TargetObject target = new TargetObject(source);
        
        printObjects(source,target);   
        
        target.setAnEnum(DummyEnum.VALUE2);
        target.setPrimitiveLong(9L);
        target.setObjectLong(-1L);
        target.setaDate(new Date());
        target.setaString("Hello World");
        
        printObjects(source,target);
    }
    
    
    
    private void printObjects(SourceObject source, TargetObject target)
    {
        log.debug("source is: {}",Strings.toString(source));
        log.debug("target is: {}",Strings.toString(target));
        log.debug("isDifferent: {}",target.isDifferent());
        Map<String,Pair<Object,Object>> differences = target.getDifferences();
        if (CollectionUtil.isMapPopulated(differences))
            log.debug("differences: {}",differences);
    }
}
