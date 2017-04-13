package test.io.github.scottbrand.common.jpa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.github.scottbrand.common.Strings;



@Entity
@Table(name = "USERS")
public class User implements Serializable
{
	private static final long	serialVersionUID	= 1982003570779723545L;

	// user_id integer NOT NULL DEFAULT
	// nextval('gvpmdd.user_user_id_seq'::regclass),
	// logon_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
	// user_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
	// email_address character varying(255) COLLATE pg_catalog."default" NOT
	// NULL,
	// creator_user_id integer NOT NULL,
	// create_ts timestamp without time zone NOT NULL,
	// last_logon_ts timestamp without time zone,
	// soft_del_cd smallint NOT NULL,

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	Long						id;

	@Column(name = "LOGON_ID")
	String						logonID;

	@Column(name = "USER_NAME")
	String						name;
	
	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;





	public Long getId()
	{
		return id;
	}





	public void setId(Long id)
	{
		this.id = id;
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


	
	
	
	



	public String getEmailAddress()
	{
		return emailAddress;
	}





	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}





	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}





	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	
	public String toString()
	{
		return Strings.toString(this);
	}
	
}
