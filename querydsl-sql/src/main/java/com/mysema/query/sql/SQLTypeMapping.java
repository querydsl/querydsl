/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * SQLTypeMapping defines a mapping from JDBC types to Java classes.
 * 
 * @author tiwe
 *
 */
public final class SQLTypeMapping {
    
    private final Map<Integer, Class<?>> sqlToJavaType = new HashMap<Integer, Class<?>>();
    
    {        
        // BOOLEAN
        sqlToJavaType.put(Types.BIT, Boolean.class);
        sqlToJavaType.put(Types.BOOLEAN, Boolean.class);

        // NUMERIC
        sqlToJavaType.put(Types.BIGINT, Long.class);
        sqlToJavaType.put(Types.DECIMAL, BigDecimal.class);
        sqlToJavaType.put(Types.DOUBLE, Double.class);
        sqlToJavaType.put(Types.FLOAT, Float.class);
        sqlToJavaType.put(Types.INTEGER, Integer.class);
        sqlToJavaType.put(Types.NUMERIC, BigDecimal.class);
        sqlToJavaType.put(Types.REAL, Float.class);
        sqlToJavaType.put(Types.SMALLINT, Short.class);
        sqlToJavaType.put(Types.TINYINT, Byte.class);
        
        // DATE and TIME
        sqlToJavaType.put(Types.DATE, java.util.Date.class);
        sqlToJavaType.put(Types.TIME, Time.class);
        sqlToJavaType.put(Types.TIMESTAMP, java.util.Date.class);

        // TEXT
        sqlToJavaType.put(Types.CHAR, Character.class);
        sqlToJavaType.put(Types.NCHAR, Character.class);
        sqlToJavaType.put(Types.CLOB, String.class);
        sqlToJavaType.put(Types.NCLOB, String.class);        
        sqlToJavaType.put(Types.LONGVARCHAR, String.class);
        sqlToJavaType.put(Types.LONGNVARCHAR, String.class);
        sqlToJavaType.put(Types.SQLXML, String.class);
        sqlToJavaType.put(Types.VARCHAR, String.class);
        sqlToJavaType.put(Types.NVARCHAR, String.class);

        // OTHER
        sqlToJavaType.put(Types.ARRAY, Object[].class);
        sqlToJavaType.put(Types.BINARY, Object.class);
        sqlToJavaType.put(Types.BLOB, Object.class);
        sqlToJavaType.put(Types.DISTINCT, Object.class);
        sqlToJavaType.put(Types.DATALINK, Object.class);
        sqlToJavaType.put(Types.JAVA_OBJECT, Object.class);
        sqlToJavaType.put(Types.LONGVARBINARY, Object.class);
        sqlToJavaType.put(Types.NULL, Object.class);        
        sqlToJavaType.put(Types.OTHER, Object.class);        
        sqlToJavaType.put(Types.REAL, Object.class);
        sqlToJavaType.put(Types.REF, Object.class);
        sqlToJavaType.put(Types.ROWID, Object.class);
        sqlToJavaType.put(Types.STRUCT, Object.class);
        sqlToJavaType.put(Types.VARBINARY, Object.class);        
    }

    @Nullable
    public Class<?> get(int sqlType) {
        return sqlToJavaType.get(sqlType);
    }

}
