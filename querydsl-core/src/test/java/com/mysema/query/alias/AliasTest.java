/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import static com.mysema.query.alias.Alias.$;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AliasTest {
    
    public enum Gender{
	MALE,
	FEMALE
    }
    
    public interface DomainType{
        
        String getFirstName();
        
        String getLastName();
        
        int getAge();
        
        List<DomainType> getList();
        
        Map<String,DomainType> getMap();
        
        BigDecimal getBigDecimal();
        
        BigInteger getBigInteger();
        
        Byte getByte();
        
        Collection<DomainType> getCollection();
        
        Double getDouble();
        
        Float getFloat();
        
        java.sql.Date getDate();
        
        java.util.Date getDate2();
        
        Set<DomainType> getSet();
        
        Short getShort();
        
        Time getTime();
        
        Timestamp getTimestamp();
        
        Gender getGender();
        
    }
    
    @Test
    public void basicUsage(){
        DomainType domainType = Alias.alias(DomainType.class);
        assertEquals("lower(domainType.firstName)", $(domainType.getFirstName()).lower().toString());
        assertEquals("domainType.age", $(domainType.getAge()).toString());
        assertEquals("domainType.map.get(a)", $(domainType.getMap().get("a")).toString());
        assertEquals("domainType.list.get(0)", $(domainType.getList().get(0)).toString());
        
        assertEquals("domainType.bigDecimal", $(domainType.getBigDecimal()).toString());
        assertEquals("domainType.bigInteger", $(domainType.getBigInteger()).toString());
        assertEquals("domainType.byte", $(domainType.getByte()).toString());
        assertEquals("domainType.collection", $(domainType.getCollection()).toString());
        assertEquals("domainType.double", $(domainType.getDouble()).toString());
        assertEquals("domainType.float", $(domainType.getFloat()).toString());
        assertEquals("domainType.date", $(domainType.getDate()).toString());
        assertEquals("domainType.date2", $(domainType.getDate2()).toString());
        assertEquals("domainType.set", $(domainType.getSet()).toString());
        assertEquals("domainType.short", $(domainType.getShort()).toString());
        assertEquals("domainType.time", $(domainType.getTime()).toString());
        assertEquals("domainType.timestamp", $(domainType.getTimestamp()).toString());
        assertEquals("domainType.gender", $(domainType.getGender()).toString());
    }
    
    @Test
    public void getAny(){
	DomainType domainType = Alias.alias(DomainType.class);
	assertEquals(DomainType.class, Alias.getAny(domainType).getType());
	assertEquals(String.class, Alias.getAny(domainType.getFirstName()).getType());
    }

    @Test
    public void otherMethods(){
	DomainType domainType = Alias.alias(DomainType.class);
	assertEquals("domainType", domainType.toString());	
    }
    
    @Test
    public void var(){
	assertEquals("it", Alias.var().toString());
	assertEquals("varInteger1", Alias.var(1).toString());
	assertEquals("X", Alias.var("X").toString());
    }
}
