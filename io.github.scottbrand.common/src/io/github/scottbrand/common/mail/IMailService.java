package io.github.scottbrand.common.mail;



import io.github.scottbrand.common.BooleanResult;




/**
 * Simple interface to provide basic mail services.
 * 
 * @author Scott Brand
 * 
 */
public interface IMailService
{
	/**
	 * The MailConnector object tell the mail service which mail server to use.
	 * 
	 * @param connector
	 */
	public void setConnector(IMailConnector connector);





	/**
	 * Send the contents of the given Envelope via the service
	 * 
	 * @param e
	 * @return
	 */
	public BooleanResult send(Envelope e);





	/**
	 * Send the contents of the given Envelope as a text/plain content type.
	 * 
	 * @param e
	 * @return
	 */
	public BooleanResult sendText(Envelope e);





	/**
	 * Send the contents of the given Envelope as a text/html content type.
	 * 
	 * @param e
	 * @return
	 */
	public BooleanResult sendHTML(Envelope e);





}
