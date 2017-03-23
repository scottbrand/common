package io.github.scottbrand.common.mail;

/**
 * Simple interface used to pass connection information to a Mail service for
 * the sending of emails.
 * 
 * @author Scott Brand
 *
 */
public interface IMailConnector
{
	public static final String	DEFAULT_HOST	= "mailhost";
	public static final String	DEFAULT_PORT	= "25";





	String getHost();





	String getPort();
	
	
	
	boolean getDebug();
}
