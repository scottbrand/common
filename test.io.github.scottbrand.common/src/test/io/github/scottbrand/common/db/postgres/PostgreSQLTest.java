package test.io.github.scottbrand.common.db.postgres;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.sql.DataSource;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.ServiceLocator;



@Component(immediate=true)
public class PostgreSQLTest
{
	
	DataSource postgreSQLDS = null;
	
	
	@Activate
	public void activate(BundleContext ctx)
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
			rs = s.executeQuery("SELECT * FROM USERS");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cc = rsmd.getColumnCount();
			while (rs.next())
			{	for (int i = 1; i <= cc; i++)
					ServiceLocator.getLogger().debug("column: {} = {}",i,rs.getString(i));
			}
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
