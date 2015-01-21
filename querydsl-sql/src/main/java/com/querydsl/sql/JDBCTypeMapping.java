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

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.mysema.commons.lang.Pair;
import com.querydsl.sql.types.Null;

/**
 * JDBCTypeMapping defines a mapping from JDBC types to Java classes.
 *
 * @author tiwe
 *
 */
public final class JDBCTypeMapping {

    private static final Map<Integer, Class<?>> defaultTypes = new HashMap<Integer, Class<?>>();

    private static final Map<Class<?>, Integer> defaultSqlTypes = new HashMap<Class<?>, Integer>();

    static{
        registerDefault(-101, Object.class);
        registerDefault(-102, java.sql.Timestamp.class); // Oracle: TIMESTAMP(6) WITH LOCAL TIME ZONE
        registerDefault(2012, Object.class); // REF_CURSOR
        registerDefault(2013, Time.class);   // TIME_WITH_TIMEZONE
        registerDefault(2014, Timestamp.class); // TIMESTAMP_WIH_TIMEZONE

        // BOOLEAN
        registerDefault(Types.BIT, Boolean.class);
        registerDefault(Types.BOOLEAN, Boolean.class);

        // NUMERIC
        registerDefault(Types.BIGINT, Long.class);
        registerDefault(Types.DECIMAL, BigDecimal.class);
        registerDefault(Types.DOUBLE, Double.class);
        registerDefault(Types.FLOAT, Float.class);
        registerDefault(Types.INTEGER, Integer.class);
        registerDefault(Types.NUMERIC, BigDecimal.class);
        registerDefault(Types.REAL, Float.class);
        registerDefault(Types.SMALLINT, Short.class);
        registerDefault(Types.TINYINT, Byte.class);

        // DATE and TIME
        registerDefault(Types.DATE, java.sql.Date.class);
        registerDefault(Types.TIME, java.sql.Time.class);
        registerDefault(Types.TIMESTAMP, java.sql.Timestamp.class);

        // TEXT
        registerDefault(Types.NCHAR, String.class);
        registerDefault(Types.CHAR, String.class);
        registerDefault(Types.NCLOB, String.class);
        registerDefault(Types.CLOB, String.class);
        registerDefault(Types.LONGNVARCHAR, String.class);
        registerDefault(Types.LONGVARCHAR, String.class);
        registerDefault(Types.SQLXML, String.class);
        registerDefault(Types.NVARCHAR, String.class);
        registerDefault(Types.VARCHAR, String.class);

        // byte[]
        registerDefault(Types.BINARY, byte[].class);
        registerDefault(Types.LONGVARBINARY, byte[].class);
        registerDefault(Types.VARBINARY, byte[].class);

        // BLOB
        registerDefault(Types.BLOB, Blob.class);

        // OTHER
        registerDefault(Types.ARRAY, Object[].class);
        registerDefault(Types.DISTINCT, Object.class);
        registerDefault(Types.DATALINK, Object.class);
        registerDefault(Types.JAVA_OBJECT, Object.class);
        registerDefault(Types.NULL, Null.class);
        registerDefault(Types.OTHER, Object.class);
        registerDefault(Types.REF, Object.class);
        registerDefault(Types.ROWID, Object.class);
        registerDefault(Types.STRUCT, Object.class);
    }

    private static void registerDefault(int sqlType, Class<?> javaType) {
        defaultTypes.put(sqlType, javaType);
        defaultSqlTypes.put(javaType, sqlType);
    }

    private final Map<Integer, Class<?>> types = new HashMap<Integer, Class<?>>();

    private final Map<Class<?>, Integer> sqlTypes = new HashMap<Class<?>, Integer>();

    private final Map<Pair<Integer,Integer>, Class<?>> numericTypes = new HashMap<Pair<Integer,Integer>, Class<?>>();

    public void register(int sqlType, Class<?> javaType) {
        types.put(sqlType, javaType);
        sqlTypes.put(javaType, sqlType);
    }

    public void registerNumeric(int total, int decimal, Class<?> javaType) {
        numericTypes.put(Pair.of(total, decimal), javaType);
    }

    private Class<?> getNumericClass(int total, int decimal) {
        Pair<Integer,Integer> key = Pair.of(total, decimal);
        if (numericTypes.containsKey(key)) {
            return numericTypes.get(key);
        } else if (decimal <= 0) {
            if (total > 18 || total == 0) {
                return BigInteger.class;
            } else if (total > 9 || total == 0) {
                return Long.class;
            } else if (total > 4) {
                return Integer.class;
            } else if (total > 2) {
                return Short.class;
            } else if (total > 0) {
                return Byte.class;
            } else {
                return Boolean.class;
            }
        } else {
            if (total > 16) {
                return BigDecimal.class;
            } else {
                return Double.class;
            }
        }
    }

    @Nullable
    public Class<?> get(int sqlType, int total, int decimal) {
        if (sqlType == Types.NUMERIC || sqlType == Types.DECIMAL) {
            return getNumericClass(total, decimal);
        } else if (types.containsKey(sqlType)) {
            return types.get(sqlType);
        } else {
            return defaultTypes.get(sqlType);
        }
    }

    @Nullable
    public Integer get(Class<?> clazz) {
        if (sqlTypes.containsKey(clazz)) {
            return sqlTypes.get(clazz);
        } else {
            return defaultSqlTypes.get(clazz);
        }
    }

}
