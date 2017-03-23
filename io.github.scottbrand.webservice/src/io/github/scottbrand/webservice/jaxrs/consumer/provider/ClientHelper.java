package io.github.scottbrand.webservice.jaxrs.consumer.provider;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



// inspired by https://gist.github.com/1069465
public class ClientHelper
{

	public static SSLContext createSSLContext()
	{
		TrustManager[] certs = new TrustManager[] { new X509TrustManager()
		{

			@Override
			public X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}





			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
			{
				// no content
			}





			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
			{
				// no content
			}
		} };
		SSLContext ctx = null;
		try
		{
			ctx = SSLContext.getInstance("TLS");
			ctx.init(null, certs, new SecureRandom());
			return ctx;
		}
		catch (java.security.GeneralSecurityException shouldNotHappen)
		{
			throw new IllegalStateException(shouldNotHappen);
		}
	}





	public static HostnameVerifier createHostNameVerifier()
	{
		HostnameVerifier verifier = new HostnameVerifier()
		{

			@Override
			public boolean verify(String hostname, SSLSession session)
			{
				return true;
			}
		};
		return verifier;
	}










	private ClientHelper()
	{
		// prevent instantiation
	}

}