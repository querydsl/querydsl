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
package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Types;

import org.junit.Test;

public class JDBCTypeMappingTest {

    private JDBCTypeMapping typeMapping = new JDBCTypeMapping();

    @Test
    public void Get() {
        assertEquals(Float.class, typeMapping.get(Types.FLOAT,0,0));
        assertEquals(Float.class, typeMapping.get(Types.REAL,0,0));
    }

    @Test
    public void StringTypes() {
        assertEquals(String.class, typeMapping.get(Types.CHAR,0,0));
        assertEquals(String.class, typeMapping.get(Types.NCHAR,0,0));
        assertEquals(String.class, typeMapping.get(Types.CLOB,0,0));
        assertEquals(String.class, typeMapping.get(Types.NCLOB,0,0));
        assertEquals(String.class, typeMapping.get(Types.LONGVARCHAR,0,0));
        assertEquals(String.class, typeMapping.get(Types.LONGNVARCHAR,0,0));
        assertEquals(String.class, typeMapping.get(Types.SQLXML,0,0));
        assertEquals(String.class, typeMapping.get(Types.VARCHAR,0,0));
        assertEquals(String.class, typeMapping.get(Types.NVARCHAR,0,0));
    }

    @Test
    public void BlobTypes() {
        assertEquals(Blob.class, typeMapping.get(Types.BLOB,0,0));
    }

    @Test
    public void BytesTypes() {
        assertEquals(byte[].class, typeMapping.get(Types.BINARY,0,0));
        assertEquals(byte[].class, typeMapping.get(Types.VARBINARY,0,0));
        assertEquals(byte[].class, typeMapping.get(Types.LONGVARBINARY,0,0));
    }

    @Test
    public void NumericTypes() {
//        19,0       -> BigInteger
//        10-18,0    -> Long
//        5-9,0      -> Integer
//        3-4,0      -> Short
//        2,0        -> Byte
//        0-1,0      -> Boolean

//        17-...,?   -> BigDecimal
//        0-16,?     -> Double
        assertEquals(typeMapping.get(Types.NUMERIC, 20, 0), BigInteger.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 19, 0), BigInteger.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 15, 0), Long.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 6, 0),  Integer.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 5, 0),  Integer.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 4, 0),  Short.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 3, 0),  Short.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 2, 0),  Byte.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 1, 0),  Byte.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 0, 0),  BigInteger.class);

        assertEquals(typeMapping.get(Types.NUMERIC, 17, 2), BigDecimal.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 5, 2),  Double.class);
    }

    @Test
    public void Max() {
        System.err.println("Byte: " + String.valueOf(Byte.MAX_VALUE).length());
        System.err.println("Short: " + String.valueOf(Short.MAX_VALUE).length());
        System.err.println("Integer: " + String.valueOf(Integer.MAX_VALUE).length());
        System.err.println("Long: " + String.valueOf(Long.MAX_VALUE).length());
    }

    @Test
    public void NumericOverriden() {
        typeMapping.registerNumeric(19, 0, BigInteger.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 19, 0), BigInteger.class);
    }

}
