/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    }

    @Test
    public void BytesTypes() {
        JDBCTypeMapping typeMapping = new JDBCTypeMapping();       
        assertEquals(byte[].class, typeMapping.get(Types.BINARY));
        assertEquals(byte[].class, typeMapping.get(Types.VARBINARY));
        assertEquals(byte[].class, typeMapping.get(Types.LONGVARBINARY));
    }
}
