package test.io.github.scottbrand.webservice;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.MultiPart;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.logging.ILoggerFactory;
import io.github.scottbrand.webservice.jaxrs.publisher.IRest;
import io.github.scottbrand.webservice.jwt.ITokenAuthenticator;
import io.github.scottbrand.webservice.jwt.UserData;



@Component // (immediate = true)
@Path("/motd")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageOfDayRS implements IRest
{

	ILogger log = null;
	
	@Activate
	private void activate()
	{
		System.out.println("motd ws has started " + (new Date()));
	}





	@GET
	public Response getMOTD(@Context HttpHeaders hh)
	{
		System.out.println("HttpHeaders: " + hh);
		//return "Hello from the MOTD provider." + (new Date());
		return Response.ok().entity("Hello from the MOTD provider." + (new Date())).build();
	}


	
	@PUT
	@Path("/map")
	public String putMap(Map<String,Object> dataMap)
	{
		System.out.println("dataMap is: " + dataMap);
		return "Got map";
	}



	@GET
	@Path("/m2")
	public String getMOTD2(@Context HttpHeaders hh)
	{
		System.out.println("HttpHeaders: " + hh);
		return "Hello from the MOTD2 provider." + (new Date());
	}





	@POST
	@Path("/file")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public MultiPart putFile(/*@FormDataParam("file") FormDataContentDisposition contentDisposition,*/ @Context HttpHeaders hh) // final FormDataMultiPart multiPart) //, @Context HttpHeaders hh)
	{
		System.out.println("multipart:" + hh);
		return null;
	}

	
	@POST
	@Path("/{id}/stock/{quantity}/{username}")
	public Response post(@PathParam("id") long id, @PathParam("quantity") int quantity,
@PathParam("username") String username, @Context Request request) {
		System.out.println("POST stock");
		System.out.println("id: " + id + ", quantity: " + quantity + ", username: " + username);
		return Response.ok().build();
		
	}
	
	
	
	
	
	@POST
	@Path("/sessions/create")
	public Response sessionCreate(Credentials creds, @Context ContainerRequestContext request) //Map<String,String> creds)
	{
		System.out.println("====> create session with: " + creds);
		System.out.println("====> username is: " + creds.getUsername());
		System.out.println("====> password is: " + creds.getPassword());
		System.out.println("====> userID is: " + creds.getUserID());
		System.out.println("====> loginDate is: " + creds.getLoginDate());
		System.out.println("====> moreData is: " + creds.getMoreData());
		String jwt = "{ \"id_token\" : \"ffdffsfsd.sffsfsf.sfsfsfs\" }";
		
		ITokenAuthenticator ta = ServiceLocator.getInstance().getService(ITokenAuthenticator.class);
		if (ta != null)
		{
			UserData ud = new UserData(creds.getUserID(), creds.getUsername(), creds.getUsername(), "127.0.0.1");
			
		}
		
		//return Response.ok(jwt, MediaType.APPLICATION_JSON).build();
		return Response.status(Status.UNAUTHORIZED).entity("This is bad message").build();
	}
	
	
	@POST
	@Path("/login")
	public Response login(Credentials creds, @Context HttpServletRequest request) //Map<String,String> creds)
	{
		System.out.println("====> create session with: " + creds);
		System.out.println("====> username is: " + creds.getUsername());
		System.out.println("====> password is: " + creds.getPassword());
		System.out.println("====> userID is: " + creds.getUserID());
		System.out.println("====> loginDate is: " + creds.getLoginDate());
		System.out.println("====> moreData is: " + creds.getMoreData());
		String jwt = "{ \"id_token\" : \"ffdffsfsd.sffsfsf.sfsfsfs\" }";
		
		ITokenAuthenticator ta = ServiceLocator.getInstance().getService(ITokenAuthenticator.class);
		if (ta != null)
		{
			UserData ud = new UserData(creds.getUserID(), creds.getUsername(), creds.getUsername(), request.getRemoteAddr());
			String token = ta.createToken(request, ud);
			log.debug("token is: {}",token);
			//String jwt = "{ \"id_token\" : \"ffdffsfsd.sffsfsf.sfsfsfs\" }";
			return Response.accepted(new JWTToken(token)).build();
		}
		
		//return Response.ok(jwt, MediaType.APPLICATION_JSON).build();
		return Response.status(Status.UNAUTHORIZED).entity("This is bad message").build();
	}
	
	
//	@POST
//	@Path("/sessions/create")
//	public Response sessionCreate(Map<String,String> creds)
//	{
//		System.out.println("====> create session with: " + creds);
//		String jwt = "{ \"id_token\" : \"ffdffsfsd.sffsfsf.sfsfsfs\" }";
//		return Response.ok(jwt, MediaType.APPLICATION_JSON).build();
//	}
	
	
//	@POST
//	@Path("/sessions/create")
//	public Response sessionCreate(String creds)
//	{
//		System.out.println("====> create session with: " + creds);
//		String jwt = "{ \"id_token\" : \"ffdffsfsd.sffsfsf.sfsfsfs\" }";
//		return Response.ok(jwt, MediaType.APPLICATION_JSON).build();
//	}

//	@POST
//	@Path("/Upload")
//	@Produces(MediaType.TEXT_HTML)
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	public String uploadFile(@FormDataParam("file") InputStream inputStream) //, @FormDataParam("file") FormDataContentDisposition contentDisposition)
//	{
//		System.out.println("multipart:");
//		return "File Done";
//	}
//
//
//
//	@POST
//	@Path("/Upload2")
//	@Produces(MediaType.TEXT_HTML)
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	public void uploadFile2(@FormDataParam("file") InputStream inputStream, @FormDataParam("file") FormDataContentDisposition contentDisposition)
//	{
//		System.out.println("multipart:");
//		//return "File Done";
//	}

	
	@Reference
	private void setILoggerFactory(ILoggerFactory loggerFactory)
	{
		log = loggerFactory.getLogger();
	}
}
