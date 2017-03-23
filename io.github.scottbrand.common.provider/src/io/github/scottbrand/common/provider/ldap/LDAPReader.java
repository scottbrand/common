package io.github.scottbrand.common.provider.ldap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.TypedResult;
import io.github.scottbrand.common.ldap.ILDAPConnector;
import io.github.scottbrand.common.ldap.ILDAPReader;


@Component
public class LDAPReader extends LDAPBaseConnector implements ILDAPReader
{
	@Override
	public TypedResult<Map<String,List<String>>> readData(String logonID, String attributes)
	{
        try
        {
            TypedResult<NamingEnumeration<?>> tne   = search(getGeneralOU(), getGeneralSearchFilter(), new Object[] {logonID}, attributes);
            Map<String,List<String>>          map  = new HashMap<String,List<String>>();
            
            
            if (tne.isValid() == false)
                new TypedResult<Map<String,List<String>>>(tne.getThrowable());
            
            NE foo = new NE(attributes);
            
            NamingEnumeration<?> ne = tne.getResult();
            while (ne.hasMore())
            {
                SearchResult      sr  = (SearchResult)ne.next();
                for (NamingEnumeration<?> ne2 = sr.getAttributes().getAll(); ne2.hasMore();)
                     foo.goOverEnumeration((Attribute)ne2.next(),map);
            }
            return new TypedResult<Map<String,List<String>>>(map);
        }
        catch (Throwable t)
        {
            return new TypedResult<Map<String,List<String>>>(t);
        }
	}
	
	
	
	@Override
	public TypedResult<Map<String,List<String>>> findUsers(String surNamePattern, String attributes)
	{
        try
        {
            TypedResult<NamingEnumeration<?>> tne  = search(getGeneralOU(), getSurNameFilter(), new Object[] {surNamePattern}, attributes);
            Map<String,List<String>>          map  = new HashMap<String,List<String>>();
            
            if (tne.isValid() == false)
                new TypedResult<Map<String,List<String>>>(tne.getThrowable());

            Set<String> selectAttributes = new HashSet<String>(Arrays.asList(attributes.split(",")));
            
            NE foo = new NE(attributes);
            
            NamingEnumeration<?> ne = tne.getResult();
            while (ne.hasMore())
            {
                SearchResult      sr  = (SearchResult)ne.next();
                List<Attribute>   attributeList = new ArrayList<Attribute>();
                
                for (NamingEnumeration<?> ne2 = sr.getAttributes().getAll(); ne2.hasMore();)
                	attributeList.add((Attribute)ne2.next());
                if (attributeList.size() != selectAttributes.size())
                	continue;
                for (Attribute a : attributeList)
                	foo.goOverEnumeration(a, map);
            }
            return new TypedResult<Map<String,List<String>>>(map);
        }
        catch (Throwable t)
        {
            return new TypedResult<Map<String,List<String>>>(t);
        }
	}



    class NE
    {
    	private boolean      allAttributes;
    	private Set<String>  selectAttributes;
    	
    	public NE(String attributes)
    	{
    		allAttributes = (attributes == null || attributes.trim().length() == 0 || "*".equals(attributes));
    		if (allAttributes == false)
    			selectAttributes = new HashSet<String>(Arrays.asList(attributes.split(",")));
    	}
    			
    	
        void goOverEnumeration(Attribute a, Map<String,List<String>> map)
        throws Throwable
        {
        	String               attributeName = a.getID();
            NamingEnumeration<?> ne            = a.getAll();
            while (ne.hasMore())
            {
                Object aa = ne.next();
                if (aa instanceof Attribute)
                    goOverEnumeration((Attribute)aa,map);
                else if (aa instanceof String)
                {
                	if (allAttributes == false && selectAttributes.contains(attributeName) == false)
                		continue;
                    List<String> list = map.get(attributeName);
                    if (list == null)
                    {
                        list = new ArrayList<String>();
                        map.put(attributeName,list);
                    }
                    list.add((String)aa);
                }
            }
        }
    }


	
	
	@Reference
	public void setConnector(ILDAPConnector connector)
	{
		this.connector = connector;
	}
}
