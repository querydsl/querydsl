/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import com.mysema.query.serialization.OperationPatterns;
import com.mysema.query.types.operation.OperatorImpl;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PathMetadata;
import com.mysema.query.types.path.PathType;

/**
 * JavaOps extends OperationPatterns to add Java syntax specific operation
 * templates.
 * 
 * @author tiwe
 * @version $Id$
 */
public class JavaPatterns extends OperationPatterns {

    public static final JavaPatterns DEFAULT = new JavaPatterns();

    protected JavaPatterns() {
        String functions = JavaPatterns.class.getName();

        add(Ops.AFTER, "%s.compareTo(%s) > 0");
        add(Ops.BEFORE, "%s.compareTo(%s) < 0");
        add(Ops.AOE, "%s.compareTo(%s) >= 0");
        add(Ops.BOE, "%s.compareTo(%s) <= 0");

        add(Ops.BETWEEN, functions + ".between(%s,%s,%s)");
        add(Ops.NOTBETWEEN, "!" + functions + ".between(%s,%s,%s)");

        add(Ops.EQ_PRIMITIVE, "%s == %s");
        add(Ops.EQ_OBJECT, "%s.equals(%s)");
        add(Ops.NE_OBJECT, "!%s.equals(%s)");

        add(Ops.ISNULL, "%s == null");
        add(Ops.ISNOTNULL, "%s != null");

        add(Ops.ISTYPEOF, "%2$s.isInstance(%1$s)");        
        add(Ops.LIKE, functions + ".like(%s,%s)");
        
        // collection
        add(Ops.COL_ISEMPTY, "%s.isEmpty()");
        add(Ops.COL_ISNOTEMPTY, "!%s.isEmpty()");
        add(Ops.COL_SIZE, "%s.size()");
        add(Ops.IN, "%2$s.contains(%1$s)");
        add(Ops.NOTIN, "!%2$s.contains(%1$s)");

        // array
        add(Ops.ARRAY_SIZE, "%s.length");
        
        // map
        add(Ops.MAP_ISEMPTY, "%s.isEmpty()");
        add(Ops.MAP_ISNOTEMPTY, "!%s.isEmpty()");
        add(Ops.CONTAINS_KEY, "%s.containsKey(%s)");
        add(Ops.CONTAINS_VALUE, "%s.containsValue(%s)");

        // java.lang.String
        add(Ops.CHAR_AT, "%s.charAt(%s)");
        add(Ops.LOWER, "%s.toLowerCase()");
        add(Ops.SPLIT, "%s.split(%s)");
        add(Ops.SUBSTR1ARG, "%s.substring(%s)");
        add(Ops.SUBSTR2ARGS, "%s.substring(%s,%s)");
        add(Ops.TRIM, "%s.trim()");
        add(Ops.UPPER, "%s.toUpperCase()");
        add(Ops.MATCHES, "%s.matches(%s)");
        add(Ops.STRING_LENGTH, "%s.length(%s)");
        add(Ops.LAST_INDEX_2ARGS, "%s.lastIndex(%s)");
        add(Ops.LAST_INDEX, "%s.lastIndex(%s,%s)");
        add(Ops.STRING_ISEMPTY, "%s.isEmpty()");
        add(Ops.STARTSWITH, "%s.startsWith(%s, 0)");
        add(Ops.STARTSWITH_IC, "%s.toLowerCase().startsWith(%s.toLowerCase(), 0)");
        add(Ops.INDEXOF_2ARGS, "%s.indexOf(%s,%s)");
        add(Ops.INDEXOF, "%s.indexOf(%s)");
        add(Ops.EQ_IGNORECASE, "%s.equalsIgnoreCase(%s)");
        add(Ops.ENDSWITH, "%s.endsWith(%s)");
        add(Ops.ENDSWITH_IC, "%s.toLowerCase().endsWith(%s.toLowerCase())");
        add(Ops.CONTAINS, "%s.contains(%s)");
        
        // math
        try {
            for (Field f : Ops.Math.class.getFields()) {
                OperatorImpl<?> op = (OperatorImpl<?>) f.get(null);
                add(op, "Math." + getPattern(op));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        add(Ops.MOD, "%s %% %s");

        // path types
        for (PathType type : new PathType[] { PathType.LISTVALUE_CONSTANT }) {
            add(type, "%s.get(%s.intValue())");
        }

        // path types
        for (PathType type : new PathType[] { PathType.LISTVALUE,
                PathType.LISTVALUE_CONSTANT, PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "%s.get(%s)");
        }
        add(PathType.ARRAYVALUE, "%s[%s]");
        add(PathType.ARRAYVALUE_CONSTANT, "%s[%s.intValue()]");
    }

    public static <A extends Comparable<? super A>> boolean between(A a, A b, A c) {
        return a.compareTo(b) > 0 && a.compareTo(c) < 0;
    }

    public static boolean like(String source, String pattern) {
        return Pattern.compile(pattern.replace("%", ".*").replace("_", ".")).matcher(source).matches();
    }

}
