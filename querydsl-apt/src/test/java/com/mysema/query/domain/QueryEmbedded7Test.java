package com.mysema.query.domain;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class QueryEmbedded7Test {

    @QueryEntity
    class Entity {
        
        @QueryEmbedded
        Collection<String> users;

        @QueryEmbedded
        Set<Long> productRoles;
        
    }

    @Test
    public void test() {
        assertEquals(StringPath.class, QQueryEmbedded7Test_Entity.entity.users.any().getClass());
        assertEquals(NumberPath.class, QQueryEmbedded7Test_Entity.entity.productRoles.any().getClass());
    }
}
