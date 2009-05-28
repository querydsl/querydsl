/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import com.mysema.query.serialization.OperationPatterns;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PathType;

/**
 * Operation patterns for JDOQL serialization
 * 
 * @author tiwe
 * 
 */
public class JDOQLPatterns extends OperationPatterns {

    public static final JDOQLPatterns DEFAULT = new JDOQLPatterns();

    public JDOQLPatterns() {
        add(Ops.EQ_PRIMITIVE, "%s == %s");
        add(Ops.EQ_OBJECT, "%s == %s");
        add(Ops.ISNULL, "%s == null");
        add(Ops.ISNOTNULL, "%s != null");
        add(Ops.INSTANCEOF, "%s instanceof %s");

        // collection
        add(Ops.IN, "%2$s.contains(%1$s)");
        add(Ops.NOTIN, "!%2$s.contains(%1$s)");
        add(Ops.COL_ISEMPTY, "%s.isEmpty()");
        add(Ops.COL_ISNOTEMPTY, "!%s.isEmpty()");
        add(Ops.CONTAINS, "%s.contains(%s)");        
        add(Ops.COL_SIZE, "%s.size()");
        
        // array
        add(Ops.ARRAY_SIZE, "%s.length");
        
        // map
        add(Ops.MAP_ISEMPTY, "%s.isEmpty()");
        add(Ops.MAP_ISNOTEMPTY, "!%s.isEmpty()");
        add(Ops.CONTAINS_KEY, "%s.containsKey(%s)");
        add(Ops.CONTAINS_VALUE, "%s.containsValue(%s)");

        // comparable
        add(Ops.AFTER, "%s > %s");
        add(Ops.BEFORE, "%s < %s");
        add(Ops.AOE, "%s >= %s");
        add(Ops.BOE, "%s <= %s");

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
        add(Ops.STARTSWITH, "%s.startsWith(%s)");
        add(Ops.STARTSWITH_IC, "%s.toLowerCase().startsWith(%s.toLowerCase())");
        add(Ops.INDEXOF_2ARGS, "%s.indexOf(%s,%s)");
        add(Ops.INDEXOF, "%s.indexOf(%s)");
        add(Ops.EQ_IGNORECASE, "%s.equalsIgnoreCase(%s)");
        add(Ops.ENDSWITH, "%s.endsWith(%s)");
        add(Ops.ENDSWITH_IC, "%s.toLowerCase().endsWith(%s.toLowerCase())");
        
        
        // path types
        add(PathType.VARIABLE, "%s");
        for (PathType type : new PathType[] { 
                PathType.LISTVALUE,
                PathType.LISTVALUE_CONSTANT, 
                PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "%s.get(%s)");
        }
        add(PathType.ARRAYVALUE, "%s[%s]");
        add(PathType.ARRAYVALUE_CONSTANT, "%s[%s]");
    }

}
