package test.io.github.scottbrand.common.db.postgres;

import java.util.Arrays;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.scottbrand.common.db.BaseDataSource;
import io.github.scottbrand.common.db.IDataSource;
import io.github.scottbrand.common.security.IPasswordEncryptor;

@Component(name=PostgresSQLDS.PID,
property={"ds.name=ds/postgres","service.ranking:Integer=100"},configurationPolicy=ConfigurationPolicy.REQUIRE)
public class PostgresSQLDS extends BaseDataSource implements javax.sql.DataSource, IDataSource
{
   
    public static final String PID = "io.github.scottbrand.common.db.postgres";
	
	@Activate
	protected void activate(BundleContext ctx, Map<String, Object> map) 
	{
		Arrays.stream(ctx.getBundles()).forEach(b -> System.out.println(b.getSymbolicName() + "\t" + b.getState()));
		
		
		HikariConfig config = new HikariConfig(getProperties(map));
		HikariDataSource ds = new HikariDataSource(config);
		this.setInternalSoruce(ds);
        setMetaData();
	}
	
	
	
	
    @Reference(target = "(" + IPasswordEncryptor.HEX_ENCODING + ")")
	public void setIPasswordEncrytor(IPasswordEncryptor encryptor)
	{
	    this.propertyReader.setPasswordEncryptor(encryptor);
	}
	
	
	
    @Override
    public String getProductName()
    {
        return this.productName;
    }





    @Override
    public String getProductVersion()
    {
        return this.productVersion;
    }
    
    
    @Override
    public String getName()
    {
        return "Sample on " + this.productName + ";Version=" + this.productVersion;
    }
}