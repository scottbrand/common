package test.io.github.scottbrand.common.db.db2;

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



@Component(immediate = true, name = DB2SQLDS.PID, property = { "ds.name=ds/db2", "service.ranking:Integer=100" }, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class DB2SQLDS extends BaseDataSource implements javax.sql.DataSource, IDataSource
{

    public static final String PID = "com.experian.gpd.db.db2";
    




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
        return "10% Sample on " + this.productName + ";Version=" + this.productVersion;
    }
}