package test.io.github.scottbrand.common.db.postgres;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.ServiceLocator;



@Component(immediate=true)
public class PostgreSQLTest
{
	
	DataSource postgreSQLDS = null;
	
	
	@Activate
	public void activate()
	{
		testPostGres();
		//System.exit(0);
	}
	
	
	@Reference(target = "(ds.name=ds/postgres)")
	public void setDataSource(DataSource ds)
	{
	    ServiceLocator.getLogger().debug("Setting postgres datasource to: " + ds);
		this.postgreSQLDS = ds;
	}

	
	
	private void testPostGres()
	{
		Connection c = null;
		try
		{
			c = postgreSQLDS.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT CURRENT_TIMESTAMP WHERE 1 = 1"); // "SELECT CURRENT_TIMESTAMP FROM SYSIBM.SYSDUMMY1");
			while (rs.next())
			    ServiceLocator.getLogger().debug("From postGres Result Set value is: " + rs.getString(1));
			rs.close();
			s.close();
			
			s = c.createStatement();
			rs = s.executeQuery("SELECT * FROM PLATFORM");
			while (rs.next())
			    ServiceLocator.getLogger().debug("ID: " + rs.getInt(1) + " VERSION: "  + rs.getInt(2) + " NAME: " + 
			                      rs.getString(3) +  " CREATED BY: " + rs.getInt(4) + " UPDATED: " + rs.getTimestamp(5));
			rs.close();
			s.close();			
			c.close();
		}
		catch (Throwable e)
		{
		    ServiceLocator.getLogger().error("Whoops: connection has exception",e);
		}

	}
	

	
}
