package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import java.sql.Blob;
import java.sql.Types;

import org.junit.Test;

public class JDBCTypeMappingTest {

    @Test
    public void Get() {
        JDBCTypeMapping typeMapping = new JDBCTypeMapping();
        assertEquals(Float.class, typeMapping.get(Types.FLOAT));
        assertEquals(Float.class, typeMapping.get(Types.REAL));
    }
    
    @Test
    public void StringTypes(){
        JDBCTypeMapping typeMapping = new JDBCTypeMapping();
        assertEquals(String.class, typeMapping.get(Types.CHAR));
        assertEquals(String.class, typeMapping.get(Types.NCHAR));
        assertEquals(String.class, typeMapping.get(Types.CLOB));
        assertEquals(String.class, typeMapping.get(Types.NCLOB));
        assertEquals(String.class, typeMapping.get(Types.LONGVARCHAR));
        assertEquals(String.class, typeMapping.get(Types.LONGNVARCHAR));
        assertEquals(String.class, typeMapping.get(Types.SQLXML));
        assertEquals(String.class, typeMapping.get(Types.VARCHAR));
        assertEquals(String.class, typeMapping.get(Types.NVARCHAR));
    }
    
    @Test
    public void BlobTypes(){
        JDBCTypeMapping typeMapping = new JDBCTypeMapping();
        assertEquals(Blob.class, typeMapping.get(Types.BLOB));
        assertEquals(Blob.class, typeMapping.get(Types.BINARY));
        assertEquals(Blob.class, typeMapping.get(Types.VARBINARY));
        assertEquals(Blob.class, typeMapping.get(Types.LONGVARBINARY));
    }

}
