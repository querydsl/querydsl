package com.mysema.query.sql;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.mysema.query.sql.types.InputStreamType;

public class JavaTypeMappingTest {

    @Test
    public void GetType_With_Subtypes() {
        JavaTypeMapping typeMapping = new JavaTypeMapping();
        typeMapping.register(new InputStreamType());
        assertNotNull(typeMapping.getType(InputStream.class));
        assertNotNull(typeMapping.getType(FileInputStream.class));
    }

}
