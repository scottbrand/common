package io.github.scottbrand.common.provider.l10n;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

import aQute.lib.utf8properties.UTF8Properties;
import io.github.scottbrand.common.Numbers;
import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;
import io.github.scottbrand.common.l10n.ITranslationService;
import io.github.scottbrand.common.provider.l10n.TranslationService.Config;





/**
 * Implementation class to provide the ITranslationService. This implementation
 * class will gather all resource properties as URL and store them in a map
 * keyed by Locale. Only when a Locale and a property key is accessed will the
 * resource be loaded into memory.
 * 
 * @author Scott
 *
 */
@Component(configurationPolicy=ConfigurationPolicy.REQUIRE)
@Designate(ocd = Config.class)
public class TranslationService implements ITranslationService
{
	
	static final String MUST_HAVE_ENTRY = "/OSGI-INF/l10n/bundle.properties";

	
	@ObjectClassDefinition
    @interface Config
    {
        @AttributeDefinition(name="package prefix", required=true, description="Comma Separate List of Package Prefixes to scan for bundle.properties")
        String package_prefix() default "io.github";
    }
	
	private TranslationBundleTracker		bundleTracker;
	private Locale							defaultLocale	= Locale.getDefault();
	private String							localeName		= null;

	//
	// Map<locale string, List<ResourceURL>>
	//
	private Map<String, List<ResourceURL>>	resourceMap		= new HashMap<>();

	//
	// Holds local variants list for look.
	// we will build this map during invocations.
	// Once a Locale variant has been built once, we do not need to build it
	// again.
	//
	private Map<String, List<String>>		variantMap		= new HashMap<>();




	public TranslationService()
	{
		localeName = defaultLocale.toString();
	}





	@Activate
	public void activate(BundleContext ctx, Config config)
	{
		int trackStates = Bundle.STARTING | Bundle.STOPPING | Bundle.RESOLVED | Bundle.INSTALLED | Bundle.UNINSTALLED | Bundle.ACTIVE;
		bundleTracker = new TranslationBundleTracker(ctx, trackStates, null, this,config.package_prefix());
		bundleTracker.open();
	}
	
	
	
	
	@Deactivate
	public void deactivate()
	{
		bundleTracker.close();
		bundleTracker = null;
	}





	@Override
	public boolean setLocale(Locale locale)
	{
		if (locale == null)
			return false;
		localeName = locale.toString();
		return true;
	}





	@Override
	public boolean resetLocale()
	{
		localeName = defaultLocale.toString();
		return true;
	}





	@Override
	public String getLocale()
	{
		return localeName;
	}





	@Override
	public String translate(String key)
	{
		return translateFull(key, localeName, (String[]) null);
	}





	@Override
	public String translateLocale(String key, String locale)
	{
		return translateFull(key, locale, (String[]) null);
	}





	@Override
	public String translate(String key, String... bindings)
	{
		return translateFull(key, localeName, bindings);
	}





	@Override
	public String translate(Object clazz, int index)
	{
		if (clazz == null && index < 0)
			return Strings.EMPTY;
		return translateFull(clazz.getClass().getSimpleName() + Strings.UNDERSCORE + index, localeName, (String[]) null);
	}





	@Override
	public String translate(Object clazz, int index, String... bindings)
	{
		if (clazz == null && index < 0)
			return Strings.EMPTY;
		return translateFull(clazz.getClass().getSimpleName() + Strings.UNDERSCORE + index, localeName, bindings);
	}





	@Override
	public String translateFull(String key, String locale, String... bindings)
	{
		List<String> variants = null;
		if (locale == null)
		{
			variants = new ArrayList<String>();
			variants.add(Strings.EMPTY);
		}
		variants = variantMap.get(locale);
		if (variants == null)
		{
			variants = buildVariants(locale);
			if (variants != null && variants.isEmpty() == false)
				variantMap.put(locale, variants);
		}
		if (variants == null)
			return key;

		String value = null;
		for (String v : variants)
		{

			List<ResourceURL> list = resourceMap.get(v);
			if (list != null)
				for (ResourceURL u : list)
				{
					value = u.get(key);
					if (value != null)
						break;
				}
			if (value != null)
				break;
		}
		value = bind(value, bindings);
		return (Strings.isEmpty(value)) ? MISSING_KEY_PREFIX + key + "]" : value;

	}





	private List<String> buildVariants(String locale)
	{

		List<String> variants = new ArrayList<String>();
		while (locale.length() > 0)
		{
			variants.add(locale);
			int i = locale.lastIndexOf('_');
			while (i >= 0 && locale.charAt(i) == '_')
				i--;
			if (i <= 0)
				break;
			locale = locale.substring(0, i + 1);
		}
		variants.add("");
		return variants;
	}





