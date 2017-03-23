package io.github.scottbrand.common.logging;



/**
 * Standard Interface for all logging behavior in GPD.
 * An implementation of this interface should be retrieved by
 * the ILoggerFactory.
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public interface ILogger
{
	String getName();

	
	boolean isTraceEnabled();
	boolean isDebugEnabled();
	boolean isInfoEnabled();
	boolean isWarnEnabled();	
	boolean isErrorEnabled();

    void trace(String msg);
    void trace(String msg, Object arg1);
    void trace(String msg, Object arg1, Object arg2);
    void trace(String msg, Object[] args);
    void trace(String msg, Throwable t);

    void debug(Object msg);
	void debug(String msg);
	void debug(String msg, Object arg1);
	void debug(String msg, Object arg1, Object arg2);
	void debug(String msg, Object[] args);
	void debug(String msg, Throwable t);

	void info(String msg);
    void info(String msg, Object arg1);
    void info(String msg, Object arg1, Object arg2);
    void info(String msg, Object[] args);
    void info(String msg, Throwable t);
	    
    void warn(String msg);
    void warn(String msg, Object arg1);
    void warn(String msg, Object arg1, Object arg2);
    void warn(String msg, Object[] args);
    void warn(String msg, Throwable t);

	void error(String msg);
	void error(String msg, Object arg1);
	void error(String msg, Object arg1, Object arg2);
	void error(String msg, Object[] args);
	void error(String msg, Throwable t);
	
	void setTraceLevel();	
	void setDebugLevel();	
	void setInfoLevel();
	void setWarnLevel();	
	void setErrorLevel();
	
	String getLevel();
	void setLevel(String level);
	
	String getPattern();
	void setPattern(String pattern);
	
	void consoleLog(String msg);
	void consoleLog(String msg, Object... objects);
}
