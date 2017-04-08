package test.io.github.scottbrand.common.cloner;

import java.util.Date;



public class SourceObject
{
    int       primitiveInt;
    long      primitiveLong;
    Date      aDate;
    String    aString;
    DummyEnum anEnum;
    Integer   objectInt;
    Long      objectLong;



    public SourceObject()
    {
        
    }

    public SourceObject(int primitiveInt, long primitiveLong, Date aDate, String aString, DummyEnum anEnum, Integer objectInt, Long objectLong)
    {
        super();
        this.primitiveInt = primitiveInt;
        this.primitiveLong = primitiveLong;
        this.aDate = aDate;
        this.aString = aString;
        this.anEnum = anEnum;
        this.objectInt = objectInt;
        this.objectLong = objectLong;
    }





    public int getPrimitiveInt()
    {
        return primitiveInt;
    }





    public void setPrimitiveInt(int primitiveInt)
    {
        this.primitiveInt = primitiveInt;
    }





    public long getPrimitiveLong()
    {
        return primitiveLong;
    }





    public void setPrimitiveLong(long primitiveLong)
    {
        this.primitiveLong = primitiveLong;
    }





    public Date getaDate()
    {
        return aDate;
    }





    public void setaDate(Date aDate)
    {
        this.aDate = aDate;
    }





    public String getaString()
    {
        return aString;
    }





    public void setaString(String aString)
    {
        this.aString = aString;
    }





    public DummyEnum getAnEnum()
    {
        return anEnum;
    }





    public void setAnEnum(DummyEnum anEnum)
    {
        this.anEnum = anEnum;
    }





    public Integer getObjectInt()
    {
        return objectInt;
    }





    public void setObjectInt(Integer objectInt)
    {
        this.objectInt = objectInt;
    }





    public Long getObjectLong()
    {
        return objectLong;
    }





    public void setObjectLong(Long objectLong)
    {
        this.objectLong = objectLong;
    }

}
