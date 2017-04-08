package test.io.github.scottbrand.common.db.postgres;

import java.util.Map;

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
   
    public static final String PID = "com.experian.gpd.db.postgres";
	
	@Activate
	protected void activate(Map<String, Object> map) 
	{
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
        return "100% Sample on " + this.productName + ";Version=" + this.productVersion;
    }
}