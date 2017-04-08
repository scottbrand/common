package test.io.github.scottbrand.common.strings;

import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import io.github.scottbrand.common.Glob;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.logging.ILogger;

@Component
public class StringsTest
{
    
    ILogger      log;
    

    @Activate
    private void activate()
    {
        log = ServiceLocator.getLogger();
        log.consoleLog("Starting Strings Test");
        doTest();
        log.consoleLog("Strings Test Ended");
        System.exit(0);
    }
    
    
    
    private void doTest()
    {
        try
        {
            Glob g = new Glob("Cons*tra");
            System.out.println(g.convertGlobToRegEx("Cons*tra"));
            System.out.println("Match?: " + g.toPattern("*trae*", Pattern.CASE_INSENSITIVE).asPredicate().test("Cons trade"));
            Parent p = new Parent();
            Child  c = new Child();
            log.consoleLog("Parent: {}",p);
            log.consoleLog("Child: {}",c);
            c.setParent(p);
            log.consoleLog("Parent #2: {}",p);
            log.consoleLog("Child #2: {}",c);
            p.addChild(c);
            log.consoleLog("Parent #3: {}",p);
            log.consoleLog("Child #3: {}",c);
            
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }
}
