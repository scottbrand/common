package io.github.scottbrand.common.logging;

import java.util.Iterator;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;



/**
 * The LogDelete is an internal implementation of the standard ILogger interface from the GPD API. This internal class
 * simply wraps an instance of a logback Logger and delegates calls to that logger.
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public class LogDelegate implements ILogger
{
    protected Logger log;





    public LogDelegate()
    {
        log = (Logger) LoggerFactory.getLogger(ILoggerFactory.DEFAULT_LOGGER_NAME);
    }





    public LogDelegate(String name)
    {
        log = (Logger) LoggerFactory.getLogger(name);
    }





    public LogDelegate(Class<?> clazz)
    {
        log = (Logger) LoggerFactory.getLogger(clazz);
    }





    public LogDelegate(Logger log)
    {
        this.log = log;
    }





    @Override
    public String getName()
    {
        return log.getName();
    }





    public void consoleLog(String message)
    {
        org.slf4j.Logger log = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        if (log != null)
            log.warn(message);
    }





    public void consoleLog(String message, Object... objects)
    {
        org.slf4j.Logger log = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        if (log != null)
            log.warn(message, objects);
    }





    @Override
    public boolean isTraceEnabled()
    {
        return log.isTraceEnabled();
    }





    @Override
    public boolean isDebugEnabled()
    {
        return log.isDebugEnabled();
    }





    @Override
    public boolean isInfoEnabled()
    {
        return log.isInfoEnabled();
    }





    @Override
    public boolean isWarnEnabled()
    {
        return log.isWarnEnabled();
    }





    @Override
    public boolean isErrorEnabled()
    {
        return log.isErrorEnabled();
    }





    @Override
    public void debug(Object msg)
    {
        if (msg != null)
            log.debug(msg.toString());
    }





    @Override
    public void debug(String msg)
    {
        log.debug(msg);
    }





    @Override
    public void debug(String msg, Object arg1)
    {
        log.debug(msg, arg1);
    }





    @Override
    public void debug(String msg, Object arg1, Object arg2)
    {
        log.debug(msg, arg1, arg2);
    }





    @Override
    public void debug(String msg, Object[] args)
    {
        log.debug(msg, args);
    }





    @Override
    public void debug(String msg, Throwable t)
    {
        log.debug(msg, t);
    }





    @Override
    public void error(String msg)
    {
        log.error(msg);
    }





    @Override
    public void error(String msg, Object arg1)
    {
        log.error(msg, arg1);
    }





    @Override
    public void error(String msg, Object arg1, Object arg2)
    {
        log.error(msg, arg1, arg2);
    }





    @Override
    public void error(String msg, Object[] args)
    {
        log.error(msg, args);
    }





    @Override
    public void error(String msg, Throwable t)
    {
        log.error(msg, t);
    }





    @Override
    public void info(String msg)
    {
        log.info(msg);
    }





    @Override
    public void info(String msg, Object arg1)
    {
        log.info(msg, arg1);
    }





    @Override
    public void info(String msg, Object arg1, Object arg2)
    {
        log.info(msg, arg1, arg2);
    }





    @Override
    public void info(String msg, Object[] args)
    {
        log.info(msg, args);
    }





    @Override
    public void info(String msg, Throwable t)
    {
        log.info(msg, t);
    }





    @Override
    public void trace(String msg)
    {
        log.trace(msg);
    }





    @Override
    public void trace(String msg, Object arg1)
    {
        log.trace(msg, arg1);
    }





    @Override
    public void trace(String msg, Object arg1, Object arg2)
    {
        log.trace(msg, arg1, arg2);
    }





    @Override
    public void trace(String msg, Object[] args)
    {
        log.trace(msg, args);
    }





    @Override
    public void trace(String msg, Throwable t)
    {
        log.trace(msg, t);
    }





    @Override
    public void warn(String msg)
    {
        log.warn(msg);
    }





    @Override
    public void warn(String msg, Object arg1)
    {
        log.warn(msg, arg1);
    }





    @Override
    public void warn(String msg, Object arg1, Object arg2)
    {
        log.warn(msg, arg1, arg2);
    }





    @Override
    public void warn(String msg, Object[] args)
    {
        log.warn(msg, args);
    }





    @Override
    public void warn(String msg, Throwable t)
    {
        log.warn(msg, t);
    }





    @Override
    public void setTraceLevel()
    {
        log.setLevel(Level.TRACE);
    }





    @Override
    public void setDebugLevel()
    {
        log.setLevel(Level.DEBUG);
    }





    @Override
    public void setInfoLevel()
    {
        log.setLevel(Level.INFO);
    }





    @Override
    public void setWarnLevel()
    {
        log.setLevel(Level.WARN);
    }





    @Override
    public void setErrorLevel()
    {
        log.setLevel(Level.ERROR);
    }





    @Override
    public String getLevel()
    {
        return log.getLevel().levelStr;
    }





    @Override
    public void setLevel(String level)
    {
        Level intLevel = Level.toLevel(level);
        switch (intLevel.toInt())
        {
            case Level.TRACE_INT:
                setTraceLevel();
                break;
            case Level.DEBUG_INT:
                setDebugLevel();
                break;
            case Level.INFO_INT:
                setInfoLevel();
                break;
            case Level.WARN_INT:
                setWarnLevel();
                break;
            case Level.ERROR_INT:
                setErrorLevel();
                break;
        }
    }





    @Override
    public String getPattern()
    {
        Iterator<Appender<ILoggingEvent>> iter = log.iteratorForAppenders();
        while (iter.hasNext())
        {
            RollingFileAppender<ILoggingEvent> rollingAppender = (RollingFileAppender<ILoggingEvent>) iter.next();
            if (rollingAppender != null)
            {
                if (rollingAppender.getEncoder() instanceof PatternLayoutEncoder)
                {
                    PatternLayoutEncoder ple = (PatternLayoutEncoder) rollingAppender.getEncoder();
                    if (ple != null)
                    {
                        return ple.getPattern();
                    }
                }
            }
        }
        return null;
    }





    @Override
    public void setPattern(String pattern)
    {
        Iterator<Appender<ILoggingEvent>> iter = log.iteratorForAppenders();
        while (iter.hasNext())
        {
            OutputStreamAppender<ILoggingEvent> appender = (OutputStreamAppender<ILoggingEvent>) iter.next();
            if (appender != null)
            {
                if (appender.getEncoder() instanceof PatternLayoutEncoder)
                {
                    PatternLayoutEncoder ple = (PatternLayoutEncoder) appender.getEncoder();
                    if (ple != null)
                    {
                        ple.stop();
                        ple.setPattern(pattern);
                        ple.start();
                        break;
                    }
                }
            }
        }
    }
}
