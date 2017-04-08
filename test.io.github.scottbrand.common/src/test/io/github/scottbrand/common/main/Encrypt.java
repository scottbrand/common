package test.io.github.scottbrand.common.main;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.github.scottbrand.common.Hex;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;



public class Encrypt
{
    private Cipher   encrypter, decrypter;
    PBEParameterSpec ps;
    SecretKeyFactory kf;
    SecretKey        k;

    /**
     * Configuration of used algorithm. 
     * Do not use less secure DES. Setting up another value can prevent of
     * using this software and will require to specify some additional
     * parameters for initial vector.
     */
    private static final String ALGORITHM = "AES";

    /**
     * Configuration of block mode. Do not use less secure ECB mode.
     */
    private static final String BLOCK_MODE = "CBC";

    /**
     * Configuration of "padding".
     */
    private static final String PADDING = "PKCS5Padding";
    
    
    /**
     * 128-bit of salt. Not so secure, but stable bytes for use with AES-CBC mode.
     */
    private static final IvParameterSpec CBC_SALT = new IvParameterSpec(
            Hex.toByteArray("0722384E5A57412B0C22384E8557412B"));
//            new byte[] { 7, 34, 56, 78, 90, 87, 65, 43, 12, 34, 56, 78, -123,
//                    87, 65, 43 });
    /**
     * 256-bit static key. Schould be replaced by generated one for better security.
     * But for demonstration purposes it's built-in.
     */
//    private static final SecretKeySpec STATIC_KEY = new SecretKeySpec(
//            new byte[] { 75, 108, 105, 99, 32, 107, 32, 104, 101, 115, 108,
//                    117, 109, 32, 101, 120, 116, 101, 114, 110, 105, 99, 104,
//                    32, 115, 121, 115, 116, 101, 109, 117, 32 }, ALGORITHM);



    public Encrypt()
    {

        ServiceLocator.getLogger().debug(Hex.toHexString(CBC_SALT.getIV()));
        ServiceLocator.getLogger().debug(Hex.toHexString("3xP3RI^N_N3xTG3N".getBytes()));

//        String saltkey = "0722384E5A57412B0C22384E8557412B";
        String skey    = "3378503352495E4E5F4E33785447334E";
        try
        {

            k = new SecretKeySpec(Hex.toByteArray(skey),ALGORITHM); 

            encrypter = Cipher.getInstance(ALGORITHM + "/" + BLOCK_MODE + "/" + PADDING);
            decrypter = Cipher.getInstance(ALGORITHM + "/" + BLOCK_MODE + "/" + PADDING);
            
            encrypter.init(Cipher.ENCRYPT_MODE, k, CBC_SALT);
            decrypter.init(Cipher.DECRYPT_MODE, k, CBC_SALT);
        }
        catch (Throwable t)
        {
            ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
        }
    }





    private synchronized String decrypt(String str) throws Exception
    {
        byte[] dec = Hex.toByteArray(str);
        byte[] utf8 = decrypter.doFinal(dec);
        return new String(utf8, "UTF-8");
    }





    private synchronized String encrypt(String str) throws Exception
    {
        byte[] utf8 = str.getBytes("UTF-8");
        byte[] enc = encrypter.doFinal(utf8);
        return Hex.toHexString(enc);
    }





    public static void main(String[] args)
    {
        try
        {
            Encrypt e = new Encrypt();

            String h = e.encrypt("gbmdPassword");
            ServiceLocator.getLogger().debug("h = " + h);
            String a = e.decrypt(h);
            ServiceLocator.getLogger().debug("a = " + a);

        }
        catch (Throwable t)
        {
            ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
        }

    }

}
