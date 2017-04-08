package test.io.github.scottbrand.webservice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;



@Component(service = ServletContextHelper.class, property = { HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=hello", HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH + "=/" })
public class HelloContext extends ServletContextHelper
{

	@Override
	public boolean handleSecurity(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		System.out.println("asked to handle security");
		return true;
	}
}