	protected String bind(String value, String... bindings)
	{
		if (value == null)
			return Strings.returnNonNull(value);

		if (bindings == null || bindings.length == 0 || (bindings.length == 1 && bindings[0] == null))
			return value;

		int length = value.length();
		int bufLen = length + (bindings.length * 20);
		StringBuilder buffer = new StringBuilder(bufLen);
		for (int i = 0; i < length; i++)
		{
			char c = value.charAt(i);
			switch (c)
			{
				case '{':
					int index = value.indexOf('}', i);
					// if we don't have a matching closing brace then...
					if (index == -1)
					{
						buffer.append(c);
						break;
					}
					i++;
					if (i >= length)
					{
						// this is a trailing "{" char at the end of the string
						buffer.append(c);
						break;
					}
					// look for a substitution
					int number = Numbers.getInteger(value.substring(i, index), -1, 0, bindings.length - 1);
					if (number == -1)
					{
						buffer.append(c);
						break;
					}
					buffer.append(bindings[number]);
					i = index;
					break;
				case '\'':
					// if a single quote is the last char on the line then skip
					// it
					int nextIndex = i + 1;
					if (nextIndex >= length)
					{
						buffer.append(c);
						break;
					}
					char next = value.charAt(nextIndex);
					// if the next char is another single quote then write out
					// one
					if (next == '\'')
					{
						i++;
						buffer.append(c);
						break;
					}
					// otherwise we want to read until we get to the next single
					// quote
					index = value.indexOf('\'', nextIndex);
					// if there are no more in the string, then skip it
					if (index == -1)
					{
						buffer.append(c);
						break;
					}
					// otherwise write out the chars inside the quotes
					buffer.append(value.substring(nextIndex, index));
					i = index;
					break;
				default:
					buffer.append(c);
			}
		}
		return buffer.toString();
	}





	public void addResource(Bundle bundle)
	{
		String bl = bundle.getHeaders().get(org.osgi.framework.Constants.BUNDLE_LOCALIZATION);
		if (bl == null || bl.trim().length() == 0)
			bl = org.osgi.framework.Constants.BUNDLE_LOCALIZATION_DEFAULT_BASENAME;
		String[] parts = bl.split("/");
		Enumeration<URL> e = null;

		if (parts.length == 1)
			e = bundle.findEntries(Strings.EMPTY, "bundle*.properties", true);
		else
			e = bundle.findEntries(bl.substring(0, bl.length() - parts[parts.length - 1].length() - 1), "bundle*.properties", true);
		if (e == null)
			return;

		while (e.hasMoreElements())
		{
			URL url = (URL) e.nextElement();
			String s = url.getPath();
			int i = s.lastIndexOf('/');
			if (i != -1)
				s = s.substring(i + 1);
			s = s.replace(".properties", Strings.EMPTY).replaceFirst("bundle", Strings.EMPTY);
			if (s.length() > 0 && s.charAt(0) == '_')
				s = s.substring(1);
			List<ResourceURL> urlList = resourceMap.get(s);
			if (urlList == null)
			{
				urlList = new ArrayList<ResourceURL>();
				resourceMap.put(s, urlList);
			}
			urlList.add(new ResourceURL(bundle.getSymbolicName() + bundle.getVersion(), url));
		}
	}





	public void removeResource(Bundle bundle)
	{

	}





	class ResourceURL
	{
		URL			url;
		Properties	properties;
		String		bundleName;





		public ResourceURL(String bundleName, URL url)
		{
			this.url = url;
			this.bundleName = bundleName;
		}





		public String getBundleName()
		{
			return bundleName;
		}





		public synchronized String get(String key)
		{
			if (properties == null)
			{
				properties = new UTF8Properties();
				try
				{
					properties.load(url.openStream());
				}
				catch (Throwable t)
				{
				    ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
				}
			}
			return properties.getProperty(key);
		}
	}





	private static final class TranslationBundleTracker extends BundleTracker<Object>
	{

		TranslationService	service;
		String              packagePrefix;





		public TranslationBundleTracker(BundleContext context, int stateMask, BundleTrackerCustomizer<Object> customizer, TranslationService service, String packagePrefix)
		{
			super(context, stateMask, customizer);
			this.service = service;
			this.packagePrefix = packagePrefix;
			Bundle[] currentBundles = context.getBundles();
			for (Bundle b : currentBundles)
			{
				if (b.getSymbolicName().startsWith(packagePrefix) && b.getState() == Bundle.ACTIVE && b.getEntry(MUST_HAVE_ENTRY) != null)
				{
					service.addResource(b);
				}
			}
		}





		public Object addingBundle(Bundle bundle, BundleEvent event)
		{
			// Typically we would inspect bundle, to figure out if we want to
			// track it or not. If we don't want to track return null, otherwise
			// return an object.

			if (event != null && event.getType() == BundleEvent.STARTED && bundle.getState() == Bundle.ACTIVE)
			{
				if (bundle.getSymbolicName().startsWith(packagePrefix) && bundle.getEntry(MUST_HAVE_ENTRY) != null)
				{
					service.addResource(bundle);
					return bundle;
				}
			}
			return null;
		}
	}

}
