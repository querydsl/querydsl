/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.alias;

import static com.mysema.query.alias.Alias.$;
import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.types.Path;

public class AliasTest {

    @Test
    public void Alias(){
        DomainType domainType = Alias.alias(DomainType.class);
        Alias.alias(DomainType.class, $(domainType.getCollection()).any());
    }
    
    @Test
    @Ignore // FIXME
    public void ComparableEntity() {
       ComparableEntity entity = Alias.alias(ComparableEntity.class);
       Path<ComparableEntity> path = $(entity);
       assertEquals(ComparableEntity.class, path.getType());
    }
    
    @Test
    public void BasicUsage(){
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
    public void GetAny(){
        DomainType domainType = Alias.alias(DomainType.class);
        assertEquals(DomainType.class, Alias.getAny(domainType).getType());
        assertEquals(String.class, Alias.getAny(domainType.getFirstName()).getType());
    }

    @Test
    public void OtherMethods(){
        DomainType domainType = Alias.alias(DomainType.class);
        assertEquals("domainType", domainType.toString());
    }

    @Test
    public void Var(){
        assertEquals("it", Alias.var().toString());
        assertEquals("varInteger1", Alias.var(1).toString());
        assertEquals("X", Alias.var("X").toString());
        assertEquals("varMALE", Alias.var(Gender.MALE).toString());
        assertEquals("varAliasTest_XXX", Alias.var(new AliasTest()).toString());
    }
    
    @Override
    public String toString(){
        return "XXX";
    }
}
