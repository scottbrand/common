package io.github.scottbrand.common.provider.security;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import io.github.scottbrand.common.Hex;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.provider.security.HexPasswordEncryptor.Config;
import io.github.scottbrand.common.security.IPasswordEncryptor;



/**
 * Hex Based Password Encryptor.
 * This component requires a salt and secret key value to be provided from configuration file.
 * Both value should be hex encoded (see Hex) class.
 * 
 * In addition, this service component will then be able to encrypt and decrypt other hex-encoded
 * passwords or other values that come from property/config files.
 * 
 * @author Scott Brand
 *
 */
@Component(name=HexPasswordEncryptor.PID,property={IPasswordEncryptor.HEX_ENCODING},configurationPolicy=ConfigurationPolicy.REQUIRE)
@Designate(ocd = Config.class)
public class HexPasswordEncryptor extends PasswordEncryptor implements IPasswordEncryptor
{
    
    public static final String PID = "io.github.scottbrand.common.provider.security.hex";
    
    @ObjectClassDefinition
    @interface Config
    {
        @AttributeDefinition(name="Salt", required=true, description="Uppercase Hex encoded Encryption Salt")
        String salt() default Strings.EMPTY;
        
        @AttributeDefinition(name="Secret Key", required=true, description="Uppercase Hex encoded Encryption Secret Key")
        String key() default Strings.EMPTY;
    }

    
    @Activate
    public void activate(Config config)
    {
        String salt = config.salt();
        String skey = config.key();
        
        iv  = new IvParameterSpec(Hex.toByteArray(salt));
        key = new SecretKeySpec(Hex.toByteArray(skey),getAlgorithm());
        
        try
        {
            onActivate();
        }
        catch (Throwable t)
        {
            ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
        }
    }





    @Override
    public String decrypt(String encryptedText) throws Exception
    {
        return new String(decrypter.doFinal(Hex.toByteArray(encryptedText)), Strings.UTF8);
    }





    @Override
    public String encrypt(String clearText) throws Exception
    {
        return Hex.toHexString(encrypter.doFinal(clearText.getBytes(Strings.UTF8)));
    }





    @Override
    public IvParameterSpec getIvParameterSpec()
    {
        return iv;
    }





    @Override
    public SecretKeySpec getSecretKeySpec()
    {
        return key;
    }

}
