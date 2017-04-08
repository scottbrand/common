package test.io.github.scottbrand.common.logging;

import java.util.List;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.MDC;

import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.logging.ILoggerFactory;
import io.github.scottbrand.common.logging.LogDelegate;



@Component
public class LoggingTest
{

    private volatile ILoggerFactory loggerFactory = null;





    @Activate
    public void activate(ComponentContext ctx)
    {
        System.out.println("activated");
        try
        {
            doTest();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        System.out.println("done");

        System.exit(0);
    }

    
    
    private void doTest()
    {
        loggerFactory.getLogger().debug("Starting default logger");
        loggerFactory.getLogger().debug("Ending default logger");

        ILogger log = loggerFactory.getContextLogger("jobs", "job-1");
        log.debug("this goes to the {} logger", log.getName());

        LogDelegate ld = (LogDelegate) log;
        log.consoleLog("Pattern: " + ld.getPattern());
        log.trace("this is log2 with trace, and should not log");
        ld.setPattern("%d %X{name} %m%n");
        MDC.put("name", "Scott");
        log.consoleLog("Pattern: " + ld.getPattern());
        log.debug("this goes to the same {} logger", log.getName());

        ILogger joblog2 = loggerFactory.getContextLogger("jobs", "job-2");
        // joblog2.debug("{} pattern = {}", joblog2.getName(), joblog2.getPattern());
        // joblog2.setPattern("%date %level %class{0}.%method\\(\\): %msg%n");
        // joblog2.debug("{} pattern = {}", joblog2.getName(), joblog2.getPattern());
        joblog2.debug("this goes to the {} logger", joblog2.getName());

        log.debug("this goes to the {} logger and it is closing", log.getName());

        joblog2.debug("this goes to the {} logger and it is closing", log.getName());
        loggerFactory.closeLogger(joblog2.getName());
        loggerFactory.closeLogger(log.getName());

        ILogger log2 = loggerFactory.getLogger(this.getClass());
        log2.debug("where does this go?");
        ILogger log3 = loggerFactory.getLogger(this.getClass().getName() + ".Foo");
        log3.debug("and this one?");
        log3.setWarnLevel();
        log2.info("log2 info should show here");
        log3.info("should not log info");
        log3.warn("should log warn but before this line there should not be an info line?");
        log3.setTraceLevel();
        log3.trace("Should log this as trace");
        log2.debug("this is log2 with debug");

        ILogger log4 = loggerFactory.getLogger(this.getClass().getName() + ".Foo.Bar");
        log4.debug("log4 is now debugging");

        List<ILogger> list = loggerFactory.getContextLoggersWithPrefix("job-");
        for (ILogger logger : list)
        {
            loggerFactory.getLogger().info(logger.getName());
        }

        ILogger rootLogger = loggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.warn("First try?");
        // rootLogger.setPattern("%m%n");
        rootLogger.warn("Are we done?");

        log4.consoleLog("This is the end");
    }




    @Reference()
    public void setLoggerFactory(ILoggerFactory loggerFactory)
    {
        this.loggerFactory = loggerFactory;
    }

}
