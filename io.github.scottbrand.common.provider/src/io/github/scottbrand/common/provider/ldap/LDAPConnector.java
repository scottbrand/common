package io.github.scottbrand.common.provider.ldap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import io.github.scottbrand.common.PropertyReader;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.ldap.ILDAPConnector;
import io.github.scottbrand.common.provider.ldap.LDAPConnector.Config;
import io.github.scottbrand.common.security.IPasswordEncryptor;

/**
 * Basic implementation of the LDAPConnector interface. This class can be
 * injected as the implementation of the ILDAPConnector interface.
 * 
 * The configuration to establish the LDAP connection to a server is externally read
 * and values should be encrypted with Hex Encoding.
 * Thus this component has a dependency on a IPasswordEncryptor (hex encoding)
 * 
 * @author Scott Brand
 * 
 */
@Component(name=LDAPConnector.PID,configurationPolicy=ConfigurationPolicy.REQUIRE)
@Designate(ocd = Config.class)
public class LDAPConnector implements ILDAPConnector
{
    public static final String PID = "io.github.scottbrand.common.provider.ldap";
    
	protected String	url;
	protected String	principal;
	protected String	password;
	protected String	scheme;
	protected String	ou;
	protected String	logonFilter;
	protected String	surNameFilter;
	
	protected volatile IPasswordEncryptor encryptor = null;

	
    @ObjectClassDefinition
    @interface Config
    {
        @AttributeDefinition(name="LDAP URL", required=true, description="URL of LDAP Server")
        String url() default Strings.EMPTY;
        
        @AttributeDefinition(name="Principal", required=true, description="Principal name of how to connect")
        String principal() default Strings.EMPTY;
            
        @AttributeDefinition(name="Password", required=true, description="Password of Principal")
        String password() default Strings.EMPTY;
        
        @AttributeDefinition(name="Scheme", required=true, description="Scheme of LDAP; typically: simple")
        String scheme() default Strings.EMPTY;
        
        @AttributeDefinition(name="OU", required=true, description="OU LDAP string")
        String ou() default Strings.EMPTY;

        @AttributeDefinition(name="Logon Filter", required=true, description="Filter to use to establish logon")
        String logonFilter() default Strings.EMPTY;
        
        @AttributeDefinition(name="SurName Filter", required=true, description="Filter to use to when searching surname")
        String surNameFilter() default Strings.EMPTY;

    }


	
	@Activate
	public void activate(Config config)
	{
	    PropertyReader pr = new PropertyReader(encryptor);
	    
		url = pr.getProperty(config.url()); 
		principal = pr.getProperty(config.principal()); 
		password = pr.getProperty(config.password()); 
		scheme = pr.getProperty(config.scheme()); 
		ou = pr.getProperty(config.ou());   
		logonFilter = pr.getProperty(config.logonFilter());  
		surNameFilter = pr.getProperty(config.surNameFilter()); 
	}

	
	@Modified
	public void modified(Config config)
	{
	    activate(config);
	}

	@Override
	public String getURL()
	{
		return url;
	}





	public void setUrl(String url)
	{
		this.url = url;
	}





	@Override
	public String getPrincipal()
	{
		return principal;
	}





	@Override
	public String getPassword()
	{
		return password;
	}





	@Override
	public String getScheme()
	{
		return scheme;
	}





	@Override
	public String getOU()
	{
		return ou;
	}





	@Override
	public String getLogonFilter()
	{
		return logonFilter;
	}





	public String getOu()
	{
		return ou;
	}





	public void setOu(String ou)
	{
		this.ou = ou;
	}





	public String getUrl()
	{
		return url;
	}





	public void setPrincipal(String principal)
	{
		this.principal = principal;
	}





	public void setPassword(String password)
	{
		this.password = password;
	}





	public void setScheme(String scheme)
	{
		this.scheme = scheme;
	}





	public void setLogonFilter(String logonFilter)
	{
		this.logonFilter = logonFilter;
	}





	@Override
	public String getSurNameFilter()
	{
		return surNameFilter;
	}





	public void setSurNameFilter(String surNameFilter)
	{
		this.surNameFilter = surNameFilter;
	}
	
	
	
	/**
	 * We only want the Hex encoder
	 * 
	 * @param encryptor
	 */
	@Reference(target="(" + IPasswordEncryptor.HEX_ENCODING + ")")
	private void setEncryptor(IPasswordEncryptor encryptor)
	{
	    this.encryptor = encryptor;
	}
}
