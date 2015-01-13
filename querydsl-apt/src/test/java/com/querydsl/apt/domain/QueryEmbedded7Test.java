package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;

public class QueryEmbedded7Test {

    @QueryEntity
    public static class Entity {
        
        @QueryEmbedded
        Collection<String> users;

        @QueryEmbedded
        Set<Long> productRoles;
        
        // misuse, but shouldn't cause problems
        @QueryEmbedded 
        Locale locale;

        // misuse, but shouldn't cause problems
        @QueryEmbedded
        String string;
        
    }

    @Test
    public void test() {
        Assert.assertEquals(StringPath.class, QQueryEmbedded7Test_Entity.entity.users.any().getClass());
        assertEquals(NumberPath.class, QQueryEmbedded7Test_Entity.entity.productRoles.any().getClass());
    }
}
