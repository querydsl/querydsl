/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import java.lang.reflect.Field;

import com.mysema.query.types.operation.OperatorImpl;
import com.mysema.query.types.operation.Ops;


public class JavaPatterns extends OperationPatterns {
    
    public static final JavaPatterns DEFAULT = new JavaPatterns();

    public JavaPatterns() {
        add(Ops.EQ_PRIMITIVE, "%s == %s");
        add(Ops.EQ_OBJECT, "%s == %s");
        add(Ops.ISNULL, "%s == null");
        add(Ops.ISNOTNULL, "%s != null");
        add(Ops.INSTANCEOF, "%s instanceof %s");

        // collection
        add(Ops.IN, "%2$s.contains(%1$s)");
        add(Ops.COL_ISEMPTY, "%s.isEmpty()");
        add(Ops.COL_SIZE, "%s.size()");
        
        // array
        add(Ops.ARRAY_SIZE, "%s.length");
        
        // map
        add(Ops.MAP_ISEMPTY, "%s.isEmpty()");
        add(Ops.MAP_SIZE, "%s.size()");
        add(Ops.CONTAINS_KEY, "%s.containsKey(%s)");
        add(Ops.CONTAINS_VALUE, "%s.containsValue(%s)");
                
        // Comparable
        add(Ops.BETWEEN, "%2$s < %1$s && %1$s < %3$s");
        
        // String
        add(Ops.CHAR_AT, "%s.charAt(%s)");
        add(Ops.LOWER, "%s.toLowerCase()");        
        add(Ops.SUBSTR1ARG, "%s.substring(%s)");
        add(Ops.SUBSTR2ARGS, "%s.substring(%s,%s)");
        add(Ops.TRIM, "%s.trim()");
        add(Ops.UPPER, "%s.toUpperCase()");
        add(Ops.MATCHES, "%s.matches(%s)");
        add(Ops.STRING_LENGTH, "%s.length()");        
        add(Ops.STRING_ISEMPTY, "%s.isEmpty()");
        add(Ops.STRING_CONTAINS, "%s.contains(%s)");
        add(Ops.STARTSWITH, "%s.startsWith(%s)");
        add(Ops.STARTSWITH_IC, "%s.toLowerCase().startsWith(%s.toLowerCase())");        
        add(Ops.INDEXOF, "%s.indexOf(%s)");
        add(Ops.INDEXOF_2ARGS, "%s.indexOf(%s,%s)");
        add(Ops.EQ_IGNORECASE, "%s.equalsIgnoreCase(%s)");
        add(Ops.ENDSWITH, "%s.endsWith(%s)");
        add(Ops.ENDSWITH_IC, "%s.toLowerCase().endsWith(%s.toLowerCase())");
        add(Ops.StringOps.SPLIT, "%s.split(%s)");
        add(Ops.StringOps.LAST_INDEX, "%s.lastIndexOf(%s)");
        add(Ops.StringOps.LAST_INDEX_2ARGS, "%s.lastIndexOf(%s,%s)");
        
        // Date and Time
        add(Ops.DateTimeOps.DAY_OF_MONTH, "%s.getDayOfMonth()");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "%s.getDayOfWeek()");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "%s.getDayOfYear()");
        add(Ops.DateTimeOps.HOUR, "%s.getHour()");
        add(Ops.DateTimeOps.MINUTE, "%s.getMinute()");
        add(Ops.DateTimeOps.MONTH, "%s.getMonth()");
        add(Ops.DateTimeOps.SECOND, "%s.getSecond()");
        add(Ops.DateTimeOps.WEEK, "%s.getWeek()");
        add(Ops.DateTimeOps.YEAR, "%s.getYear()");

        // Math
        try {
            for (Field f : Ops.MathOps.class.getFields()) {
                OperatorImpl<?> op = (OperatorImpl<?>) f.get(null);
                add(op, "Math." + getPattern(op));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        add(Ops.MOD, "%s %% %s");
        
    }

}
