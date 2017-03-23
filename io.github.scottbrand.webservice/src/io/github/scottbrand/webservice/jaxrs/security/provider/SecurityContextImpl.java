package io.github.scottbrand.webservice.jaxrs.security.provider;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import io.github.scottbrand.webservice.jaxrs.security.AuthorizationHandler;



public class SecurityContextImpl implements SecurityContext
{

	private final boolean				secure;
	private final String				authenticationScheme;
	private final Principal				principal;
	private final AuthorizationHandler	authorizationHandler;





	public SecurityContextImpl(String authenticationScheme, Principal principal, boolean secure, AuthorizationHandler authorizationHandler)
	{
		this.authenticationScheme = authenticationScheme;
		this.principal = principal;
		this.secure = secure;
		this.authorizationHandler = authorizationHandler;
	}





	@Override
	public String getAuthenticationScheme()
	{
		return authenticationScheme;
	}





	@Override
	public Principal getUserPrincipal()
	{
		return principal;
	}





	@Override
	public boolean isSecure()
	{
		return secure;
	}





	@Override
	public boolean isUserInRole(String role)
	{
		if (authorizationHandler != null)
		{
			return authorizationHandler.isUserInRole(principal, role);
		}
		return false;
	}
}
