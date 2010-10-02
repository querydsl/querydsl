/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.EntityType;

public class DefaultNamingStrategyTest {

    private NamingStrategy namingStrategy = new DefaultNamingStrategy();

    @Test
    public void GetClassName() {
        assertEquals("QUserData", namingStrategy.getClassName("Q", "user_data"));
        assertEquals("QU", namingStrategy.getClassName("Q", "u"));
        assertEquals("QUs",namingStrategy.getClassName("Q", "us"));
        assertEquals("QU", namingStrategy.getClassName("Q", "u_"));
        assertEquals("QUs",namingStrategy.getClassName("Q", "us_"));
    }

    @Test
    public void GetPropertyName() {
        EntityType entityModel = new EntityType("Q", Types.OBJECT);
        assertEquals("whileCol", namingStrategy.getPropertyName("while", "Q", entityModel));
        assertEquals("name", namingStrategy.getPropertyName("name", "Q", entityModel));
        assertEquals("userId", namingStrategy.getPropertyName("user_id", "Q", entityModel));
        assertEquals("accountEventId", namingStrategy.getPropertyName("accountEvent_id", "Q", entityModel));
    }

    @Test
    public void GetPropertyNameForInverseForeignKey(){
        EntityType entityModel = new EntityType("Q", Types.OBJECT);
        assertEquals("_superiorFk", namingStrategy.getPropertyNameForInverseForeignKey("fk_superior", entityModel));
    }
    
    @Test
    public void GetPropertyNameForForeignKey(){
        EntityType entityModel = new EntityType("Q", Types.OBJECT);
        assertEquals("superiorFk", namingStrategy.getPropertyNameForForeignKey("fk_superior", entityModel));
        assertEquals("superiorFk", namingStrategy.getPropertyNameForForeignKey("FK_SUPERIOR", entityModel));        
    }
    
}
