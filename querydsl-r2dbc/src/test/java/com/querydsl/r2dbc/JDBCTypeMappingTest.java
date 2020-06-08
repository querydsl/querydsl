/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.r2dbc;

import com.querydsl.core.testutil.ReportingOnly;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class JDBCTypeMappingTest {

    private JDBCTypeMapping typeMapping = new JDBCTypeMapping();

    @Test
    public void get() {
        assertEquals(Float.class, typeMapping.get(Types.FLOAT, 0, 0));
        assertEquals(Float.class, typeMapping.get(Types.REAL, 0, 0));
    }

    @Test
    public void stringTypes() {
        assertEquals(String.class, typeMapping.get(Types.CHAR, 0, 0));
        assertEquals(String.class, typeMapping.get(Types.NCHAR, 0, 0));
        assertEquals(String.class, typeMapping.get(Types.CLOB, 0, 0));
        assertEquals(String.class, typeMapping.get(Types.NCLOB, 0, 0));
        assertEquals(String.class, typeMapping.get(Types.LONGVARCHAR, 0, 0));
        assertEquals(String.class, typeMapping.get(Types.LONGNVARCHAR, 0, 0));
        assertEquals(String.class, typeMapping.get(Types.SQLXML, 0, 0));
        assertEquals(String.class, typeMapping.get(Types.VARCHAR, 0, 0));
        assertEquals(String.class, typeMapping.get(Types.NVARCHAR, 0, 0));
    }

    @Test
    public void blobTypes() {
        assertEquals(Blob.class, typeMapping.get(Types.BLOB, 0, 0));
    }

    @Test
    public void bytesTypes() {
        assertEquals(byte[].class, typeMapping.get(Types.BINARY, 0, 0));
        assertEquals(byte[].class, typeMapping.get(Types.VARBINARY, 0, 0));
        assertEquals(byte[].class, typeMapping.get(Types.LONGVARBINARY, 0, 0));
    }

    @Test
    public void numericTypes() {
//        19,0       -> BigInteger
//        10-18,0    -> Long
//        5-9,0      -> Integer
//        3-4,0      -> Short
//        1-2,0      -> Byte

//        ?,?   -> BigDecimal
        assertEquals(typeMapping.get(Types.NUMERIC, 20, 0), BigInteger.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 19, 0), BigInteger.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 15, 0), Long.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 6, 0), Integer.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 5, 0), Integer.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 4, 0), Short.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 3, 0), Short.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 2, 0), Byte.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 1, 0), Byte.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 0, 0), BigInteger.class);

        assertEquals(typeMapping.get(Types.NUMERIC, 17, 2), BigDecimal.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 5, 2), BigDecimal.class);
    }

    @Test
    @Category(ReportingOnly.class)
    public void max() {
        System.err.println("Byte: " + String.valueOf(Byte.MAX_VALUE).length());
        System.err.println("Short: " + String.valueOf(Short.MAX_VALUE).length());
        System.err.println("Integer: " + String.valueOf(Integer.MAX_VALUE).length());
        System.err.println("Long: " + String.valueOf(Long.MAX_VALUE).length());
    }

    @Test
    public void numericOverriden() {
        typeMapping.registerNumeric(19, 0, BigInteger.class);
        assertEquals(typeMapping.get(Types.NUMERIC, 19, 0), BigInteger.class);
    }

    @Test
    public void numericOverriden2() {
        typeMapping.registerNumeric(19, 0, BigInteger.class);
        assertEquals(typeMapping.get(Types.INTEGER, 19, 0), BigInteger.class);
        assertEquals(typeMapping.get(Types.INTEGER, 18, 0), Integer.class);
    }

    @Test
    public void numericOverriden3() {
        typeMapping.registerNumeric(5, 2, BigDecimal.class);
        assertEquals(typeMapping.get(Types.DOUBLE, 5, 2), BigDecimal.class);
        assertEquals(typeMapping.get(Types.DOUBLE, 5, 1), Double.class);
    }

}
