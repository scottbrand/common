package io.github.scottbrand.webservice.jwt.provider;

import java.security.Key;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import io.github.scottbrand.common.PropertyReader;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.security.IPasswordEncryptor;
import io.github.scottbrand.webservice.jwt.ITokenAuthenticator;
import io.github.scottbrand.webservice.jwt.UserData;
import io.github.scottbrand.webservice.jwt.provider.JWTTokenProvider.Config;



@Component(name = JWTTokenProvider.PID, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = Config.class)
public class JWTTokenProvider implements ITokenAuthenticator
{

    public static final String            PID       = "io.github.scottbrand.webservice.jwt";

    /**
     * The object that will read/write our token
     */
    private JwtConsumer                   jwtConsumer;

    /**
     * The key that is used to do the encryption
     */
    private Key                           key       = null;

    /**
     * Used by our Config to unencrypt Hex-Encoding encrypted configuration values.
     */
    private volatile IPasswordEncryptor encryptor = null;
    
    private PropertyReader pr = null;

    /**
     * Config object supplied via ConfigAdmin service
     */
    private Config                        config;





    @ObjectClassDefinition
    @interface Config
    {
        @AttributeDefinition(name = "key", required = true, description = "Key value used for encrypting tokens")
        String key() default Strings.EMPTY;





        @AttributeDefinition(name = "issuer", required = true, description = "String of who issued the token")
        String issuer() default Strings.EMPTY;





        @AttributeDefinition(name = "audience", required = true, description = "String of the token audience")
        String audience() default Strings.EMPTY;





        @AttributeDefinition(name = "timeout", required = true, description = "Number of minutes before the token times out")
        int timeout() default 10;





        @AttributeDefinition(name = "subject", required = true, description = "String of the token subject")
        String subject() default Strings.EMPTY;
    }





    @Activate
    public void activate(Config config)
    {
        this.config = config;
        pr = new PropertyReader(encryptor);

        try
        {
            key = new HmacKey(pr.getProperty(config.key()).getBytes(Strings.UTF8));

            jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(30)
                    .setRequireSubject()
                    .setExpectedIssuer(pr.getProperty(config.issuer()))
                    .setExpectedAudience(pr.getProperty(config.audience()))
                    .setVerificationKey(key)
                    .setRelaxVerificationKeyValidation()
                    .build();

        }
        catch (Throwable t)
        {
            ILogger log = ServiceLocator.getLogger();
            if (log != null)
                log.error("Unable to initialize token provider",t);
        }
    }





    @Override
    public UserData authenticate(HttpServletRequest request)
    {
    	String jwt = getJWTString(request);
  
    	if (jwt == null)
    		return null;
    	
    	try
    	{
	        JwtClaims c = jwtConsumer.processToClaims(jwt);
	            
	                //
	                // place the token in the HandlerContext so the refreshToken
	                // method can get the token to refresh it.
	                //
	        
	        return  new UserData(c.getClaimValue(UserData.USER_ID, Long.class),
	                                  c.getClaimValue(UserData.LOGON_ID, String.class),
	                                  c.getClaimValue(UserData.NAME, String.class),
	                                  c.getClaimValue(UserData.REMOTE_IP,String.class));
    	}
    	catch (Throwable t)
    	{
    		ServiceLocator.getLogger().error(t.getMessage(),t);
    		return null;
    	}
    }


    
    
    protected String getJWTString(HttpServletRequest request)
    {
    	String header = request.getHeader(AUTHORIZATION);
        if (Strings.isNullOrEmpty(header))
            return null;
        
        try
        {
            if (jwtConsumer == null)
                throw new Throwable("token provider has null consumer, provider not initialized properly");
                
            if (header.indexOf(' ') == -1)
                return null;
                
                //
                // our token should be after the "Bearer " schema name.
                //
            return header.split(" ")[1];
        }
        catch (Throwable t)
        {
        	ServiceLocator.getLogger().error(t.getMessage(),t);
        	return null;
        }
    }



    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
    {
        String jwt = getJWTString(request);
        if (Strings.isNullOrEmpty(jwt))
            return;

        try
        {
            if (jwtConsumer == null)
                throw new Throwable("token consumer is null, provider is not initialized properly");

            JwtClaims claims = jwtConsumer.processToClaims(jwt);

            claims.setExpirationTimeMinutesInTheFuture(config.timeout());
            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
            jws.setKey(key);
            jws.setDoKeyValidation(false);
            jwt = jws.getCompactSerialization();

            response.setHeader(ACCESS_TOKEN, jwt);
        }
        catch (Throwable t)
        {
        	response.setHeader(ACCESS_TOKEN, null);
            ServiceLocator.getLogger().error("Problem refreshing token", t);
        }
    }





    @Override
    public String createToken(HttpServletRequest request, UserData user)
    {
        try
        {
            if (key == null)
                throw new Throwable("encryption key is null, provider is initialized properly");
            JwtClaims claims = new JwtClaims();
            claims.setIssuedAtToNow();
            claims.setGeneratedJwtId();
            claims.setExpirationTimeMinutesInTheFuture(config.timeout());
            claims.setSubject(config.subject());
            claims.setIssuer(config.issuer());
            claims.setAudience(config.audience());
            claims.setClaim(UserData.USER_ID, user.getUserID());
            claims.setClaim(UserData.NAME, user.getName());
            claims.setClaim(UserData.LOGON_ID, user.getLogonID());
            claims.setClaim(UserData.REMOTE_IP, user.getRemoteIPAddress());

            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
            jws.setKey(key);
            jws.setDoKeyValidation(false);

            return jws.getCompactSerialization();
        }
        catch (Throwable t)
        {
            ServiceLocator.getLogger().error("Problem creating token", t);
            return null;
        }
    }





    @Reference(target = "(" + IPasswordEncryptor.HEX_ENCODING + ")")
    private void setEncryptor(IPasswordEncryptor encryptor)
    {
        this.encryptor = encryptor;
    }
}
