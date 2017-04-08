package test.io.github.scottbrand.common.db.db2;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.CollectionUtil;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.db.IDataSource;
import io.github.scottbrand.common.logging.ILogger;



@Component(immediate=true)
public class DB2SQLTest 
{
	ILogger    log;
	DataSource db2SQLDS = null;
	
	
	@Activate
	public void activate()
	{
	    log = ServiceLocator.getLogger();
		testDB2();

		new Runnable()
		{

            @Override
            public void run()
            {
                try
                {
                    log.debug("Sleeping for 8 seconds before checking for IDataSources");
                    Thread.sleep(8000L);
                }
                catch (Throwable t)
                {
                    
                }
                listIDataSources();
            }
		    
		}.run();
		
		System.exit(0);
	}
	
	
	
	private void listIDataSources()
	{
        List<IDataSource> dsList = ServiceLocator.getInstance().getServices(IDataSource.class);
        if (CollectionUtil.isPopulatedList(dsList))
            for (IDataSource ds : dsList)
                log.debug("IDataSource: {}",ds.getName());
        else
            log.debug("NO IDataSource objects");
        
        IDataSource ds2 = findIDataSource("10% Sample on DB2/AIX64;Version=SQL09075");
        log.debug("ds2 = {}",ds2);
	    
	}
	
	
	private IDataSource findIDataSource(String name)
	{
	    if (Strings.isNullOrEmpty(name))
	        return null;
	    
        List<IDataSource> dsList = ServiceLocator.getInstance().getServices(IDataSource.class);
        if (CollectionUtil.isPopulatedList(dsList))
            for (IDataSource ds : dsList)
                if (name.equals(ds.getName()))
                    return ds;
        return null;
                   
	}


	
	
	private void testDB2()
	{
		Connection c = null;
		try
		{
			c = db2SQLDS.getConnection();
			DatabaseMetaData md = c.getMetaData();
			log.debug("Product: {}; Version: {}",md.getDatabaseProductName(),md.getDatabaseProductVersion());
			log.debug("Database Version major: {}; minor: {}",md.getDatabaseMajorVersion(),md.getDatabaseMinorVersion());
			log.debug("Driver Name: {}; Version: {}",md.getDriverName(),md.getDriverVersion());
			log.debug("Database Driver Version major: {}; minor: {}",md.getDriverMajorVersion(),md.getDriverMinorVersion());
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT CURRENT_TIMESTAMP FROM SYSIBM.SYSDUMMY1"); 
			while (rs.next())
			    log.debug("From db2 Result Set value is: {}",rs.getString(1));
			rs.close();
			s.close();
			
			s = c.createStatement();
			rs = s.executeQuery("SELECT * FROM SYSCAT.SCHEMATA WITH UR");
			ResultSetMetaData rmd = rs.getMetaData();
			while (rs.next())
			{
				for (int i = 1; i <= rmd.getColumnCount(); i++)
				{
				    log.debug("column: {}; value: {}",rmd.getColumnName(i),rs.getObject(i));
				}
			}
			rs.close();
			s.close();			
			c.close();
		}
		catch (Throwable e)
		{
		    log.error(Strings.EXCEPTION,e);
		}
	}
	
	
	
	   @Reference(target = "(ds.name=ds/db2)")
	    public void setDataSource(DataSource ds)
	    {
	        this.db2SQLDS = ds;
	    }
	
}
