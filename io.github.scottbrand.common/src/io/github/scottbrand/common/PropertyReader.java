package io.github.scottbrand.common;

import java.util.Map;

import io.github.scottbrand.common.security.IPasswordEncryptor;


/**
 * When reading property files, the values may contains substitution variable replacement
 * like ${user.home} or other values.
 * In addition, property values that are wrapped in "ENC(x)" are treated where "x" is an encrypted
 * value and must be decrypted.
 * This class provides the wrapper to handle the conversation of such property values.
 * 
 * In order to decrypt "ENC(x)" encoded values, an appropriate instance of @{link IPasswordEncryptor}
 * must be provided either through the constructor or the {@link #setPasswordEncryptor(IPasswordEncryptor)}
 * method.
 * 
 * For variable replacement, and map should be provideed via the {@link #setVarMap(Map)} method.
 * Variable replacement is done via the {@link VariableReplacer} class.
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public class PropertyReader
{
    
    /**
     * When a property in a properties file needs to contain an encrypted value, then
     * the value should begin with the ENCRYPTION_START string, followed by the encrypted value,
     * followed by the ENCRYPTION_END string.
     */
    public static final String ENCRYPTION_START = "ENC(";
    
    public static final String ENCRYPTION_END   = ")";
    
    IPasswordEncryptor  passwordEncryptor;
    VariableReplacer    varReplacer;
    Map<String, Object> varMap;





    public PropertyReader()
    {
        varReplacer = new VariableReplacer();
        varMap = Create.map(String.class, Object.class);
    }





    public PropertyReader(IPasswordEncryptor passwordEncryptor)
    {
        this();
        this.passwordEncryptor = passwordEncryptor;
    }





    public void setPasswordEncryptor(IPasswordEncryptor passwordEncryptor)
    {
        this.passwordEncryptor = passwordEncryptor;
    }





    public void setVarMap(Map<String, Object> varMap)
    {
        this.varMap = varMap;
    }





    public String getProperty(String value)
    {
        if (Strings.isNullOrEmpty(value))
            return Strings.EMPTY;

        String newValue = value;
        if (value.startsWith(ENCRYPTION_START) && value.endsWith(ENCRYPTION_END))
        {
            String s = value.substring(ENCRYPTION_START.length(), value.length() - 1);
            if (passwordEncryptor != null)
            {
                try
                {
                    newValue = passwordEncryptor.decrypt(s);
                }
                catch (Throwable t)
                {
                    ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
                }
            }
        }
        return CollectionUtil.isMapNullOrEmpty(varMap) ? newValue :varReplacer.replace(newValue, varMap);
    }
}
