package io.github.scottbrand.common.provider.logging;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.util.FileSize;
import io.github.scottbrand.common.Create;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.logging.ILoggerFactory;
import io.github.scottbrand.common.logging.LogDelegate;



/**
 * Core implementation of the ILoggerFactory. This implementation supports the
 * idea of having different loggers that log to different files for specific
 * context concerns. Context specific logging configuration is done via
 * properties and by using the given <code>context</code> name during the call
 * to the {@link #createContextLogger(String, String)} method.
 * 
 * 
 * This class is intended to have itself bootstrapped by configuration via the
 * ConfigAdmin using the PID (io.github.scottbrand.common.logging.factory).
 * 
 * Parameters specified via the {@link #activate(Map)} method follow a specific
 * format that allows a certain level of customizability when defining loggers.
 * All parameters should be prefixed by their <code>context</code> name. The
 * following properties are available where "a" denotes the "context" name:
 *
 * <pre>
 * a.pattern=                               // what logging format to use. Default: "%date %level %msg%n"
 * a.filePath=                              // the path of where the logger will be put on the file system.
 *                                          // the filePath can be a relative or absolute path. 
 *                                          // If not specified, the log file will be placed in the current directory. 
 * a.fileName=                              // the name of  the log file with "filePath" prepended and "fileExtension" appended. 
 *                                          // if "fileName" is not provided, then fileName = filePath / loggerName /  fileExtension 
 * a.fileExtension=                         // what extension to be added to the log file that is created. Default: .log 
 * a.fileCompression={.gz,.zip}             // whether to compress rolled logs or not. Default: ".log" 
 * a.fileMaxSize={XKB,XMB}                  // size of  * logfile before a new file is rolled. Default: 500KB
 * a.level={TRACE|DEBUG|INFO|WARN|ERROR}    // when not provided, defaults to DEBUG. 
 * a.additive={true|false}                  // when not provided, defaults to false.
 * 
 * </pre>
 * 
 * 
 * @author Scott Brand
 *
 */
@Component(name = LogFactory.PID,configurationPolicy=ConfigurationPolicy.OPTIONAL)
public final class LogFactory implements ILoggerFactory
{
	public static final String PID = "io.github.scottbrand.common.logging.factory";
	
    private Map<String, ILogger> cachedLoggers         = new ConcurrentHashMap<String, ILogger>();
    private Map<String, Object>  propertyMap           = Create.map(String.class, Object.class);

    public static final String   PATTERN               = ".pattern";
    public static final String   PATTERN_DEFAULT       = "%date [%t] %level %msg%n";

    public static final String   FILE_PATH             = ".filepath";
    public static final String   FILE_PATH_DEFAULT     = "logs/";

    public static final String   FILE_NAME             = ".filename";

    public static final String   FILE_EXT              = ".fileextension";
    public static final String   FILE_EXT_DEFAULT      = ".log";

    public static final String   FILE_COMPRESS         = ".filecompression";
    public static final String   FILE_COMPRESS_DEFAULT = ".log";

    public static final String   FILE_MAX_SIZE         = ".filemaxsize";
    public static final String   FILE_MAX_SIZE_DEFAULT = "2 mb";

    public static final String   LEVEL                 = ".level";
    public static final String   LEVEL_DEFAULT         = "DEBUG";

    public static final String   ADDITIVE              = ".additive";
    public static final String   ADDITIVE_DEFAULT      = "false";

    public static final String   LOGGER_FILE           = ".LF";
    public static final String   FILE_PATTERN          = ".FP";

    public static final String   DEFAULT_LOGGER       = "io.github";

    public static final String   DEFAULT_LOG_FILE      = ILoggerFactory.DEFAULT_LOGGER_NAME;

