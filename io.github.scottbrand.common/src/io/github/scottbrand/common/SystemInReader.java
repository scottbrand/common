package io.github.scottbrand.common;



/**
 * Convience class to print a message on the System.out and waiting for user input
 * which reads from System.in.
 * The bytes are returned when the "Enter" key is hit.
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public class SystemInReader
{
    public static String requestInputText(String message) throws Exception
    {
        byte[] byteText = new byte[500];
        char[] charText = null;
        System.out.print(message);
        while (true)
        {
            int i = System.in.read(byteText);
            if (i < 1)
                break;
            charText = new char[i - 2];
            for (int z = 0; z < i - 2; z++)
                charText[z] = (char) byteText[z];
            break;
        }
        return new String(charText);
    }
}
