package test.io.github.scottbrand.common.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.osgi.service.component.annotations.Component;

import test.io.github.scottbrand.common.jpa.domain.Platform;

@Component
public class PlatformService implements IPlatformService
{
	@PersistenceContext(unitName="GBP_MetaData")
	protected EntityManager em;
	
	@Override
	public List<Platform> getPlatforms()
	{
		 TypedQuery<Platform> q = em.createQuery("Select p from Platform p", Platform.class);
	     List<Platform> results = q.getResultList();
	     return results;
	}

}
