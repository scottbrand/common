package test.io.github.scottbrand.common.ldap;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.BooleanResult;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.TypedResult;
import io.github.scottbrand.common.ldap.ILDAPReader;
import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.security.IAuthenticator;

@Component(immediate=true)
public class LDAPTest
{
	ILDAPReader     reader;
	IAuthenticator  auth;
	ILogger         log;
	
	
	@Activate
	public void activate(ComponentContext ctx)
	{
	    log = ServiceLocator.getLogger();
		doTest();
		System.exit(0);
	}
	
	
	
	
	
	private void doTest()
	{
		if (reader != null)
		{
			TypedResult<Map<String,List<String>>> users = reader.findUsers("brand", "cn,sAMAccountName,mail");   //,distinguishedName");
			log.info("findUsers returned: {}",users.getResult());
		}
		else
			log.info("Reader was null");
		
		if (auth != null)
		{
		    BooleanResult br; 
			if (( br = auth.authenticate("!baq15sb", "*")).getThrowable() != null)
				log.error("bad login",br.getThrowable());
			else 
				log.info("Good login");
			if (( br = auth.authenticate("albert", "enstein")).getResult() == false)
				log.error("bad login");//,auth.getThrowable());
			else 
				log.info("Good login");			
		}
		else
			log.info("auth was null");

	}
	
	
	
	
	
	@Reference 
	public void setLDAPReader(ILDAPReader reader)
	{
		this.reader = reader;
	}
	
	
	
	@Reference 
	public void setAuthenticator(IAuthenticator auth)
	{
		this.auth = auth;
	}
	

}
