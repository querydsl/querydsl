/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

/**
 * Templates provides operator patterns for query expression serialization
 *
 * @author tiwe
 * @version $Id$
 */
@Immutable
public class Templates {

    private static final TemplateFactory templateFactory = new TemplateFactory();

    public static final Templates DEFAULT = new Templates();

    private final Map<Operator<?>, Template> templates = new HashMap<Operator<?>, Template>();

    private final Map<Operator<?>, Integer> precedence = new HashMap<Operator<?>, Integer>();

    protected Templates() {
        //CHECKSTYLE:OFF

        add(Ops.LIST, "{0}, {1}");

        // boolean
        add(Ops.AND, "{0} && {1}", 36);
        add(Ops.NOT, "!{0}", 3);
        add(Ops.OR, "{0} || {1}", 38);
        add(Ops.XNOR, "{0} xnor {1}", 39);
        add(Ops.XOR, "{0} xor {1}", 39);

        // collection
        add(Ops.COL_IS_EMPTY, "empty({0})");
        add(Ops.COL_SIZE, "size({0})");

        // array
        add(Ops.ARRAY_SIZE, "size({0})");

        // map
        add(Ops.MAP_SIZE, "size({0})");
        add(Ops.MAP_ISEMPTY, "empty({0})");
        add(Ops.CONTAINS_KEY, "containsKey({0},{1})");
        add(Ops.CONTAINS_VALUE, "containsValue({0},{1})");

        // comparison
        add(Ops.BETWEEN, "{0} between {1} and {2}", 30);
        add(Ops.GOE, "{0} >= {1}", 20);
        add(Ops.GT, "{0} > {1}", 21);
        add(Ops.LOE, "{0} <= {1}", 22);
        add(Ops.LT, "{0} < {1}", 23);

        add(Ops.AFTER, "{0} > {1}", 21);
        add(Ops.BEFORE, "{0} < {1}", 23);
        add(Ops.AOE, "{0} >= {1}", 21);
        add(Ops.BOE, "{0} <= {1}", 23);

        // numeric
        add(Ops.ADD, "{0} + {1}", 13);
        add(Ops.DIV, "{0} / {1}", 8);
        add(Ops.MOD, "{0} % {1}", 10);
        add(Ops.MULT, "{0} * {1}", 7);
        add(Ops.SUB, "{0} - {1}", 12);

        // various
        add(Ops.EQ_PRIMITIVE, "{0} = {1}", 18);
        add(Ops.EQ_OBJECT, "{0} = {1}", 18);
        add(Ops.EQ_IGNORE_CASE, "eqIc({0},{1})", 18);
        add(Ops.INSTANCE_OF, "{0}.class = {1}");
        add(Ops.NE_PRIMITIVE, "{0} != {1}", 25);
        add(Ops.NE_OBJECT, "{0} != {1}", 25);
        add(Ops.IN, "{0} in {1}");
        add(Ops.IS_NULL, "{0} is null", 26);
        add(Ops.IS_NOT_NULL, "{0} is not null", 26);
        add(Ops.ALIAS, "{0} as {1}");

        add(Ops.EXISTS, "exists({0})");

        add(Ops.NUMCAST, "cast({0},{1})");
        add(Ops.STRING_CAST, "str({0})");

        // string
        add(Ops.CONCAT, "{0} + {1}", 37);
        add(Ops.LOWER, "lower({0})");
        add(Ops.SUBSTR_1ARG, "substring({0},{1})");
        add(Ops.SUBSTR_2ARGS, "substring({0},{1},{2})");
        add(Ops.TRIM, "trim({0})");
        add(Ops.UPPER, "upper({0})");
        add(Ops.MATCHES, "matches({0},{1})");
        add(Ops.STARTS_WITH, "startsWith({0},{1})");
        add(Ops.STARTS_WITH_IC, "startsWithIgnoreCase({0},{1})");
        add(Ops.ENDS_WITH, "endsWith({0},{0}");
        add(Ops.ENDS_WITH_IC, "endsWithIgnoreCase({0},{1})");
        add(Ops.STRING_CONTAINS, "contains({0},{1})");
        add(Ops.STRING_CONTAINS_IC, "containsIc({0},{1})");
        add(Ops.CHAR_AT, "charAt({0},{1})");
        add(Ops.STRING_LENGTH, "length({0})");
        add(Ops.INDEX_OF, "indexOf({0},{1})");
        add(Ops.INDEX_OF_2ARGS, "indexOf({0},{1},{2})");
        add(Ops.STRING_IS_EMPTY, "empty({0})");
        add(Ops.LIKE, "{0} like {1}");

        add(Ops.StringOps.LTRIM, "ltrim({0})");
        add(Ops.StringOps.RTRIM, "rtrim({0})");
        add(Ops.StringOps.SPACE, "space({0})");
        add(Ops.StringOps.LAST_INDEX, "lastIndexOf({0},{1})");
        add(Ops.StringOps.LAST_INDEX_2ARGS, "lastIndexOf({0},{1},{2})");
        add(Ops.StringOps.SPLIT, "split({0},{1})");

        // date time
        add(Ops.DateTimeOps.SYSDATE, "sysdate");
        add(Ops.DateTimeOps.CURRENT_DATE, "current_date()");
        add(Ops.DateTimeOps.CURRENT_TIME, "current_time()");
        add(Ops.DateTimeOps.CURRENT_TIMESTAMP, "current_timestamp()");
        add(Ops.DateTimeOps.SECOND, "second({0})");
        add(Ops.DateTimeOps.MILLISECOND, "millisecond({0})");
        add(Ops.DateTimeOps.MINUTE, "minute({0})");
        add(Ops.DateTimeOps.HOUR, "hour({0})");
        add(Ops.DateTimeOps.WEEK, "week({0})");
        add(Ops.DateTimeOps.MONTH, "month({0})");
        add(Ops.DateTimeOps.YEAR, "year({0})");
        add(Ops.DateTimeOps.YEAR_MONTH, "yearMonth({0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "dayofweek({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "dayofmonth({0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "dayofyear({0})");

        // math
        add(Ops.MathOps.ABS, "abs({0})");
        add(Ops.MathOps.ACOS, "acos({0})");
        add(Ops.MathOps.ASIN, "asin({0})");
        add(Ops.MathOps.ATAN, "atan({0})");
        add(Ops.MathOps.CEIL, "ceil({0})");
        add(Ops.MathOps.COS, "cos({0})");
        add(Ops.MathOps.TAN, "tan({0})");
        add(Ops.MathOps.SQRT, "sqrt({0})");
        add(Ops.MathOps.SIN, "sin({0})");
        add(Ops.MathOps.ROUND, "round({0})");
        add(Ops.MathOps.RANDOM, "random()");
        add(Ops.MathOps.POWER, "pow({0},{1})");
        add(Ops.MathOps.MIN, "min({0},{1})");
        add(Ops.MathOps.MAX, "max({0},{1})");
        add(Ops.MathOps.LOG10, "log10({0})");
        add(Ops.MathOps.LOG, "log({0})");
        add(Ops.MathOps.FLOOR, "floor({0})");
        add(Ops.MathOps.EXP, "exp({0})");

        // path types
        add(PathType.DELEGATE, "{0}");
        add(PathType.PROPERTY, "{0}.{1s}");
        add(PathType.VARIABLE, "{0s}");
        add(Ops.ORDINAL, "ordinal({0})");

        for (PathType type : new PathType[] {
                PathType.LISTVALUE,
                PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "{0}.get({1})");
        }
        add(PathType.ARRAYVALUE, "{0}[{1}]");
        add(PathType.LISTVALUE_CONSTANT, "{0}.get({1s})"); // serialized constant
        add(PathType.ARRAYVALUE_CONSTANT, "{0}[{1s}]");    // serialized constant

        // case
        add(Ops.CASE, "case {0} end");
        add(Ops.CASE_WHEN,  "when {0} then {1} {2}");
        add(Ops.CASE_ELSE,  "else {0}");

        // case for
        add(Ops.CASE_EQ, "case {0} {1} end");
        add(Ops.CASE_EQ_WHEN,  "when {1} then {2} {3}");
        add(Ops.CASE_EQ_ELSE,  "else {0}");

        // coalesce
        add(Ops.COALESCE, "coalesce({0})");

        // subquery
        add(Ops.EXISTS, "exists {0}");

        // numeric aggregates
        add(Ops.AggOps.AVG_AGG, "avg({0})");
        add(Ops.AggOps.MAX_AGG, "max({0})");
        add(Ops.AggOps.MIN_AGG, "min({0})");
        add(Ops.AggOps.SUM_AGG, "sum({0})");
        add(Ops.AggOps.COUNT_AGG, "count({0})");
        add(Ops.AggOps.COUNT_DISTINCT_AGG, "count(distinct {0})");
        add(Ops.AggOps.COUNT_ALL_AGG, "count(*)");

        // quantified expressions
        add(Ops.QuantOps.AVG_IN_COL, "avg({0})");
        add(Ops.QuantOps.MAX_IN_COL, "max({0})");
        add(Ops.QuantOps.MIN_IN_COL, "min({0})");

        add(Ops.QuantOps.ANY, "any {0}");
        add(Ops.QuantOps.ALL, "all {0}");
        //CHECKSTYLE:ON
    }

    protected final void add(Operator<?> op, String pattern) {
        Template template = templateFactory.create(pattern);
        templates.put(op, template);
    }

    protected final void add(Operator<?> op, String pattern, int pre) {
        add(op, pattern);
        precedence.put(op, pre);
    }

    @Nullable
    public Template getTemplate(Operator<?> op) {
        return templates.get(op);
    }

    public int getPrecedence(Operator<?> operator) {
        if (precedence.containsKey(operator)) {
            return precedence.get(operator);
        } else {
            return -1;
        }
    }

}
