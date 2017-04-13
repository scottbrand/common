package test.io.github.scottbrand.common.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.osgi.service.component.annotations.Component;

import test.io.github.scottbrand.common.jpa.domain.User;

@Component
public class UsersService implements IUsersService
{
	@PersistenceContext(unitName="TEST_MetaData")
	protected EntityManager em;
	
	@Override
	public List<User> getUsers()
	{
		 TypedQuery<User> q = em.createQuery("Select u from User u", User.class);
	     List<User> results = q.getResultList();
	     return results;
	}

}
