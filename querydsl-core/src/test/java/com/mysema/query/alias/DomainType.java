/**
 * 
 */
package com.mysema.query.alias;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DomainType{

    String getFirstName();

    String getLastName();

    int getAge();

    List<DomainType> getList();
    
    Collection<DomainType> getCollection();
    
    Set<DomainType> getSet();

    Map<String,DomainType> getMap();

    BigDecimal getBigDecimal();

    BigInteger getBigInteger();

    Byte getByte();

    Double getDouble();

    Float getFloat();

    java.sql.Date getDate();

    java.util.Date getDate2();

    Short getShort();

    Time getTime();

    Timestamp getTimestamp();

    Gender getGender();

}