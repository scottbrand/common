package io.github.scottbrand.common.security;

import java.util.Map;

import io.github.scottbrand.common.BooleanResult;


public interface IAuthenticator
{

	public static final String	PRINCIPAL	= "USER";
	public static final String	PASSWORD	= "PASS";





	/**
	 * The authenticate method is how an application makes the request to check
	 * a users <tt>userID</tt> and <tt>password</tt> credentials.
	 * 
	 * @param userID
	 * @param password
	 * 
	 * @return true if the underlying implementation could authenticate the
	 *         user. Otherwise false.
	 */
	BooleanResult authenticate(String userID, String password);





	BooleanResult authenticate(Map<String, Object> credentials);

}
