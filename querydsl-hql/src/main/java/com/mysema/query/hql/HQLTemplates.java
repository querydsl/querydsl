/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.Templates;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PathType;

/**
 * HQLPatterns extends OperationPatterns to provide operator patterns for HQL
 * serialization
 * 
 * @author tiwe
 * @version $Id$
 */
public class HQLTemplates extends Templates {

    public static final List<Operator<?>> wrapCollectionsForOp;

    static {
        wrapCollectionsForOp = Collections.<Operator<?>> unmodifiableList(Arrays.<Operator<?>> asList(
            Ops.IN, 
            Ops.QuantOps.ALL, 
            Ops.QuantOps.ANY,
            Ops.QuantOps.AVG_IN_COL, 
            Ops.QuantOps.EXISTS, 
            Ops.QuantOps.NOTEXISTS));
    }

    public HQLTemplates() {
        // boolean
        add(Ops.AND, "{0} and {1}", 36);
        add(Ops.NOT, "not {0}", 3);
        add(Ops.OR, "{0} or {1}", 38);
        add(Ops.BooleanOps.XNOR, "{0} xnor {1}", 39);
        add(Ops.BooleanOps.XOR, "{0} xor {1}", 39);

        // comparison
        add(Ops.BETWEEN, "{0} between {1} and {2}", 30);

        // numeric
        add(Ops.MathOps.SQRT, "sqrt({0})");

        // various        
        add(Ops.ISNULL, "{0} is null", 26);
        add(Ops.ISNOTNULL, "{0} is not null", 26);
        
        // collection
        add(Ops.IN, "{0} in {1}");
        add(Ops.COL_ISEMPTY, "{0} is empty");
        add(Ops.COL_SIZE, "{0}.size");
        
        // string
        add(Ops.CONCAT, "{0} || {1}", 37);
        add(Ops.MATCHES, "{0} like {1}", 27); // FIXME limited regex functionality
        add(Ops.LOWER, "lower({0})");
        add(Ops.SUBSTR1ARG, "substring({0},{1})");
        add(Ops.SUBSTR2ARGS, "substring({0},{1},{2})");
        add(Ops.TRIM, "trim({0})");
        add(Ops.UPPER, "upper({0})");
        add(Ops.EQ_IGNORECASE, "lower({0}) = lower({1})");
        add(Ops.CHAR_AT, "cast(substring({0},{1}+1,1) as char)");
        add(Ops.STRING_CONTAINS, "locate({1},{0}) > 0");
        add(Ops.ENDSWITH, "locate({0},{1}) > -1"); // FIXME
        add(Ops.ENDSWITH_IC, "locate(lower({0}),lower({1})) > -1"); // FIXME
        add(Ops.STARTSWITH, "locate({0},{1}) = 0");
        add(Ops.STARTSWITH_IC, "locate(lower({0}),lower({1})) = 0");
        add(Ops.INDEXOF, "locate({0},{1})");
        add(Ops.INDEXOF_2ARGS, "locate({0},{1},{2})");
        add(Ops.STRING_ISEMPTY, "length({0}) = 0");

        // date time
        add(Ops.DateTimeOps.SYSDATE, "sysdate");
        add(Ops.DateTimeOps.CURRENT_DATE, "current_date()");
        add(Ops.DateTimeOps.CURRENT_TIME, "current_time()");
        add(Ops.DateTimeOps.CURRENT_TIMESTAMP, "current_timestamp()");
        add(Ops.DateTimeOps.SECOND, "second({0})");
        add(Ops.DateTimeOps.MINUTE, "minute({0})");
        add(Ops.DateTimeOps.HOUR, "hour({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "day({0})");
        add(Ops.DateTimeOps.MONTH, "month({0})");
        add(Ops.DateTimeOps.YEAR, "year({0})");

        // path types
        for (PathType type : new PathType[] { 
                PathType.LISTVALUE,
                PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "{0}[{1}]");
        }
        add(PathType.LISTVALUE_CONSTANT, "{0}[{1s}]");        
        add(PathType.PROPERTY, "{0}.{1s}");        
        add(PathType.VARIABLE, "{0s}");

    }
    
}
