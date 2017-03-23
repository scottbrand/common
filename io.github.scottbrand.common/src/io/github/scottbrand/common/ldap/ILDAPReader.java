package io.github.scottbrand.common.ldap;

import java.util.List;
import java.util.Map;

import io.github.scottbrand.common.TypedResult;

public interface ILDAPReader
{
    /**
     * The readData method will read the LDAP directory for the 
     * given userID (logonID), and return the requested attributes.
     * Only matching attributes are returned.
     * To return all associated attributes for the given logonID
     * set the attributes parameter to "*"
     * 
     * @param logonID
     * @param attributes
     * 
     * @return Map keyed by the attribute name and at each name a List of attribute values
     */
    TypedResult<Map<String,List<String>>>   readData(String logonID, String attributes);
	
	
    /**
     * The findUsers method will read the LDAP directory for the 
     * given surName, and return the requested attributes.
     * Only matching attributes are returned.
     * To return all associated attributes for the given surName
     * set the attributes parameter to "*"
     * 
     * @param logonID
     * @param attributes
     * 
     * @return Map keyed by the attribute name and at each name a List of attribute values
     */
    TypedResult<Map<String,List<String>>>   findUsers(String surNamePattern, String attributes);
    

}
