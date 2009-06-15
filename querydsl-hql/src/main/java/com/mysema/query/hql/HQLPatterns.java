/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.serialization.OperationPatterns;
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
public class HQLPatterns extends OperationPatterns {

    public static final HQLPatterns DEFAULT = new HQLPatterns();

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

    public HQLPatterns() {
        // boolean
        add(Ops.AND, "%s and %s", 36);
        add(Ops.NOT, "not %s", 3);
        add(Ops.OR, "%s or %s", 38);
        add(Ops.BooleanOps.XNOR, "%s xnor %s", 39);
        add(Ops.BooleanOps.XOR, "%s xor %s", 39);

        // comparison
        add(Ops.BETWEEN, "%s between %s and %s", 30);
//        add(Ops.NOTBETWEEN, "%s not between %s and %s", 30);

        // numeric
        add(Ops.MathOps.SQRT, "sqrt(%s)");

        // various        
        add(Ops.ISNULL, "%s is null", 26);
        add(Ops.ISNOTNULL, "%s is not null", 26);
        
        // collection
        add(Ops.IN, "%s in %s");
//        add(Ops.NOTIN, "%s not in %s");
        add(Ops.COL_ISEMPTY, "%s is empty");
//        add(Ops.COL_ISNOTEMPTY, "%s is not empty");
        add(Ops.COL_SIZE, "%s.size");
        
        // string
        add(Ops.CONCAT, "%s || %s", 37);
        add(Ops.MATCHES, "%s like %s", 27); // FIXME limited regex functionality
        add(Ops.LOWER, "lower(%s)");
        add(Ops.SUBSTR1ARG, "substring(%s,%s)");
        add(Ops.SUBSTR2ARGS, "substring(%s,%s,%s)");
        add(Ops.TRIM, "trim(%s)");
        add(Ops.UPPER, "upper(%s)");
        add(Ops.EQ_IGNORECASE, "lower(%s) = lower(%s)");
        add(Ops.CHAR_AT, "cast(substring(%s,%s+1,1) as char)");
        add(Ops.STRING_CONTAINS, "locate(%s,%s) > -1");
        add(Ops.ENDSWITH, "locate(%s,%s) > -1"); // FIXME
        add(Ops.ENDSWITH_IC, "locate(lower(%s),lower(%s)) > -1"); // FIXME
        add(Ops.STARTSWITH, "locate(%s,%s) = 0");
        add(Ops.STARTSWITH_IC, "locate(lower(%s),lower(%s)) = 0");
        add(Ops.INDEXOF, "locate(%s,%s)");
        add(Ops.INDEXOF_2ARGS, "locate(%s,%s,%s)");
        add(Ops.STRING_ISEMPTY, "length(%s) = 0");
//        add(Ops.STRING_ISNOTEMPTY, "length(%s) > 0");

        // date time
        add(Ops.DateTimeOps.SYSDATE, "sysdate");
        add(Ops.DateTimeOps.CURRENT_DATE, "current_date()");
        add(Ops.DateTimeOps.CURRENT_TIME, "current_time()");
        add(Ops.DateTimeOps.CURRENT_TIMESTAMP, "current_timestamp()");
        add(Ops.DateTimeOps.SECOND, "second(%s)");
        add(Ops.DateTimeOps.MINUTE, "minute(%s)");
        add(Ops.DateTimeOps.HOUR, "hour(%s)");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "day(%s)");
        add(Ops.DateTimeOps.MONTH, "month(%s)");
        add(Ops.DateTimeOps.YEAR, "year(%s)");

        // path types
        for (PathType type : new PathType[] { PathType.LISTVALUE,
                PathType.LISTVALUE_CONSTANT, PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "%s[%s]");
        }
        add(PathType.PROPERTY, "%s.%s");
        
        add(PathType.VARIABLE, "%s");

    }
    
}
