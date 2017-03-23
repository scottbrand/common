package io.github.scottbrand.common.mail;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Basic Envelope object that contais most everything needed to
 * store content for sending an email via the Mail service.
 * 
 * @author Scott Brand
 *
 */
public class Envelope
{
    public static final String TYPE_TEXT = "text/plain";
    public static final String TYPE_HTML = "text/html";
    
    private List<String> recipients;

    private List<String> carbonCopies;

    private List<String> blindCarbonCopies;

    private String       from;

    private String       subject;

    private String       message;

    private String       contentType;

    private List<File>   attachments;

    private boolean      priority = false;





    /**
     * 
     */
    public Envelope()
    {
        recipients = new ArrayList<String>();
        carbonCopies = new ArrayList<String>();
        blindCarbonCopies = new ArrayList<String>();

        subject = null;
        message = null;

        attachments = new ArrayList<File>();

        contentType = TYPE_TEXT;
    }





    /**
     * @return Returns the attachments.
     */
    public List<File> getAttachments()
    {
        return attachments;
    }





    /**
     * @param attachments
     *            The attachments to set.
     */
    public void setAttachments(List<File> attachments)
    {
        if (attachments != null)
            this.attachments = attachments;
    }





    /**
     * @return Returns the bcc.
     */
    public List<String> getBCC()
    {
        return this.blindCarbonCopies;
    }





    /**
     * @param bcc
     *            The bcc to set.
     */
    public void setBCC(List<String> bcc)
    {
        if (bcc != null)
            this.blindCarbonCopies = bcc;
    }





    /**
     * @return Returns the cc.
     */
    public List<String> getCC()
    {
        return this.carbonCopies;
    }





    /**
     * @param cc
     *            The cc to set.
     */
    public void setCC(List<String> cc)
    {
        if (cc != null)
            this.carbonCopies = cc;
    }















    /**
     * @return Returns the message.
     */
    public String getMessage()
    {
        return message;
    }





    /**
     * @param message
     *            The message to set.
     */
    public void setMessage(String message)
    {
        this.message = message;
    }





    /**
     * @return Returns the recipients.
     */
    public List<String> getRecipients()
    {
        return recipients;
    }





    /**
     * @param recipients
     *            The recipients to set.
     */
    public void setRecipients(List<String> recipients)
    {
        if (recipients != null)
            this.recipients = recipients;
    }





    /**
     * @return Returns the subject.
     */
    public String getSubject()
    {
        return subject;
    }





    /**
     * @param subject
     *            The subject to set.
     */
    public void setSubject(String subject)
    {
        this.subject = subject;
    }





    public void addRecipient(String recipient)
    {
        if (recipient != null && recipient.length() > 0)
            recipients.add(recipient);
    }





    public void addCC(String cc)
    {
        if (cc != null && cc.length() > 0)
            carbonCopies.add(cc);
    }





    public void addBCC(String bcc)
    {
        if (bcc != null && bcc.length() > 0)
            blindCarbonCopies.add(bcc);
    }





    public void addAttachment(File attachment)
    {
        if (attachment != null)
        	attachments.add(attachment);
//        	if (attachment.exists())
//        		if (attachment.isFile())
//        			attachments.add(attachment);
    }





    /**
     * @return Returns the from.
     */
    public String getFrom()
    {
        return from;
    }





    /**
     * @param from
     *            The from to set.
     */
    public void setFrom(String from)
    {
        this.from = from;
    }





    /**
     * @return Returns the contentType.
     */
    public String getContentType()
    {
        return contentType;
    }





    /**
     * @param contentType
     *            The contentType to set.
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }





    /**
     * @param priority
     *            this sets an e-mail header indicating this is a high priority e-mail used for sending abnormal conditions
     */
    public void setPriority(boolean priority)
    {
        this.priority = priority;
    }





    public boolean isPriority()
    {
        return this.priority;
    }

}
