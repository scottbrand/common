package io.github.scottbrand.common.db;

import java.sql.Connection;
import java.sql.SQLException;



/**
 * @author Victor Chambe (a03994a)
 *
 *         Interface that tag and provide basic data source information. It should be implemented by all data sources
 *         components irrespective of underline technology so they can be discovered.
 * 
 */
public interface IDataSource
{
    
    /**
     * The default data source type that we are dealing with.
     */
    public static final String DEFAULT_TYPE = "JDBC";





    /**
     * It returns type of Data Source connection. By default it is JDBC
     * 
     * @return String the Type of Data Source
     */
    default String getType()
    {
        return DEFAULT_TYPE;
    }





    /**
     * It returns product name of data source. Typically could be derived from
     * DatabaseMetaData.getDatabaseProductName();
     * 
     * @return String product name
     */
    String getProductName();





    /**
     * It returns product version of data source. Typically could be derived from
     * DatabaseMetaData.getDatabaseMajorVersion();
     * 
     * @return
     */
    String getProductVersion();





    /**
     * Returns a user friendly name that combines the product name and the product version.
     * 
     * @return
     */
    default String getName()
    {
        return getProductName() + ";Version=" + getProductVersion();
    }





    /**
     * Gets a Connection object to this data source.
     * 
     * @return java.sql.Connection
     */
    Connection getConnection() throws SQLException;
}
