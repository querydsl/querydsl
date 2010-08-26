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
        types.put(Types.BIT, Boolean.class);
        types.put(Types.BOOLEAN, Boolean.class);

        // NUMERIC
        types.put(Types.BIGINT, Long.class);
        types.put(Types.DECIMAL, BigDecimal.class);
        types.put(Types.DOUBLE, Double.class);
        types.put(Types.FLOAT, Float.class);
        types.put(Types.INTEGER, Integer.class);
        types.put(Types.NUMERIC, BigDecimal.class);
        types.put(Types.REAL, Float.class);
        types.put(Types.SMALLINT, Short.class);
        types.put(Types.TINYINT, Byte.class);

        // DATE and TIME
        types.put(Types.DATE, java.sql.Date.class);
        types.put(Types.TIME, java.sql.Time.class);
        types.put(Types.TIMESTAMP, java.util.Date.class);

        // TEXT
        types.put(Types.CHAR, String.class);
        types.put(Types.NCHAR, String.class);
        types.put(Types.CLOB, String.class);
        types.put(Types.NCLOB, String.class);
        types.put(Types.LONGVARCHAR, String.class);
        types.put(Types.LONGNVARCHAR, String.class);
        types.put(Types.SQLXML, String.class);
        types.put(Types.VARCHAR, String.class);
        types.put(Types.NVARCHAR, String.class);

        // OTHER
        types.put(Types.ARRAY, Object[].class);
        types.put(Types.BINARY, Object.class);
        types.put(Types.BLOB, Object.class);
        types.put(Types.DISTINCT, Object.class);
        types.put(Types.DATALINK, Object.class);
        types.put(Types.JAVA_OBJECT, Object.class);
        types.put(Types.LONGVARBINARY, Object.class);
        types.put(Types.NULL, Object.class);
        types.put(Types.OTHER, Object.class);
        types.put(Types.REAL, Object.class);
        types.put(Types.REF, Object.class);
        types.put(Types.ROWID, Object.class);
        types.put(Types.STRUCT, Object.class);
        types.put(Types.VARBINARY, Object.class);
    }

    @Nullable
    public Class<?> get(int sqlType) {
        return types.get(sqlType);
    }

}
