/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.sql.Types;

import org.junit.Test;

import com.mysema.query.alias.Gender;
import com.mysema.query.sql.types.EnumByNameType;
import com.mysema.query.sql.types.InputStreamType;
import com.mysema.query.sql.types.StringType;
import com.mysema.query.sql.types.UtilDateType;

public class ConfigurationTest {
    
    @Test
    public void Various(){
        Configuration configuration = new Configuration(new H2Templates());
//        configuration.setJavaType(Types.DATE, java.util.Date.class);
        configuration.register(new UtilDateType());
        configuration.register("person", "secureId", new EncryptedString());
        configuration.register("person", "gender",  new EnumByNameType<Gender>(Gender.class));
        configuration.register(new StringType());        
        assertEquals(Gender.class, configuration.getJavaType(java.sql.Types.VARCHAR, "person", "gender"));
    }
    
    @Test
    public void Custom_Type(){
        Configuration configuration = new Configuration(new H2Templates());
//        configuration.setJavaType(Types.BLOB, InputStream.class);
        configuration.register(new InputStreamType());        
        assertEquals(InputStream.class, configuration.getJavaType(Types.BLOB, "", ""));
    }
        
}
