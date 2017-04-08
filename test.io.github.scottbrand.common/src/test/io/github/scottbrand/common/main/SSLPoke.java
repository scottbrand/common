package test.io.github.scottbrand.common.main;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



public class SSLPoke
{

    public static void main(String[] args)
    {
        SSLPoke ssl = new SSLPoke();
        System.setProperty("javax.net.debug","ssl,handshake,data,trustmanager"); 
        System.setProperty("javax.net.ssl.trustStore","C://temp/ssl/gvap_qa_client.keystore");
        System.setProperty("javax.net.ssl.trustStorePassword","gvap_qa_client_password");
        System.out.println("Testing with trustStore");
        ssl.doSSL("https://usmkdapgpd01.mck.experian.com:14200/gvap");
    }


    private void doSSL(String connectionURL)
    {
        try
        {
            URL    url = new URL(connectionURL);
            URLConnection connection;    
            connection = url.openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setAllowUserInteraction(false);
            connection.setUseCaches(false);
            ((HttpURLConnection)connection).setRequestMethod("POST");
            
            connection.connect();
            connection.getOutputStream().write("Hello".getBytes());
            System.out.println("Success on write");
            InputStream is = connection.getInputStream();
            byte[] b = new byte[4096];
            int bytesRead;
            StringBuilder sb = new StringBuilder(8192);
            while ( (bytesRead = is.read(b)) != -1)
            {
                System.out.println("bytes Read: " + bytesRead);
                sb.append(new String(b));
            }
            is.close();
            System.out.println("Response returned:\n" + sb);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }


    public void poke(String host, int port, boolean withAcceptance)
    {
        try
        {
            if (withAcceptance)
            {
                try
                {
                    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
                    {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }





                        public void checkClientTrusted(X509Certificate[] certs, String authType)
                        {
                        }





                        public void checkServerTrusted(X509Certificate[] certs, String authType)
                        {
                        }

                    } };

                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());

                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }

                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        System.out.println("validate host: " + hostname + ": session: " + session);
                        return true;
                    }
                });
            }

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port);

            InputStream in = sslsocket.getInputStream();
            OutputStream out = sslsocket.getOutputStream();

            // Write a test byte to get a reaction :)
            out.write(1);

            while (in.available() > 0)
            {
                System.out.print(in.read());
            }
            System.out.println("Successfully connected");

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

}
