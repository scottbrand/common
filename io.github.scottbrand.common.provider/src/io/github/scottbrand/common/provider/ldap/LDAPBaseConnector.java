package io.github.scottbrand.common.provider.ldap;

import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;

import io.github.scottbrand.common.BooleanResult;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.TypedResult;
import io.github.scottbrand.common.ldap.ILDAPConnector;



public abstract class LDAPBaseConnector
{
	protected ILDAPConnector	connector;
	protected DirContext		ctx;





	/**
	 * The connect method will attempt to make a connection to the LDAP
	 * server(s) based on properties derived by the interface.
	 *
	 * @throws Exception
	 *             if a connection can not be established.
	 */

	protected BooleanResult connect()
	{
		try
		{

			Hashtable<String, String> env = createEnvironment(connector.getPrincipal(), connector.getPassword());

			ctx = new InitialLdapContext(env, null);

			return BooleanResult.TRUE;
		}
		catch (Throwable t)
		{
			ctx = null;
			return new BooleanResult(t);
		}
	}





	/**
	 * Basic way of building an Environment object to allow us to connect to an
	 * LDAP server.
	 * 
	 * @param principal
	 * @param password
	 * @return
	 */
	protected Hashtable<String, String> createEnvironment(String principal, String password)
	{
		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, connector.getURL());
		env.put(Context.SECURITY_AUTHENTICATION, connector.getScheme());
		// env.put(Context.SECURITY_PRINCIPAL, connector.getPrincipal());
		// env.put(Context.SECURITY_CREDENTIALS, connector.getPassword());
		env.put(Context.SECURITY_PRINCIPAL, principal);
		env.put(Context.SECURITY_CREDENTIALS, password);
		// Specify SSL

		env.put("java.naming.ldap.factory.socket", SSLSocketFactory.class.getName()); 
		env.put(Context.SECURITY_PROTOCOL, "ssl");

		return env;
	}










	/**
	 * The isConnected method will return the state of the LDAP context
	 * connection. This method will return the current state of the context, but
	 * does not actually test it. Therefore, eventhough this method may report
	 * that a connection is established, the next time the connection is
	 * actually used, the application may find that it is no longer connected
	 * due to external system time outs, or other issues.
	 * 
	 * @return true if the context is defined, false if the context is null
	 *         which implies that there is no current connection.
	 */

	protected boolean isConnected()
	{
		return (ctx != null);
	}





	/**
	 * The disconnect method will close the current LDAP context connection. And
	 * set the internal context attribute to null.
	 * 
	 */
	protected void disconnect()
	{
		try
		{
			if (ctx != null)
				ctx.close();
		}
		catch (Exception e)
		{
			ServiceLocator.getLogger().error(Strings.EXCEPTION, e);
		}
		finally
		{
			ctx = null;
		}
	}





	protected TypedResult<NamingEnumeration<?>> search(String searchFrom, String searchFilter, Object[] searchArgs, String attribute)
	{
		try
		{
			String[] attributeArray = null;

			SearchControls controls = new SearchControls();
			NamingEnumeration<?> ne = null;

			if (attribute == null)
				attributeArray = new String[1];
			else if (attribute.indexOf(",") == -1)
				attributeArray = new String[] { attribute };
			else
			{
				StringTokenizer st = new StringTokenizer(attribute, ",");
				attributeArray = new String[st.countTokens()];
				for (int i = 0; st.hasMoreTokens(); i++)
					attributeArray[i] = st.nextToken();
			}

			controls.setReturningAttributes(attributeArray);
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			//
			// make sure we are connected before doing a search of the context.
			//

			if (isConnected() == false)
				connect();

			try
			{
				ne = ctx.search(searchFrom, searchFilter, searchArgs, controls);
			}
			catch (Throwable t)
			{
				disconnect();
				connect();
				return new TypedResult<NamingEnumeration<?>>(t);
			}

			return new TypedResult<NamingEnumeration<?>>(ne);
		}
		catch (Throwable t)
		{
			return new TypedResult<NamingEnumeration<?>>(t);
		}
	}





	public void setConnector(ILDAPConnector connector)
	{
		this.connector = connector;
	}





	protected String getGeneralOU()
	{
		return connector.getOU(); // "DC=comany,DC=local";
	}





	protected String getGeneralSearchFilter()
	{
		return connector.getLogonFilter(); // "(&(objectClass=*)(sAMAccountName={0}))";
	}





	protected String getSurNameFilter()
	{
		return connector.getSurNameFilter(); // "(&(objectClass=*)(sn={0}*))"
	}




}
