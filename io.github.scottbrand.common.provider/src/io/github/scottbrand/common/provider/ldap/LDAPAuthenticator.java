package io.github.scottbrand.common.provider.ldap;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.BooleanResult;
import io.github.scottbrand.common.TypedResult;
import io.github.scottbrand.common.ldap.ILDAPConnector;
import io.github.scottbrand.common.security.IAuthenticator;



@Component(immediate=true,service=IAuthenticator.class)
public class LDAPAuthenticator extends LDAPBaseConnector implements IAuthenticator
{
	
	private static final String DN = "distinguishedName";
	
	
	
	private Map<String, String>	cachedMap	= new HashMap<String, String>();
	

	public LDAPAuthenticator()
	{
		super();
	}


	@Override
	public BooleanResult authenticate(String userID, String password)
	{
		try
		{
			if (userID == null || password == null)
				return new BooleanResult(new Throwable("id and password must be provided"));

			//
			// Our implementation will cache valid logins as to avoid future
			// LDAP requests.
			//
			String p = cachedMap.get(userID);
			if (p != null)
			{
				//
				// okay I found the userID in the cache, now see if the password
				// matches.
				// return immediately if it does.
				//

				if (p.equals(password))
					return BooleanResult.TRUE;

				//
				// if the password is wrong clear the entry from the cache and
				// move
				// on the standard LDAP authentication
				//

				cachedMap.remove(userID);
			}
			TypedResult<String> tcn = getDistinguishedName(userID);
			if (tcn == null || tcn.isValid() == false)
			{
				return new BooleanResult(new Throwable("Unable to find: " + userID + ", as a valid logonID",tcn == null ? null : tcn.getThrowable()));
			}

			//
			// Establish a temporary context with the CN and Password, to see if
			// we can connect to the LDAP with the users credentials.
			// If we can, then immediately close the context as we have done
			// what
			// we wanted to, namely authenticate the id/password.
			// If we could not establish the context them an Exception will
			// be thrown and we will capture it and return false.
			//

			Hashtable<String, String> env = createEnvironment(tcn.getResult(), password);

			DirContext ctx = new InitialLdapContext(env, null);

			if (ctx != null)
				ctx.close();
			cachedMap.put(userID, password);
			return BooleanResult.TRUE;
		}
		catch (Throwable t)
		{
			return new BooleanResult(t);
		}
	}








	/**
	 * The getDistinguishedName method will search the directory given by the
	 * {@link #getGeneralOU() getGeneralOU()} method, based on the search filter
	 * given by the {@link #getGeneralSearchFilter() getGeneralSearchFilter()}
	 * method. If the <tt>distinguishedName</tt> attribute is found, then its
	 * value is returned.
	 * 
	 * @param userID
	 *            the id of the user to search for.
	 * @return the value of the distinguishedName attribute if found, otherwise
	 *         returns <tt>null</tt>
	 * 
	 * @throws Exception
	 *             if any suboperation fails.
	 */
	private TypedResult<String> getDistinguishedName(String userID)
	{
		try
		{
			TypedResult<NamingEnumeration<?>> tne = search(getGeneralOU(), getGeneralSearchFilter(), new Object[] { userID }, DN);

			if (tne.isValid() == false)
			    return new TypedResult<String>(tne.getThrowable());
			
			NamingEnumeration<?> ne = tne.getResult();
			while (ne.hasMore())
			{
				SearchResult sr = (SearchResult) ne.next();

				for (NamingEnumeration<?> ne2 = sr.getAttributes().getAll(); ne2.hasMore();)
				{
					Attribute a = (Attribute) ne2.next();
					if (a != null && DN.equals(a.getID()))
						return new TypedResult<String>((String) (a.getAll().next()));
				}
			}

			return null;
		}
		catch (Throwable t)
		{
			return new TypedResult<String>(t);
		}
	}





	@Override
	public BooleanResult authenticate(Map<String, Object> arg0)
	{
		return new BooleanResult(new UnsupportedOperationException());
	}





	@Reference
	public void setConnector(ILDAPConnector connector)
	{
		this.connector = connector;
	}
}
