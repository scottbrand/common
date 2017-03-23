package io.github.scottbrand.webservice.jwt;

import java.io.Serializable;

import io.github.scottbrand.common.Strings;



public class UserData implements Serializable
{
    public static final String USER_ID          = "UID";
    public static final String NAME             = "NAME";
    public static final String LOGON_ID         = "LID";
    public static final String REMOTE_IP        = "RIP";

    private static final long  serialVersionUID = -8266269351994506415L;

    Long                       userID;

    String                     logonID;

    String                     name;

    String                     remoteIPAddress;





    public UserData(Long userID, String logonID, String name, String remoteIPAddress)
    {
        this.userID = userID;
        this.logonID = logonID;
        this.name = name;
        this.remoteIPAddress = remoteIPAddress;
    }





    public Long getUserID()
    {
        return userID;
    }





    public void setUserID(Long userID)
    {
        this.userID = userID;
    }





    public String getLogonID()
    {
        return logonID;
    }





    public void setLogonID(String logonID)
    {
        this.logonID = logonID;
    }





    public String getName()
    {
        return name;
    }





    public void setName(String name)
    {
        this.name = name;
    }





    public String getRemoteIPAddress()
    {
        return remoteIPAddress;
    }





    public void setRemoteIPAddress(String remoteIPAddress)
    {
        this.remoteIPAddress = remoteIPAddress;
    }





    public String toString()
    {
        return Strings.toString(this);
    }

}
