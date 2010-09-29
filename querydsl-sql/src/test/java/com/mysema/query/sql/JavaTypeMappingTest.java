package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.mysema.query.sql.types.BlobType;
import com.mysema.query.sql.types.InputStreamType;

public class JavaTypeMappingTest {

    private JavaTypeMapping typeMapping = new JavaTypeMapping();
    
    @Test
    public void GetType_With_Subtypes() {        
        typeMapping.register(new InputStreamType());
        assertNotNull(typeMapping.getType(InputStream.class));
        assertNotNull(typeMapping.getType(FileInputStream.class));
    }
    
    @Test
    public void GetType_With_Interfaces(){
        assertEquals(BlobType.class, typeMapping.getType(DummyBlob.class).getClass());
    }

}
