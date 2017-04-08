package test.io.github.scottbrand.common.jpa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class Platform implements Serializable
{
	@Id
	Long   id;
	
	@Column
	String name;
	
	@Column
	Integer  version;
	
//	@Column(name="CREATE_TS")
//	@Temporal(TemporalType.TIMESTAMP)
//	Date createTS;
	
//	@Column(name="UPDATE_TS")
//	@Temporal(TemporalType.TIMESTAMP)
//	Date updateTS;
	
	//@Override
	public Long getID()
	{
		return id;
	}





	//@Override
	public String getName()
	{
		return name;
	}





	//@Override
	public Integer getVersion()
	{
		return version;
	}





	//@Override
//	public Date getCreationTS()
//	{
//		return createTS;
//	}





	//@Override
//	public Date getUpdatedTS()
//	{
//		return updateTS;
//	}





	//@Override
	public void setID(Long id)
	{
		if (this.id == null && id != null)
		{
			this.id = id;
		}
	}





	//@Override
	public void setName(String name)
	{
		this.name = name;
	}





	//@Override
	public void setVersion(Integer version)
	{
		if (this.version == null && version != null)
		{
			this.version = version;
		}
	}





	//@Override
//	public void setCreationTS(Timestamp ts)
//	{
//		this.createTS = ts;
//	}





	//@Override
//	public void setUpdatedTS(Timestamp ts)
//	{
//		this.updateTS = ts;
//	}
	
	

}
