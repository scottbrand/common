package test.io.github.scottbrand.common.security;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.Hex;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.security.IPasswordEncryptor;



@Component
public class SecurityTest
{
    private ILogger log;
    
    private volatile IPasswordEncryptor encryptor;





    @Activate
    public void activate()
    {
        try
        {
            log = ServiceLocator.getLogger();
         
            encryptLDAP();
            encryptDB2();
            
//            String password = encryptor.encrypt("gbmdPassword");
//            ServiceLocator.getLogger().debug("encrypt = " + password);
//            ServiceLocator.getLogger().debug("decrypt = " + encryptor.decrypt(password));
//            
//            password = encryptor.encrypt("gbmdqaPassword");
//            ServiceLocator.getLogger().debug("encrypt = " + password);
//            ServiceLocator.getLogger().debug("decrypt = " + encryptor.decrypt(password));
            System.exit(0);
        }
        catch (Throwable t)
        {
            log.error(Strings.EXCEPTION,t);
        }
    }



    private void encryptLDAP() throws Throwable
    {
        log.debug("LDAP\n\n");
        log.debug("url=ENC({})",encryptor.encrypt("ldap://167.107.252.30:3268/ ldap://expadroot1.experian.local:3268/"));
        log.debug("principal=ENC({})",encryptor.encrypt("iSmartSvc"));
        log.debug("password=ENC({})",encryptor.encrypt("4Smart$Access"));
        log.debug("scheme=simple");
        log.debug("ou=DC=experian,DC=local");
        log.debug("logonFilter=(&(objectClass=*)(sAMAccountName={0}))");
        log.debug("surNameFilter=(&(objectClass=*)(sn={0}*))");
    }
    
    
    
    
    private void encryptDB2() throws Throwable
    {
        log.debug("DB2\n\n");
        log.debug("serverName=ENC({})",encryptor.encrypt("10.10.188.171"));
        log.debug("portNumber=ENC({})",encryptor.encrypt("50000"));
        log.debug("databaseName=ENC({})",encryptor.encrypt("GVAPDDB1"));
        log.debug("user=ENC({})",encryptor.encrypt("gvapdb"));
        log.debug("password=ENC({})",encryptor.encrypt("sNqT8345"));
    }


    @Reference(target="(" + IPasswordEncryptor.HEX_ENCODING + ")")
    public void setPasswordEncryptor(IPasswordEncryptor encryptor)
    {
        this.encryptor = encryptor;
    }
    
    
    
    public static void main(String[] args)
    {
        ServiceLocator.getLogger().debug("salt       = " + Hex.toHexString("somesalt&pepper!".getBytes()));
        ServiceLocator.getLogger().debug("secret.key = " + Hex.toHexString("GlobalBureauPlat".getBytes()));
    }
}
