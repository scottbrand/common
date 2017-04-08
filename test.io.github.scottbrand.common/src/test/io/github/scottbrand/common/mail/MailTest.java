package test.io.github.scottbrand.common.mail;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import aQute.lib.io.IO;
import io.github.scottbrand.common.BooleanResult;
import io.github.scottbrand.common.CollectionUtil;
import io.github.scottbrand.common.Create;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.logging.ILogger;
import io.github.scottbrand.common.mail.Envelope;
import io.github.scottbrand.common.mail.IMailService;

@Component
public class MailTest
{
	IMailService mailService;
	ILogger      log;
	
	
	@Activate
	public void activate(ComponentContext ctx)
	{
	    log = ServiceLocator.getLogger();
		log.info("instance of mailService is: {}",mailService);
		List<File> files = Create.list(File.class); 
		files.add(copyBundleFiles(ctx.getBundleContext().getBundle(),"/mail_test.txt"));
		files.add(copyBundleFiles(ctx.getBundleContext().getBundle(),"/mail_test.pdf"));
		files.add(copyBundleFiles(ctx.getBundleContext().getBundle(),"/mail_test.docx"));

		doTest(Envelope.TYPE_TEXT,"Hello,\nThis email came to you from " + this.getClass().getName(),files);
		doTest(Envelope.TYPE_HTML,readBundleFile(ctx.getBundleContext().getBundle(),"starwars.html"),files);
		
		System.exit(0);
	}
	
	
	private File copyBundleFiles(Bundle b, String resourceName)
	{
		try
		{
			URL u = b.getResource(resourceName);
			File f = b.getDataFile(resourceName);
			if (u == null || f == null)
				return null;
			
			if (f.exists() == false)
				Files.copy(u.openStream(), Paths.get(f.toURI()));
			return f;
		}
		catch (Throwable t)
		{
		    ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
			return null;
		}
	}
	
	
	
	
    private String readBundleFile(Bundle b, String resourceName)
    {
        try
        {
            URL u = b.getResource(resourceName);
            if (u == null)
                return null;
            return IO.collect(u.openStream());
        }
        catch (Throwable t)
        {
            ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
            return null;
        }
    }
	
	
	
	private void doTest(String contentType, String message, List<File> attachments)
	{
		log.info("sending an email");
		Envelope e = new Envelope();
		e.addRecipient("scott.brand@experian.com");
		e.addCC("scottalanbrand@yahoo.com");
		e.setMessage(message);
		e.setContentType(contentType);
		e.setSubject("Sent as type: " + contentType);
		if (CollectionUtil.isListNullOrEmpty(attachments) == false)
			for (File f : attachments)
				e.addAttachment(f);
		BooleanResult v = mailService.send(e);
		log.info("send returned: {}",v.getResult());
		if (v.getResult() == false)
			log.error("Mail problem was: ",v.getThrowable());
		
}
	
	
	
	@Reference
	public void setMailService(IMailService mailService)
	{
		this.mailService = mailService;
	}
	

}
