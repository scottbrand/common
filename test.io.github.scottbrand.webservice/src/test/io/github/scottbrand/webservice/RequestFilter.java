package test.io.github.scottbrand.webservice;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.logging.ILoggerFactory;
import test.io.github.scottbrand.webservice.RequestFilter.Config;



@Provider
@Priority(value = 1)
@Component(configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = Config.class)
public class RequestFilter implements ContainerRequestFilter
{
	
	private Pattern p = null;
	private ILogger  log = null;

	@ObjectClassDefinition
	@interface Config
	{
		@AttributeDefinition(name = "pattern", required = true, description = "RegExp of uri paths to allow without a token")
		String pattern() default ".*(/signup|/login|/logon|/logout)";
	}


	@Activate
	private void activate(Config config)
	{
		p = Pattern.compile(config.pattern());
	}


	@Override
	public void filter(ContainerRequestContext rc) throws IOException
	{
		String path = rc.getUriInfo().getPath();
		System.out.println("Got path of: " + path);
		
		boolean noTokenOkay = false;
		if (p.matcher(path).matches())
		{
			log.debug("matched uri of: {}, okay to proceed without token");
			noTokenOkay = true;
		}
		
		String authToken = rc.getHeaderString("Authorization");
		if (authToken == null && noTokenOkay == false)
			rc.abortWith(Response.status(Status.UNAUTHORIZED).build());
		
		log.debug("authToken is: {}, need to validate token",authToken);
		
//		try( InputStream inputStream = rc.getEntityStream())
//		{
//			byte[] b = new byte[16384];
//			int br = inputStream.read(b);
//			System.out.println("bytes read: " + br);
//			if (br > 0)
//				System.out.println("value: " + new String(Arrays.copyOfRange(b, 0, br-1)).toString());
//		}
//		if ("session".contains(path))
//			return;
//		if (path.contains("session"))
//			return;
//		String authToken = rc.getHeaderString("Authorization");
//		if (authToken == null)
//			rc.abortWith(Response.status(Status.UNAUTHORIZED).build());
		
	}

	
	
	@Reference
	private void setLoggerFactory(ILoggerFactory loggerFactory)
	{
		log = loggerFactory.getLogger();
	}
}
