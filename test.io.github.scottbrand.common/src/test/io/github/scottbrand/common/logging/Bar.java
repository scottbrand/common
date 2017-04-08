package test.io.github.scottbrand.common.logging;

import java.util.List;

import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;

public class Bar
{

	public String saySomething()
	{
		try
		{
			List<IFoo> foos = ServiceLocator.getInstance().getServices(IFoo.class,"(foo=b*)");
			return foos.toString();
		}
		catch (Throwable t)
		{
		    ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
			return "Crap";
		}
	}
}
