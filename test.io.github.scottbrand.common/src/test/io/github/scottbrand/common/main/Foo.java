package test.io.github.scottbrand.common.main;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Foo implements Serializable
{
    private String message;
	private Date date;
	private Long time;
	
	
	public Foo()
	{
		date = new Date();
		time = System.currentTimeMillis();
		message = "Hello World";
	}


	public Date getDate()
	{
		return date;
	}


	public void setDate(Date date)
	{
		this.date = date;
	}


	public Long getTime()
	{
		return time;
	}


	public void setTime(Long time)
	{
		this.time = time;
	}
	
	
	
	
	public String getMessage()
	{
	    return message;
	}
	
	
	public String toString()
	{
		return "Date is: " + date.toString() + " Time is: " + time;
	}
	
}
