package com.mysema.query.sql;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.sql.Types;

import org.junit.Test;

import com.mysema.query.sql.types.InputStreamType;

public class InputStreamTypeUsageTest {

    @Test
    public void test(){
        Configuration configuration = new Configuration(new MySQLTemplates());
        configuration.setType(Types.BLOB, InputStream.class);
        configuration.register(new InputStreamType());        
        assertEquals(InputStream.class, configuration.getJavaType(Types.BLOB, "", ""));
    }
    
}
