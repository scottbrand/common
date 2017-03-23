package io.github.scottbrand.common;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class FileFacade implements Serializable
{
    private static final long serialVersionUID = -1639333691369165688L;
    
    private String  absolutePath;
    private Long    size;
    private Date    lastModified;
    private String  extension;
    private String  name;
    private boolean canRead;
    private boolean canWrite;
    private boolean canExecute;
    private String  formattedLength;
    private boolean isDir;
    
    
    public FileFacade(File f)
    {
        if (f == null)
            return;
        
        absolutePath    = f.getAbsolutePath();
        
        if (absolutePath != null && absolutePath.length() > 0)
            absolutePath = absolutePath.replaceFirst("[\\w]+:","").replaceAll("\\\\","/");
        size            = f.length();
        lastModified    = new Date(f.lastModified());
        name            = f.getName();
        canRead         = f.canRead();
        canWrite        = f.canWrite();
        canExecute      = f.canExecute();
        extension       = "";
        formattedLength = null;
        isDir           = false;
        if (f.isFile())
        {
            int i = name.lastIndexOf('.');
            if (i != -1)
                extension = name.substring(i+1);
        }
        else if (f.isDirectory())
        {
            isDir = true;
        }
        
    }
    
    
    
    public FileFacade(File f, boolean asDirectory)
    {
        this(f);
        isDir = asDirectory;
    }
    
    
    
    public FileFacade(String absolutePath)
    {
        if (absolutePath == null)
            return;
        
        this.absolutePath = absolutePath.replaceFirst("[\\w]+:","").replaceAll("\\\\","/");
        size         = 0L;
        lastModified = new Date(0L);
        canRead      = false;
        canWrite     = false;
        extension    = "";

        int i = this.absolutePath.lastIndexOf("/");
        name = i != -1 ? this.absolutePath.substring(i+1) : this.absolutePath;
        
        formattedLength = null;
        i = name.lastIndexOf('.');
        if (i != -1)
            extension = name.substring(i+1);
    }
    
    
    




    public String getAbsolutePath()
    {
        return absolutePath;
    }





    public Long getSize()
    {
        return size;
    }





    public Date getLastModified()
    {
        return lastModified;
    }





    public String getExtension()
    {
        return extension;
    }





    public String getName()
    {
        return name;
    }






    public boolean isCanRead()
    {
        return canRead;
    }



    public boolean isCanWrite()
    {
        return canWrite;
    }


    
    public boolean isDirectory()
    {
        return isDir;
    }



    public String getFormattedLength()
    {
        if (formattedLength == null)
            formattedLength = formatLength(size);
        
        return formattedLength;
    }

    
    
    public static String formatLength(long size)
    {
        String bytes = "KB";

        if (size <= 0)
            return "0 KB";

        float s = size / 1024F;
        if (s > 1024)
        {
            s = size / (1024F * 1024F);
            bytes = "MB";
        }
        if (s > 1024)
        {
            s = size / (1024F * 1024F * 1024F);
            bytes = "GB";
        }
        if (s > 1024)
        {
            s = size / (1024F * 1024F * 1024F * 1024F);
            bytes = "TB";
        }
        return String.format("%1$,.2f %2$s", s, bytes);
    }



	public boolean isCanExecute()
	{
		return canExecute;
	}
	
	
	
	public String toString()
	{
	    return absolutePath;
	}
}
