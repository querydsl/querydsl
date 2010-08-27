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
 * SQLTypeMapping defines a mapping from JDBC types to Java classes.
 *
 * @author tiwe
 *
 */
public final class SQLTypeMapping {

    private final Map<Integer, Class<?>> types = new HashMap<Integer, Class<?>>();

    {
        // BOOLEAN
        register(Types.BIT, Boolean.class);
        register(Types.BOOLEAN, Boolean.class);

        // NUMERIC
        register(Types.BIGINT, Long.class);
        register(Types.DECIMAL, BigDecimal.class);
        register(Types.DOUBLE, Double.class);
        register(Types.FLOAT, Float.class);
        register(Types.INTEGER, Integer.class);
        register(Types.NUMERIC, BigDecimal.class);
        register(Types.REAL, Float.class);
        register(Types.SMALLINT, Short.class);
        register(Types.TINYINT, Byte.class);

        // DATE and TIME
        register(Types.DATE, java.sql.Date.class);
        register(Types.TIME, java.sql.Time.class);
        register(Types.TIMESTAMP, java.util.Date.class);

        // TEXT
        register(Types.CHAR, String.class);
        register(Types.NCHAR, String.class);
        register(Types.CLOB, String.class);
        register(Types.NCLOB, String.class);
        register(Types.LONGVARCHAR, String.class);
        register(Types.LONGNVARCHAR, String.class);
        register(Types.SQLXML, String.class);
        register(Types.VARCHAR, String.class);
        register(Types.NVARCHAR, String.class);

        // OTHER
        register(Types.ARRAY, Object[].class);
        register(Types.BINARY, Object.class);
        register(Types.BLOB, Object.class);
        register(Types.DISTINCT, Object.class);
        register(Types.DATALINK, Object.class);
        register(Types.JAVA_OBJECT, Object.class);
        register(Types.LONGVARBINARY, Object.class);
        register(Types.NULL, Object.class);
        register(Types.OTHER, Object.class);
        register(Types.REAL, Object.class);
        register(Types.REF, Object.class);
        register(Types.ROWID, Object.class);
        register(Types.STRUCT, Object.class);
        register(Types.VARBINARY, Object.class);
    }
    
    public void register(int sqlType, Class<?> javaType){
        types.put(sqlType, javaType);
    }

    @Nullable
    public Class<?> get(int sqlType) {
        return types.get(sqlType);
    }

}