    /**
     * Called when component is activated. If ConfigAdmin has properties to
     * supply via PID, they will come in the propertyMap.
     * 
     * @param propertyMap
     *            externally configured properties to be used to create loggers.
     */
    @Activate
    public void activate(Map<String, Object> propertyMap)
    {
            //
            // incoming propertyMap is a read-only dictionary, so copy its values
            // to our local map
            //
        try
        {
            this.propertyMap.putAll(propertyMap); 
            createDefaultLogger();
            boolean has_com_github = false;
            for (String k : propertyMap.keySet())
            {
                if (k.startsWith(DEFAULT_LOGGER))
                {
                	has_com_github = true;
                    break;
                }
            }
            if (has_com_github == false)
            {
                this.propertyMap.put(DEFAULT_LOGGER + FILE_NAME,DEFAULT_LOG_FILE);
                this.propertyMap.put(DEFAULT_LOGGER + ADDITIVE,ADDITIVE_DEFAULT);
            }
            createContextLogger(DEFAULT_LOGGER,DEFAULT_LOGGER);
            
            
            Logger rootLogger = (Logger)LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
            rootLogger.setLevel(Level.WARN);
            ILogger rLogger = getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
            if (rLogger != null)
                rLogger.setPattern("%date [%t] %msg%n");
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        
    }
    
    
    
    
    @Modified
    public void update(Map<String, Object> propertyMap)
    {
        
    }





    /**
     * Configuration Admin property changes have been saved.
     * 
     * @param propertyMap
     */
    @Modified
    public void udpate(Map<String, Object> propertyMap)
    {
        this.propertyMap = propertyMap;
        // createDefaultLogger();
    }





    @Override
    public ILogger getLogger()
    {
        return getILogger(ILoggerFactory.DEFAULT_LOGGER_NAME);
    }





    @Override
    public ILogger getLogger(String name)
    {
        return getILogger(name);
    }





    @Override
    public ILogger getLogger(Class<?> clazz)
    {
        return getILogger(clazz != null ? clazz.getName() : null);
    }





    private ILogger getILogger(String name)
    {
        if (Strings.isNullOrEmpty(name))
            name = ILoggerFactory.DEFAULT_LOGGER_NAME;

        if (cachedLoggers.containsKey(name))
            return cachedLoggers.get(name);
        ILogger logger = new LogDelegate(name);
        cachedLoggers.put(name, logger);
        return logger;
    }





    @Override
    public void closeLogger(String name)
    {
        if (Strings.isNullOrEmpty(name))
            return;

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = lc.exists(name);
        if (logger == null)
            return;
        logger.detachAndStopAllAppenders();
    }





    @Override
    public void closeLogger(Class<?> clazz)
    {
        if (clazz != null)
            closeLogger(clazz.getName());

    }





    @Override
    public ILogger getContextLogger(String context, String name)
    {
        if (Strings.isNullOrEmpty(name))
            return getLogger();

        if (cachedLoggers.containsKey(name))
            return cachedLoggers.get(name);
        return createContextLogger(context, name);
    }





    private void createDefaultLogger()
    {
        if (propertyMap.containsKey(ILoggerFactory.DEFAULT_LOGGER_NAME + ADDITIVE) == false)
            propertyMap.put(ILoggerFactory.DEFAULT_LOGGER_NAME + ADDITIVE,ADDITIVE_DEFAULT);
        createContextLogger(ILoggerFactory.DEFAULT_LOGGER_NAME, ILoggerFactory.DEFAULT_LOGGER_NAME);
    }









    private ILogger createContextLogger(String context, String loggerName)
    {
        ILogger newLogger = null;

        try
        {
            Map<String, String> propertyMap = getLoggerPropertyMap(context, loggerName);

            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            RollingFileAppender<ILoggingEvent> rollingAppender = new RollingFileAppender<ILoggingEvent>();

            PatternLayoutEncoder ple = new PatternLayoutEncoder();
            ple.setPattern(propertyMap.get(context + PATTERN));
            ple.setContext(lc);
            ple.start();

            rollingAppender.setFile(propertyMap.get(context + LOGGER_FILE));
            rollingAppender.setEncoder(ple);
            rollingAppender.setContext(lc);

            FixedWindowRollingPolicy fwrp = new FixedWindowRollingPolicy();
            fwrp.setContext(lc);
            fwrp.setMinIndex(1);
            fwrp.setMaxIndex(20);
            fwrp.setFileNamePattern(propertyMap.get(context + FILE_PATTERN));
            fwrp.setParent(rollingAppender);
            fwrp.start();

            rollingAppender.setRollingPolicy(fwrp);

            SizeBasedTriggeringPolicy<ILoggingEvent> stp = new SizeBasedTriggeringPolicy<ILoggingEvent>();
            stp.setMaxFileSize(FileSize.valueOf(propertyMap.get(context + FILE_MAX_SIZE)));
            stp.start();
            rollingAppender.setTriggeringPolicy(stp);

            rollingAppender.start();

            Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
            logger.addAppender(rollingAppender);

            logger.setLevel(Level.toLevel(propertyMap.get(context + LEVEL)));
            logger.setAdditive("true".equals(propertyMap.get(context + ADDITIVE)));

            newLogger = new LogDelegate(logger);
        }
        catch (Throwable t)
        {
            if (ILoggerFactory.DEFAULT_LOGGER_NAME.equals(loggerName) == false)
                getLogger().error("Error creating context logger for context: " + context + " and name: " + loggerName, t);
            newLogger = cachedLoggers.get(ILoggerFactory.DEFAULT_LOGGER_NAME);
        }
        if (newLogger == null)
            newLogger = new LogDelegate((Logger) LoggerFactory.getLogger(ILoggerFactory.DEFAULT_LOGGER_NAME));
        
        cachedLoggers.put(loggerName, newLogger);

        return newLogger;
    }





    private Map<String, String> getLoggerPropertyMap(String context, String loggerName)
    {
        Map<String, String> newMap = Create.map(String.class, String.class);

        //
        // first build our new map with values from the original map
        // but lowercase all key values. This way we can match properties
        // without worrying about case-sensitivity.
        //

        for (String k : propertyMap.keySet())
        {
            newMap.put(k.toLowerCase(), propertyMap.get(k).toString());
        }
        context = context.toLowerCase();

        String value;
        String key;

        key = context + PATTERN;
        value = newMap.get(key);
        newMap.put(key, Strings.isNullOrEmpty(value) ? PATTERN_DEFAULT : value);

        key = context + FILE_PATH;
        value = newMap.get(key);
        if (Strings.isNullOrEmpty(value))
        {
            newMap.put(key, FILE_PATH_DEFAULT);
        }
        else
        {
            newMap.put(key, value.endsWith("/") ? value : value + "/");
        }

        key = context + FILE_NAME;
        value = newMap.get(key);
        newMap.put(key, Strings.isNullOrEmpty(value) ? loggerName : value);

        key = context + FILE_EXT;
        value = newMap.get(key);
        if (Strings.isNullOrEmpty(value))
        {
            newMap.put(key, FILE_EXT_DEFAULT);
        }
        else
        {
            newMap.put(key, value.charAt(0) != '.' ? "." + value : value);
        }

        newMap.put(key, Strings.isNullOrEmpty(value) ? FILE_EXT_DEFAULT : value);

        key = context + FILE_COMPRESS;
        value = newMap.get(key);
        if (Strings.isNullOrEmpty(value))
        {
            newMap.put(key, FILE_COMPRESS_DEFAULT);
        }
        else
        {
            newMap.put(key, value.charAt(0) != '.' ? "." + value : value);
        }
        
        key = context + FILE_MAX_SIZE;
        value = newMap.get(key);
        newMap.put(key, Strings.isNullOrEmpty(value) ? FILE_MAX_SIZE_DEFAULT : value);

        
        key = context + LEVEL;
        value = newMap.get(key);
        if (Strings.isNullOrEmpty(value))
        {
            newMap.put(key, LEVEL_DEFAULT);
        }
        else
        {
            newMap.put(key, "TRACE;DEBUG;INFO;WARN;ERROR".contains(value.toUpperCase()) ? value.toUpperCase() : LEVEL_DEFAULT);
        }

        key = context + ADDITIVE;
        value = newMap.get(key);
        if (Strings.isNullOrEmpty(value))
        {
            newMap.put(key, ADDITIVE_DEFAULT);
        }
        else
        {
            newMap.put(key, "TRUE;FALSE".contains(value.toUpperCase()) ? value.toLowerCase() : ADDITIVE_DEFAULT);
        }

        newMap.put(context + LOGGER_FILE, newMap.get(context + FILE_PATH) + newMap.get(context + FILE_NAME) + newMap.get(context + FILE_EXT));
        newMap.put(context + FILE_PATTERN, newMap.get(context + LOGGER_FILE) + "%i" + newMap.get(context + FILE_COMPRESS));

        return newMap;
    }
    
    
    
    
    
    @Override
    public List<ILogger> getContextLoggersWithPrefix(String loggerNamePrefix)
    {
        List<ILogger> list = new ArrayList<ILogger>();

        Enumeration<String> iter = ((ConcurrentHashMap<String, ILogger>) cachedLoggers).keys();
        while (iter.hasMoreElements())
        {
            String key = iter.nextElement();
            if (key.startsWith(loggerNamePrefix))
            {
                list.add(cachedLoggers.get(key));
            }
        }

        return list;
    }




    @Override
    public ILogger getConsoleLogger()
    {
        return getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

}
