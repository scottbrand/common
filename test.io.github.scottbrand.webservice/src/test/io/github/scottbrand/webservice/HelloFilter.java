package test.io.github.scottbrand.webservice;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;



//@Component(property = { HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN + "=/hello/services" })

@Component(property = { HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN + "=/*", HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=hello)" })
public class HelloFilter implements Filter
{
	private String name;





	@Activate
	private void activate()
	{
		this.name = "HelloFilter";
	}

	// public HelloFilter(String name)
	// {
	// this.name = name;
	// }





	public void init(FilterConfig config) throws ServletException
	{
		doLog("Filter Init with config [" + config + "]");
	}





	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
	{
		doLog("Filter request [" + req + "]");
		chain.doFilter(req, res);
	}





	public void destroy()
	{
		doLog("Destroyed filter");
	}





	private void doLog(String message)
	{
		System.out.println("## [" + this.name + "] " + message);
	}
}
