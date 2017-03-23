package io.github.scottbrand.webservice.jaxrs.publisher.provider;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import io.github.scottbrand.webservice.jaxrs.publisher.ApplicationConfiguration;



@SuppressWarnings("rawtypes")
public class Configuration implements ManagedService
{

	static final String				CONFIG_SERVICE_PID		= "io.github.scottbrand.jaxrs.connector";
	static final String				PROPERTY_ROOT			= "root";
	static final String             DEFAULT_SERVICE_CONTEXT = "/services";
	static final String				PROPERTY_PUBLISH_DELAY	= "publishDelay";
	static final long				DEFAULT_PUBLISH_DELAY	= 150;

	private final JAXRSConnector	connector;
	private long					publishDelay;
	private String					rootPath;





	public Configuration(JAXRSConnector jaxRsConnector)
	{
		this.connector = jaxRsConnector;
		this.publishDelay = DEFAULT_PUBLISH_DELAY;
	}





	@Override
	public void updated(Dictionary properties) throws ConfigurationException
	{
		if (properties != null)
		{
			Object root = properties.get(PROPERTY_ROOT);
			ensureRootIsPresent(root);
			String rootPath = (String) root;
			ensureRootIsValid(rootPath);
			this.rootPath = rootPath;
			
			ApplicationConfiguration.log.debug("registering webservice root: {}",rootPath);
			
			this.publishDelay = getPublishDelay(properties);
			connector.updateConfiguration(this);
		}
	}





	private void ensureRootIsValid(String rootPath) throws ConfigurationException
	{
		if (!rootPath.startsWith("/"))
		{
			throw new ConfigurationException(PROPERTY_ROOT, "Root path does not start with a /");
		}
	}





	private void ensureRootIsPresent(Object root) throws ConfigurationException
	{
		if (root == null || !(root instanceof String))
		{
			throw new ConfigurationException(PROPERTY_ROOT, "Property is not set or invalid.");
		}
	}





	private long getPublishDelay(Dictionary properties)
	{
		Object interval = properties.get(PROPERTY_PUBLISH_DELAY);
		if (interval == null)
		{
			return DEFAULT_PUBLISH_DELAY;
		}
		return ((Long) interval);
	}





	public long getPublishDelay()
	{
		return publishDelay;
	}





	public String getRoothPath()
	{
		return rootPath == null ? DEFAULT_SERVICE_CONTEXT : rootPath;
	}

}
