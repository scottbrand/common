package io.github.scottbrand.webservice.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This interface is used by the neo servlet and RemoteProxyHandler
 * object to authenticate access to an end point via the inspection
 * of a valid token (i.e. JWT/JOSE)
 * 
 * @author Scott Brand
 *
 */
public interface ITokenAuthenticator
{
    public static final String AUTHORIZATION  = "Authorization"; 
    public static final String ACCESS_TOKEN   = "access_token";
    
    /**
     * Pass in a HandlerContext object an allow the implementation to 
     * authenticate the request
     * 
     * @param request
     * @return true if authenticated, false otherwise.
     */
    UserData authenticate(HttpServletRequest request);
    
    
    
    /**
     * For each call made with potentially a valid token,
     * the implementation may want to signify to the token and the
     * response an update to the tokens freshness or time to live.
     * The method is provided to do that.
     * 
     * @param request
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response);
    
    
    
    String createToken(HttpServletRequest request, UserData user);
}
