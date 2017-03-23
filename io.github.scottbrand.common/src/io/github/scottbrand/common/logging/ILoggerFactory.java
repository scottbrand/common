package io.github.scottbrand.common.logging;

import java.util.List;



/**
 * ILoggerFactory is the standard interface in which all request for an ILogger
 * in GPD should be done. Primarily named loggers should be registered and used
 * as opposed to using the className methods.
 * 
 * Implementation classes should always provide a default logger with the name
 * of "GPD" with a default log level set to the equivalent of {@link
 * ILogger.setDebugLevel()}
 * 
 * @author Scott Brand
 *
 */
public interface ILoggerFactory
{

    static final String DEFAULT_LOGGER_NAME = "application";





    /**
     * Returns the default ILogger instance which has the name "GPD"
     * 
     * @return
     */
    ILogger getLogger();





    /**
     * Returns the ILogger instance that matches the name. If no ILogger with
     * that name is found, then the default ILogger("GPD") is returned.
     * 
     * @param name
     * @return
     */
    ILogger getLogger(String loggerName);





    /**
     * Creates a new stand alone log file. The pattern, file size, base
     * directory, etc is derived by the given
     * <code>context</context> parameters.
     * 
     * @param context
     *            the name of the context to use to create this logger.
     * @param name
     *            of the ILogger. Future references of getLogger(name), will
     *            return this logger.
     * 
     * @return ILogger the new logger or existing Logger if it has already been
     *         created.
     */
    ILogger getContextLogger(String context, String loggerName);





    /**
     * Returns a list of loggers that start with the given prefix.
     * 
     * @param loggerNamePrefix
     * @return
     */
    List<ILogger> getContextLoggersWithPrefix(String loggerNamePrefix);





    /**
     * Returns the ILogger instance that matches the class formed by
     * (clazz.getName()) If no ILogger with that name is found, then the default
     * ILogger("GPD") is returned.
     * 
     * @param clazz
     * @return
     */
    ILogger getLogger(Class<?> clazz);





    /**
     * Closes and detaches all appenders for the logger given by the name. After
     * this call, any further calls to the logger will simply do nothing as it
     * will have no appenders for output.
     * 
     * @param name
     *            of the logger to close.
     */
    void closeLogger(String loggerName);





    /**
     * Closes and detaches all appenders for the logger given by the class.
     * After this call, any further calls to the logger will simply do nothing
     * as it will have no appenders for output.
     * 
     * @param name
     *            of the logger to close.
     */
    void closeLogger(Class<?> clazz);
    
    
    
    ILogger getConsoleLogger();

}
