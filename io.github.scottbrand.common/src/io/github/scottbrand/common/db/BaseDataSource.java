package io.github.scottbrand.common.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.sql.DataSource;

import io.github.scottbrand.common.Patterns;
import io.github.scottbrand.common.PropertyReader;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;


/**
 * This is a Base class that implements <code>javax.sql.DataSource</code>
 * via a faster connection pool library provided by HikariDataSource.
 * 
 * 
 * @since 1.0
 * @author Scott Brand
 *
 */
public class BaseDataSource implements javax.sql.DataSource
{
	
    /**
     * When reading in the external configuration properties values used
     * to configure this database, properties that are to be considered for
     * configuring the datasource should be prefixed with this character.
     * Other properties will be ignored in terms of using them for configuring the datasource.
     */
    public static final String PROPERTY_PREFIX = "_";
    
    
    /**
     * Designated key value in the Map to be used to locate the SQL query string
     * to be executed.
     */
	public static final String QUERY           = "Q";

	
	
	/**
	 * we cache java.sql.Statements by the ResultSet.hash(), so that when the ResultSet
	 * object is released via {@link #releaseQuery(ResultSet)} we can get the Statement
	 * object as it was when first created.  We use the Statement object to get the 
	 * Connection so we can return it to the Connection Pool.
	 */
	protected Map<Integer,Statement> statementMap = new ConcurrentHashMap<Integer,Statement>();
	
	
	protected DataSource internalSource;

	protected PropertyReader propertyReader = new PropertyReader();
	
	protected String productName;
	
	protected String productVersion;
	
	
	
	protected void setInternalSoruce(DataSource ds)
	{
		this.internalSource = ds;
	}
	

	
	
	protected Properties getProperties(Map<String,Object> map)
	{
		Properties p = new Properties();
		if (map != null)
			for (String k : map.keySet())
				if (k.startsWith(PROPERTY_PREFIX))
				    p.setProperty(k.substring(1), propertyReader.getProperty(map.get(k).toString()));
		return p;
	}

	
	

	
	
	
	
	/**
	 * Executes a SQL SELECT statement along with potential variable substitution
	 * and return the resultant ResultSet.
	 * The actual query text must be stored in the parameters map in the QUERY key.
	 * 
	 * @param parameters
	 * @return
	 */
	public ResultSet executeQuery(Map<String,String> parameters) throws Throwable
	{
		return executeQuery(getConnection(),parameters);
	}

	

	
	
	/**
	 * Executes a SQL SELECT statement along with potential variable substitution
	 * and return the resultant ResultSet.
	 * 
	 * @param parameters
	 * @return
	 */
	public ResultSet executeQuery(Connection c, Map<String,String> parameters)
	throws Throwable
	{
		if (c == null || c.isClosed())
			return null;
		
		Statement  s = c.createStatement();
		try
		{
		    String     q = Patterns.replace(parameters.get(QUERY),true,parameters);
					   
		    ResultSet rs = s.executeQuery(q);
			if (rs != null)
				statementMap.put(rs.hashCode(),s);
			else
				s.getConnection().close();
			return rs;
		}
		catch (Throwable t)
		{
			throw t;
		}
	}
	

	/**
	 * This method should be called by every invocation of {@link #executeQuery(Connection, Map)}
	 * in order to close down the ResultSet object and return the Connection to the ConnectionPool.
	 * 
	 * @param rs
	 */
	public void releaseQuery(ResultSet rs)
	{
		try
		{
			if (rs == null)
				return;
			Statement s = statementMap.remove(rs.hashCode());
			
			if (s != null)
				s.getConnection().close();
			rs = null;
		}
		catch (Throwable t)
		{
		    ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
		}
	}



	@Override
	public PrintWriter getLogWriter() throws SQLException
	{
		return internalSource.getLogWriter();
	}



	@Override
	public int getLoginTimeout() throws SQLException
	{
		return internalSource.getLoginTimeout();
	}



	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException
	{
		return internalSource.getParentLogger();
	}



	@Override
	public void setLogWriter(PrintWriter arg0) throws SQLException
	{
		internalSource.setLogWriter(arg0);
	}



	@Override
	public void setLoginTimeout(int arg0) throws SQLException
	{
		internalSource.setLoginTimeout(arg0);
	}



	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException
	{
		return internalSource.isWrapperFor(arg0);
	}



	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException
	{
		return internalSource.unwrap(arg0);
	}




	@Override
	public Connection getConnection() throws SQLException
	{
		return internalSource.getConnection();
	}



	@Override
	public Connection getConnection(String userID, String password) throws SQLException
	{
		return internalSource.getConnection(userID, password);
	}
	
	
	protected void setMetaData()
	{
	    try
	    {
    	    Connection c = getConnection();
    	    if (c != null)
    	    {
    	        DatabaseMetaData meta = c.getMetaData();
    	        this.productName = meta.getDatabaseProductName();
    	        this.productVersion = meta.getDatabaseProductVersion();
    	        c.close();
    	    }
	    }
	    catch (Throwable t)
	    {
	        ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
	    }
	}

}
