/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PathType;

/**
 * OperationPatterns provides operator patterns for SQL/HQL serialization
 * 
 * @author tiwe
 * @version $Id$
 */
// TODO : replace String.format based expressions with custom expressions
public class OperationPatterns {

    private final Map<Operator<?>, String> patterns = new HashMap<Operator<?>, String>();

    private final Map<Operator<?>, Integer> precedence = new HashMap<Operator<?>, Integer>();

    public OperationPatterns() {
        // boolean
        add(Ops.AND, "%s && %s", 36);
        add(Ops.NOT, "!%s", 3);
        add(Ops.OR, "%s || %s", 38);
        add(Ops.XNOR, "%s xnor %s", 39);
        add(Ops.XOR, "%s xor %s", 39);

        // collection
        add(Ops.COL_ISEMPTY, "empty(%s)");
        add(Ops.COL_ISNOTEMPTY, "not empty(%s)");
        add(Ops.COL_SIZE, "%s.size");
        
        // array
        add(Ops.ARRAY_SIZE, "empty(%s)");
        
        // map
        add(Ops.MAP_ISEMPTY, "empty(%s)");
        add(Ops.MAP_ISNOTEMPTY, "not empty(%s)");
        add(Ops.CONTAINS_KEY, "containsKey(%s,%s)");
        add(Ops.CONTAINS_VALUE, "containsValue(%s,%s)");
        
        // comparison
        add(Ops.BETWEEN, "%s between %s and %s", 30);
        add(Ops.NOTBETWEEN, "%s not between %s and %s", 30);
        add(Ops.GOE, "%s >= %s", 20);
        add(Ops.GT, "%s > %s", 21);
        add(Ops.LOE, "%s <= %s", 22);
        add(Ops.LT, "%s < %s", 23);

        add(Ops.AFTER, "%s > %s", 21);
        add(Ops.BEFORE, "%s < %s", 23);
        add(Ops.AOE, "%s >= %s", 21);
        add(Ops.BOE, "%s <= %s", 23);

        // numeric
        add(Ops.ADD, "%s + %s", 13);
        add(Ops.DIV, "%s / %s", 8);
        add(Ops.MOD, "%s % %s", 10);
        add(Ops.MULT, "%s * %s", 7);
        add(Ops.SUB, "%s - %s", 12);
        // add(Ops.SQRT, "sqrt(%s)");

        // various
        add(Ops.EQ_PRIMITIVE, "%s = %s", 18);
        add(Ops.EQ_OBJECT, "%s = %s", 18);
        add(Ops.EQ_IGNORECASE, "eqIc(%s,%s)", 18);
        add(Ops.INSTANCEOF, "%s.class = %s");
        add(Ops.NE_PRIMITIVE, "%s != %s", 25);
        add(Ops.NE_OBJECT, "%s != %s", 25);
        add(Ops.IN, "%s in %s");
        add(Ops.NOTIN, "%s not in %s");
        add(Ops.ISNULL, "%s is null", 26);
        add(Ops.ISNOTNULL, "%s is not null", 26);
        add(Ops.ALIAS, "%s as %s");
        
        add(Ops.EXISTS, "exists(%s)");

        add(Ops.NUMCAST, "cast(%s,%s)");
        add(Ops.STRING_CAST, "cast(%s,%s)");

        // string
        add(Ops.CONCAT, "%s + %s", 37);
        add(Ops.LIKE, "%s like %s", 27);
        add(Ops.LOWER, "lower(%s)");
        add(Ops.SUBSTR1ARG, "substring(%s,%s)");
        add(Ops.SUBSTR2ARGS, "substring(%s,%s,%s)");
        add(Ops.TRIM, "trim(%s)");
        add(Ops.UPPER, "upper(%s)");
        add(Ops.MATCHES, "like(%s,%s)");
        add(Ops.STARTSWITH, "startsWith(%s,%s)");
        add(Ops.STARTSWITH_IC, "startsWithIgnoreCase(%s,%s)");
        add(Ops.ENDSWITH, "endsWith(%s,%s");
        add(Ops.ENDSWITH_IC, "endsWithIgnoreCase(%s,%s");
        add(Ops.STRING_CONTAINS, "contains(%s,%s)");
        add(Ops.SPLIT, "split(%s,%s)");
        add(Ops.CHAR_AT, "charAt(%s,%s)");
        add(Ops.STRING_LENGTH, "length(%s)");
        add(Ops.INDEXOF, "indexOf(%s,%s)");
        add(Ops.INDEXOF_2ARGS, "indexOf(%s,%s,%s)");
        add(Ops.LAST_INDEX, "lastIndexOf(%s,%s)");
        add(Ops.LAST_INDEX_2ARGS, "lastIndexOf(%s,%s,%s)");
        add(Ops.STRING_ISEMPTY, "empty(%s)");

        // date time
        add(Ops.DateTimeOps.SYSDATE, "sysdate");
        add(Ops.DateTimeOps.CURRENT_DATE, "current_date()");
        add(Ops.DateTimeOps.CURRENT_TIME, "current_time()");
        add(Ops.DateTimeOps.CURRENT_TIMESTAMP, "current_timestamp()");
        add(Ops.DateTimeOps.SECOND, "second(%s)");
        add(Ops.DateTimeOps.MINUTE, "minute(%s)");
        add(Ops.DateTimeOps.HOUR, "hour(%s)");
        add(Ops.DateTimeOps.DAY, "day(%s)");
        add(Ops.DateTimeOps.WEEK, "week(%s)");
        add(Ops.DateTimeOps.MONTH, "month(%s)");
        add(Ops.DateTimeOps.YEAR, "year(%s)");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "dayofweek(%s)");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "dayofmonth(%s)");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "dayofyear(%s)");

        // math
        add(Ops.MathOps.ABS, "abs(%s)");
        add(Ops.MathOps.ACOS, "acos(%s)");
        add(Ops.MathOps.ASIN, "asin(%s)");
        add(Ops.MathOps.ATAN, "atan(%s)");
        add(Ops.MathOps.CEIL, "ceil(%s)");
        add(Ops.MathOps.COS, "cos(%s)");
        add(Ops.MathOps.TAN, "tan(%s)");
        add(Ops.MathOps.SQRT, "sqrt(%s)");
        add(Ops.MathOps.SIN, "sin(%s)");
        add(Ops.MathOps.ROUND, "round(%s)");
        add(Ops.MathOps.RANDOM, "random()");
        add(Ops.MathOps.POWER, "pow(%s,%s)");
        add(Ops.MathOps.MIN, "min(%s,%s)");
        add(Ops.MathOps.MAX, "max(%s,%s)");
        // add(Ops.OpMath.MOD,"mod(%s,%s)");
        add(Ops.MathOps.LOG10, "log10(%s)");
        add(Ops.MathOps.LOG, "log(%s)");
        add(Ops.MathOps.FLOOR, "floor(%s)");
        add(Ops.MathOps.EXP, "exp(%s)");

        // string
        add(Ops.StringOps.LTRIM, "ltrim(%s)");
        add(Ops.StringOps.RTRIM, "rtrim(%s)");
        add(Ops.StringOps.SPACE, "space(%s)");
        
        // path types
        for (PathType type : new PathType[] { PathType.LISTVALUE,
                PathType.LISTVALUE_CONSTANT, PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "%s.get(%s)");
        }
        add(PathType.PROPERTY, "%s.%s");
        add(PathType.VARIABLE, "%s");

        // numeric aggregates
        add(Ops.AVG_AGG, "avg(%s)");
        add(Ops.MAX_AGG, "max(%s)");
        add(Ops.MIN_AGG, "min(%s)");
        add(Ops.SUM_AGG, "sum(%s)");
        add(Ops.COUNT_AGG, "count(%s)");
        add(Ops.COUNT_ALL_AGG, "count(*)");
        

        // quantified expressions
        add(Ops.QuantOps.AVG_IN_COL, "avg(%s)");
        add(Ops.QuantOps.MAX_IN_COL, "max(%s)");
        add(Ops.QuantOps.MIN_IN_COL, "min(%s)");

        add(Ops.QuantOps.ANY, "any %s");
        add(Ops.QuantOps.ALL, "all %s");
        add(Ops.QuantOps.EXISTS, "exists %s");
        add(Ops.QuantOps.NOTEXISTS, "not exists %s");
    }

    protected void add(Operator<?> op, String pattern) {
        patterns.put(op, pattern);
    }

    protected void add(Operator<?> op, String pattern, int pre) {
        patterns.put(op, pattern);
        precedence.put(op, pre);
    }

    public String getPattern(Operator<?> op) {
        return patterns.get(op);
    }

    public int getPrecedence(Operator<?> operator) {
        if (precedence.containsKey(operator)) {
            return precedence.get(operator);
        } else {
            return -1;
        }
    }

    public OperationPatterns toUpperCase() {
        for (Map.Entry<Operator<?>, String> entry : patterns.entrySet()) {
            patterns.put(entry.getKey(), entry.getValue().toUpperCase());
        }
        return this;
    }

    public OperationPatterns toLowerCase() {
        for (Map.Entry<Operator<?>, String> entry : patterns.entrySet()) {
            patterns.put(entry.getKey(), entry.getValue().toLowerCase());
        }
        return this;
    }

}
