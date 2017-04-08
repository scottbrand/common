package test.io.github.scottbrand.webservice;

import java.util.Date;

public class Credentials
{
	private String	username;
	private String	password;
	private Long	userID;
	private Date    loginDate;
	
	private MoreData moreData;





	public MoreData getMoreData()
	{
		return moreData;
	}





	public void setMoreData(MoreData moreData)
	{
		this.moreData = moreData;
	}





	public Date getLoginDate()
	{
		return loginDate;
	}





	public void setLoginDate(Date loginDate)
	{
		this.loginDate = loginDate;
	}





	public Long getUserID()
	{
		return userID;
	}





	public void setUserID(Long userID)
	{
		this.userID = userID;
	}





	public String getUsername()
	{
		return username;
	}





	public void setUsername(String username)
	{
		this.username = username;
	}





	public String getPassword()
	{
		return password;
	}





	public void setPassword(String password)
	{
		this.password = password;
	}

}
