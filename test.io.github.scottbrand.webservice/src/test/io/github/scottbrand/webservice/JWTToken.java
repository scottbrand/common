package test.io.github.scottbrand.webservice;

public class JWTToken
{
	private String id_token;


	public JWTToken(String tokenValue)
	{
		id_token = tokenValue;
	}



	public String getId_token()
	{
		return id_token;
	}





	public void setId_token(String id_token)
	{
		this.id_token = id_token;
	}

}
