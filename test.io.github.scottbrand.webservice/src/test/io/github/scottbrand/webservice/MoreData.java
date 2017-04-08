package test.io.github.scottbrand.webservice;

public class MoreData
{
	Long	id;
	String	header;





	public Long getId()
	{
		return id;
	}





	public void setId(Long id)
	{
		this.id = id;
	}





	public String getHeader()
	{
		return header;
	}





	public void setHeader(String header)
	{
		this.header = header;
	}
	
	
	
	public String toString()
	{
		return "moreData : id : " + id + ", header: " + header;
	}

}
