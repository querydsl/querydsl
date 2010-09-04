/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import java.sql.Types;

import org.junit.Test;

import com.mysema.query.alias.AliasTest.Gender;
import com.mysema.query.sql.types.EnumByNameType;
import com.mysema.query.sql.types.StringType;

public class ConfigurationTest {
    
    SQLTemplates templates = new H2Templates();
    Configuration configuration = new Configuration(templates);
    
    @Test
    public void test(){        
        configuration.setType(Types.DATE, java.util.Date.class);
        configuration.setType("person", "secureId", new EncryptedString());
        configuration.setType("person", "gender",  new EnumByNameType<Gender>(Gender.class));
        configuration.register(new StringType());
        
        assertEquals(Gender.class, configuration.getJavaType(java.sql.Types.VARCHAR, "person", "gender"));
    }
    
}
