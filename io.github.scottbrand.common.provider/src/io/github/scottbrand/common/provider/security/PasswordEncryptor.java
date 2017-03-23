package io.github.scottbrand.common.provider.security;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.security.IPasswordEncryptor;



public abstract class PasswordEncryptor implements IPasswordEncryptor
{
    protected Cipher          encrypter;
    protected Cipher          decrypter;
    protected IvParameterSpec iv;
    protected SecretKeySpec   key;
    protected String          transformation;




    protected void onActivate() throws Throwable
    {
        transformation = Strings.concat('/', getAlgorithm(),getBlockMode(),getPadding());
        
        encrypter = Cipher.getInstance(transformation);
        decrypter = Cipher.getInstance(transformation);

        encrypter.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(), getIvParameterSpec());
        decrypter.init(Cipher.DECRYPT_MODE, getSecretKeySpec(), getIvParameterSpec());
    }





    @Override
    public abstract String decrypt(String encryptedText) throws Exception;





    @Override
    public abstract String encrypt(String clearText) throws Exception;





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
