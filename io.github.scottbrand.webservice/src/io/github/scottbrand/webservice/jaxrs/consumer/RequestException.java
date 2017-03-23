package io.github.scottbrand.webservice.jaxrs.consumer;

import javax.ws.rs.core.MultivaluedMap;



public class RequestException extends IllegalStateException
{

	private static final long serialVersionUID = -8939869961674834084L;
	
	private final RequestError error;





	public RequestException(RequestError error)
	{
		validateError(error);
		this.error = error;
	}





	private void validateError(RequestError error)
	{
		if (error == null)
		{
			throw new IllegalArgumentException("error must not be null");
		}
	}





	@Override
	public String getMessage()
	{
		return error.getMessage();
	}





	public int getStatus()
	{
		return error.getStatus();
	}





	public String getEntity()
	{
		return error.getEntity();
	}





	public String getMethod()
	{
		return error.getMethod();
	}





	public String getRequestUrl()
	{
		return error.getRequestUrl();
	}





	public MultivaluedMap<String, Object> getHeaders()
	{
		return error.getHeaders();
	}
}
