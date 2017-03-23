package io.github.scottbrand.common.provider.mail;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.BooleanResult;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.mail.Envelope;
import io.github.scottbrand.common.mail.IMailConnector;
import io.github.scottbrand.common.mail.IMailService;



/**
 * Basic implementation of the Mail interface. This class can be injected as the
 * impl of the Mail interface.
 * 
 * @author Scott Brand
 *
 */

@Component
public class MailService implements IMailService
{
	private static final String	HOST_PROPERTY	= "mail.smtp.host";
	private static final String	PORT_PROPERTY	= "mail.smtp.port";

	private IMailConnector		connector;





	@Override
	public BooleanResult sendText(Envelope e)
	{
		e.setContentType(Envelope.TYPE_TEXT);
		return send(e);
	}





	@Override
	public BooleanResult sendHTML(Envelope e)
	{
		e.setContentType(Envelope.TYPE_HTML);
		return send(e);
	}




    @Override
    public BooleanResult send(Envelope e)
    {
    	
		if (e == null ||Strings.isNullOrEmpty(e.getMessage()))
		{
			return new BooleanResult(new Throwable("Null Envelope or no text in message"));
		}
		Properties props = new Properties();
		props.put(HOST_PROPERTY, connector.getHost());
		props.put(PORT_PROPERTY, connector.getPort());
		Session session = Session.getInstance(props);
		session.setDebug(connector.getDebug());
        
        try
        {
            Message msg = new MimeMessage(session);
            if (e.getFrom() != null)
                msg.setFrom(new InternetAddress(e.getFrom()));
            
            addRecipientTypes(msg,Message.RecipientType.TO,e.getRecipients());
            addRecipientTypes(msg,Message.RecipientType.CC,e.getCC());
            addRecipientTypes(msg,Message.RecipientType.BCC,e.getBCC());
            
            if(e.isPriority())
                msg.addHeader("X-Priority", "2");
            if (e.getSubject() != null)
                msg.setSubject(e.getSubject());
            
            Multipart mp = new MimeMultipart(); 
            
            if (e.getMessage() != null)
                msg.setText(e.getMessage());
            
            addBody(mp,e);
            
            addAttachments(mp,e);
            
            msg.setContent(mp);
            msg.setSentDate(new Date());
            
            
                        
            Transport.send(msg);
            return BooleanResult.TRUE;
        }
        catch(Throwable t)
        {
            return new BooleanResult(t);
        }
    }

    

    
    
    private void addRecipientTypes(Message msg, Message.RecipientType type, List<String> addresses)
    throws Exception
    {
        if (addresses == null || addresses.size() == 0)
            return;
        for (String a : addresses)
            msg.addRecipient(type,new InternetAddress(a));
    }
    
    

    

    
    private void addBody(Multipart mp, Envelope e)
    throws Exception
    {
        MimeBodyPart mb = new MimeBodyPart();
        mb.setContent(e.getMessage(),e.getContentType());
        mp.addBodyPart(mb);
    }
 

    
    private void addAttachments(Multipart mp, Envelope e)
    throws Exception
    {
        List<File> list = e.getAttachments();
        for (File f : list)
        {
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setDataHandler(new DataHandler(new FileDataSource(f.getAbsolutePath())));
            mbp.setFileName(f.getName());
            mp.addBodyPart(mbp);
        }
    }



	@Reference
	public void setConnector(IMailConnector connector)
	{
		this.connector = connector;
	}

}
