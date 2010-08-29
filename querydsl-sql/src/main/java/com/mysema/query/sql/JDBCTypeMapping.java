/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * JDBCTypeMapping defines a mapping from JDBC types to Java classes.
 *
 * @author tiwe
 *
 */
public final class JDBCTypeMapping {

    private static final Map<Integer, Class<?>> defaultTypes = new HashMap<Integer, Class<?>>();
    
    static{
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
        registerDefault(Types.TIMESTAMP, java.util.Date.class);

        // TEXT
        registerDefault(Types.CHAR, String.class);
        registerDefault(Types.NCHAR, String.class);
        registerDefault(Types.CLOB, String.class);
        registerDefault(Types.NCLOB, String.class);
        registerDefault(Types.LONGVARCHAR, String.class);
        registerDefault(Types.LONGNVARCHAR, String.class);
        registerDefault(Types.SQLXML, String.class);
        registerDefault(Types.VARCHAR, String.class);
        registerDefault(Types.NVARCHAR, String.class);

        // OTHER
        registerDefault(Types.ARRAY, Object[].class);
        registerDefault(Types.BINARY, Object.class);
        registerDefault(Types.BLOB, Object.class);
        registerDefault(Types.DISTINCT, Object.class);
        registerDefault(Types.DATALINK, Object.class);
        registerDefault(Types.JAVA_OBJECT, Object.class);
        registerDefault(Types.LONGVARBINARY, Object.class);
        registerDefault(Types.NULL, Object.class);
        registerDefault(Types.OTHER, Object.class);
        registerDefault(Types.REAL, Object.class);
        registerDefault(Types.REF, Object.class);
        registerDefault(Types.ROWID, Object.class);
        registerDefault(Types.STRUCT, Object.class);
        registerDefault(Types.VARBINARY, Object.class);
    }
    
    private static void registerDefault(int sqlType, Class<?> javaType){
        defaultTypes.put(sqlType, javaType);
    }
    
    private final Map<Integer, Class<?>> types = new HashMap<Integer, Class<?>>();
    
    public void register(int sqlType, Class<?> javaType){
        types.put(sqlType, javaType);
    }

    @Nullable
    public Class<?> get(int sqlType) {
        if (types.containsKey(sqlType)){
            return types.get(sqlType);
        }else{
            return defaultTypes.get(sqlType);
        }        
    }

}
