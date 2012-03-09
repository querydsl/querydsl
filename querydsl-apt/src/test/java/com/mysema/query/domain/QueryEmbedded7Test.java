package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class QueryEmbedded7Test {

    @QueryEntity
    static class Entity {
        
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
        assertEquals(StringPath.class, QQueryEmbedded7Test_Entity.entity.users.any().getClass());
        assertEquals(NumberPath.class, QQueryEmbedded7Test_Entity.entity.productRoles.any().getClass());
    }
}
