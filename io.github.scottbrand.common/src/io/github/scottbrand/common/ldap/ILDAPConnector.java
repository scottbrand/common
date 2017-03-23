package io.github.scottbrand.common.ldap;

/**
 * Simple interface used to pass connection information to an LDAP service.
 * 
 * @author Scott Brand
 *
 */
public interface ILDAPConnector
{
	String getURL();





	String getPrincipal();





	String getPassword();





	String getScheme();





	String getOU(); // "DC=company,DC=local";





	String getLogonFilter(); // "(&(objectClass=*)(sAMAccountName={0}))";





	String getSurNameFilter(); // "(&(objectClass=*)(sn={0}*))"
}
