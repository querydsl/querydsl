/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;

public class PropertyTest {

    @Test
    public void test(){
        Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false,false);
        EntityType type = new EntityType("Q","",  typeModel);
        Property p1 = new Property(type, "property", type, new String[0]);
        Property p2 = new Property(type, "property", type, new String[0]);
        assertEquals(p1, p1);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

}
