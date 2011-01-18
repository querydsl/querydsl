package com.mysema.query.alias;

import static com.mysema.query.alias.Alias.$;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.EntityPath;

public class CollectionTest {
    
    @Test
    public void CollectionUsage(){
        DomainType domainType = Alias.alias(DomainType.class);
        assertEquals("any(domainType.collection) = domainType", $(domainType.getCollection()).any().eq(domainType).toString());
        assertEquals("any(domainType.set) = domainType", $(domainType.getSet()).any().eq(domainType).toString());
        assertEquals("any(domainType.list) = domainType", $(domainType.getList()).any().eq(domainType).toString());
        assertEquals("domainType.list.get(0) = domainType", $(domainType.getList().get(0)).eq(domainType).toString());
        assertEquals("domainType.list.get(0) = domainType", $(domainType.getList()).get(0).eq(domainType).toString());
        assertEquals("domainType.map.get(key) = domainType", $(domainType.getMap()).get("key").eq(domainType).toString());
        
        EntityPath<DomainType> domainTypePath = $(domainType);
        assertEquals("domainType in domainType.collection", $(domainType.getCollection()).contains(domainTypePath).toString());
    }
    
    @Test
    public void CollectionUsage_Types(){
        DomainType domainType = Alias.alias(DomainType.class);
        assertEquals(DomainType.class, $(domainType.getCollection()).any().getType());
        assertEquals(DomainType.class, $(domainType.getSet()).any().getType());
        assertEquals(DomainType.class, $(domainType.getList()).any().getType());
        assertEquals(DomainType.class, $(domainType.getMap()).get("key").getType());
    }
    

}
