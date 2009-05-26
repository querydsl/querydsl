/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.util.Collection;

import com.mysema.query.serialization.OperationPatterns;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.operation.Ops.Op;
import com.mysema.query.types.path.PathMetadata;
import com.mysema.query.types.path.PathMetadata.PathType;

/**
 * Operation patterns for JDOQL serialization
 * 
 * @author tiwe
 * 
 */
public class JDOQLOps extends OperationPatterns {

    public static final JDOQLOps DEFAULT = new JDOQLOps();

    public JDOQLOps() {
        add(Ops.EQ_PRIMITIVE, "%s == %s");
        add(Ops.EQ_OBJECT, "%s == %s");
        add(Ops.NE_OBJECT, "%s != %s");
        add(Ops.ISNULL, "%s == null");
        add(Ops.ISNOTNULL, "%s != null");
        add(Ops.ISTYPEOF, "%s instanceof %s");

        // collection
        add(Ops.IN, "%2$s.contains(%1$s)");
        add(Ops.NOTIN, "!%2$s.contains(%1$s)");
        add(Ops.COL_ISEMPTY, "%s.isEmpty()");
        add(Ops.COL_ISNOTEMPTY, "!%s.isEmpty()");
        add(Ops.CONTAINS, "%s.contains(%s)");
        
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
        add(PathMetadata.VARIABLE, "%s");
        for (PathType type : new PathType[] { PathMetadata.LISTVALUE,
                PathMetadata.LISTVALUE_CONSTANT, PathMetadata.MAPVALUE,
                PathMetadata.MAPVALUE_CONSTANT }) {
            add(type, "%s.get(%s)");
        }
        add(PathMetadata.ARRAYVALUE, "%s[%s]");
        add(PathMetadata.ARRAYVALUE_CONSTANT, "%s[%s]");

        add(PathMetadata.ARRAY_SIZE, "%s.length");
        add(PathMetadata.SIZE, "%s.size()");

    }

    /**
     * The Interface OpHql.
     */
    public interface OpHql {
        Op<java.lang.Boolean> ISEMPTY = new Op<java.lang.Boolean>(Collection.class);
        Op<java.lang.Boolean> ISNOTEMPTY = new Op<java.lang.Boolean>(Collection.class);
        Op<Number> SUM = new Op<Number>(Number.class);
    }

    /**
     * The Interface OpQuant.
     */
    public interface OpQuant {
        Op<java.lang.Number> AVG_IN_COL = new Op<java.lang.Number>(Collection.class);
        Op<java.lang.Number> MAX_IN_COL = new Op<java.lang.Number>(Collection.class);
        Op<java.lang.Number> MIN_IN_COL = new Op<java.lang.Number>(Collection.class);

        // some / any = true for any
        // all = true for all
        // exists = true is subselect matches
        // not exists = true if subselect doesn't match
        Op<?> ANY = new Op<Object>(Object.class);
        Op<?> ALL = new Op<Object>(Object.class);
        Op<?> EXISTS = new Op<Object>(Object.class);
        Op<?> NOTEXISTS = new Op<Object>(Object.class);
    }

}
