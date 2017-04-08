package test.io.github.scottbrand.common.jpa;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.db.jpa.AbstractEMFBuilder;
import io.github.scottbrand.common.db.jpa.IPersistenceManager;
import test.io.github.scottbrand.common.jpa.domain.Platform;

@Component
public class EMFBuilder extends AbstractEMFBuilder
{

	@Activate
	public void activate(ComponentContext ctx)
	{
		Object o = onActivate(ctx.getBundleContext(),"GBP_MetaData",null);
		if (o != null)
			doTest();
	}
	
	
	
	@Override
	@Reference(target = "(ds.name=ds/postgres)")
	protected void setDataSource(DataSource ds)
	{
		this.ds = ds;
	}





	@Override
	@Reference(target="(osgi.unit.name=GBP_MetaData)")
	protected void setEntityManagerFactoryBuilder(EntityManagerFactoryBuilder emfb)
	{
		this.emfb = emfb;
	}

	
	
	
	private void doTest()
	{
		try
		{
			IPersistenceManager pm = ServiceLocator.getInstance().getService(IPersistenceManager.class);
			if (pm != null)
			{
				IPlatformService ips = pm.getPersistenceService(IPlatformService.class);
				List<Platform> platforms = ips.getPlatforms();
				if (platforms != null)
					for (Platform p : platforms)
					    ServiceLocator.getLogger().debug(p.toString());
			}
		}
		catch (Throwable t)
		{
		    ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
		}
	}
}
