package io.github.scottbrand.common.provider.mail;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.mail.IMailConnector;
import io.github.scottbrand.common.provider.mail.MailConnector.Config;

/**
 * Basic implementation of the MailConnector interface.
 * 
 * This class can be injected as the impl of the MailConnector interface. 
 * @author Scott Brand
 *
 */

@Component(name=MailConnector.PID,configurationPolicy=ConfigurationPolicy.REQUIRE)
@Designate(ocd = Config.class)
public class MailConnector implements IMailConnector
{

    public static final String PID = "io.github.scottbrand.common.provider.mail";
    
	
    private String host;
    private String port;

    
    @ObjectClassDefinition
    @interface Config
    {
        @AttributeDefinition(name="Mail Host", required=true, description="Host Address or IP of mail server")
        String mailhost() default Strings.EMPTY;
        
        @AttributeDefinition(name="Mail Port", required=false, description="Port of Listening Mail Service.  Default is 25")
        int port() default 25;
    }


    @Activate
    public void activate(Config config)
    {
        host = config.mailhost();
        port = String.valueOf(config.port());
    }



    @Override
    public String getHost()
    {
        return host;
    }





    @Override
    public String getPort()
    {
        return port;
    }





    public void setHost(String host)
    {
        this.host = host;
    }





    public void setPort(String port)
    {
        this.port = port;
    }
    
    
    
    @Override
    public boolean getDebug()
    {
    	return false;
    }

}
