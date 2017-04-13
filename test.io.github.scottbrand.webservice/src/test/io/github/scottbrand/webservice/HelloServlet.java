package test.io.github.scottbrand.webservice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(service = Servlet.class, property = { HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=hello)",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN + "=/hello" })
public class HelloServlet extends HttpServlet
{
	private static final long	serialVersionUID	= 9053297261753327473L;

	private String				name;





	public HelloServlet()
	{
		System.out.println("Created");
	}





	public HelloServlet(String name)
	{
		this.name = name;
	}





	@Activate
	private void activate()
	{
		System.out.println("Here I am");
	}





	@Override
	public void init(ServletConfig config) throws ServletException
	{
		doLog("Init with config [" + config + "]");
		super.init(config);
	}





	@Override
	public void destroy()
	{
		doLog("Destroyed servlet");
		super.destroy();
	}





	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		res.setContentType("text/html");
		// PrintWriter out = res.getWriter();

		//Files.copy(Paths.get("static", "index.html"), res.getOutputStream());
		Files.copy(Paths.get("static", "index-vue.html"), res.getOutputStream());
		//Files.copy(Paths.get("static", "auth.html"), res.getOutputStream());
		// out.println("Request = " + req);
		// out.println("PathInfo = " + req.getPathInfo());
	}





	private void doLog(String message)
	{
		System.out.println("## [" + this.name + "] " + message);
	}

}
