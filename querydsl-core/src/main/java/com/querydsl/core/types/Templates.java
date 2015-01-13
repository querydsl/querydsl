/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.types;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Templates provides operator patterns for querydsl expression serialization
 *
 * @author tiwe
 */
public class Templates {

    public static final Templates DEFAULT = new Templates();

    private final Map<Operator<?>, Template> templates = new IdentityHashMap<Operator<?>, Template>(150);

    private final Map<Operator<?>, Integer> precedence = new IdentityHashMap<Operator<?>, Integer>(150);

    private final TemplateFactory templateFactory;

    private final char escape;

    protected Templates() {
        this('\\');
    }

    protected Templates(char escape) {
        this.escape = escape;
        templateFactory = new TemplateFactory(escape) {
            public String escapeForLike(String str) {
                return Templates.this.escapeForLike(str);
            }
        };
        //CHECKSTYLE:OFF

        add(Ops.LIST, "{0}, {1}", 40);
        add(Ops.SINGLETON, "{0}", 40);
        add(Ops.WRAPPED, "({0})");

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
        add(Ops.MAP_IS_EMPTY, "empty({0})");
        add(Ops.CONTAINS_KEY, "containsKey({0},{1})");
        add(Ops.CONTAINS_VALUE, "containsValue({0},{1})");

        // comparison
        add(Ops.BETWEEN, "{0} between {1} and {2}", 30);
        add(Ops.GOE, "{0} >= {1}", 20);
        add(Ops.GT, "{0} > {1}", 21);
        add(Ops.LOE, "{0} <= {1}", 22);
        add(Ops.LT, "{0} < {1}", 23);

        // numeric
        add(Ops.NEGATE, "-{0}", 6);
        add(Ops.ADD, "{0} + {1}", 13);
        add(Ops.DIV, "{0} / {1}", 8);
        add(Ops.MOD, "{0} % {1}", 10);
        add(Ops.MULT, "{0} * {1}", 7);
        add(Ops.SUB, "{0} - {1}", 12);

        // various
        add(Ops.EQ, "{0} = {1}", 18);
        add(Ops.EQ_IGNORE_CASE, "eqIc({0},{1})", 18);
        add(Ops.INSTANCE_OF, "{0}.class = {1}");
        add(Ops.NE, "{0} != {1}", 25);
        add(Ops.IN, "{0} in {1}", 27);
        add(Ops.NOT_IN, "{0} not in {1}", 27);
        add(Ops.IS_NULL, "{0} is null", 26);
        add(Ops.IS_NOT_NULL, "{0} is not null", 26);
        add(Ops.ALIAS, "{0} as {1}", 0);

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
        add(Ops.MATCHES_IC, "matchesIgnoreCase({0},{1})");
        add(Ops.STARTS_WITH, "startsWith({0},{1})");
        add(Ops.STARTS_WITH_IC, "startsWithIgnoreCase({0},{1})");
        add(Ops.ENDS_WITH, "endsWith({0},{1})");
        add(Ops.ENDS_WITH_IC, "endsWithIgnoreCase({0},{1})");
        add(Ops.STRING_CONTAINS, "contains({0},{1})");
        add(Ops.STRING_CONTAINS_IC, "containsIc({0},{1})");
        add(Ops.CHAR_AT, "charAt({0},{1})");
        add(Ops.STRING_LENGTH, "length({0})");
        add(Ops.INDEX_OF, "indexOf({0},{1})");
        add(Ops.INDEX_OF_2ARGS, "indexOf({0},{1},{2})");
        add(Ops.STRING_IS_EMPTY, "empty({0})");
        add(Ops.LIKE, "{0} like {1}", 26);
        add(Ops.LIKE_ESCAPE, "{0} like {1} escape '{2s}'", 26);

        add(Ops.StringOps.LEFT, "left({0},{1})");
        add(Ops.StringOps.RIGHT, "right({0},{1})");
        add(Ops.StringOps.LTRIM, "ltrim({0})");
        add(Ops.StringOps.RTRIM, "rtrim({0})");
        add(Ops.StringOps.LOCATE, "locate({0},{1})");
        add(Ops.StringOps.LOCATE2, "locate({0},{1},{2s})");
        add(Ops.StringOps.LPAD, "lpad({0},{1})");
        add(Ops.StringOps.RPAD, "rpad({0},{1})");
        add(Ops.StringOps.LPAD2, "lpad({0},{1},'{2s}')");
        add(Ops.StringOps.RPAD2, "rpad({0},{1},'{2s}')");

        // date time
        add(Ops.DateTimeOps.SYSDATE, "sysdate");
        add(Ops.DateTimeOps.CURRENT_DATE, "current_date()");
        add(Ops.DateTimeOps.CURRENT_TIME, "current_time()");
        add(Ops.DateTimeOps.CURRENT_TIMESTAMP, "current_timestamp()");
        add(Ops.DateTimeOps.DATE, "date({0})");

        add(Ops.DateTimeOps.MILLISECOND, "millisecond({0})");
        add(Ops.DateTimeOps.SECOND, "second({0})");
        add(Ops.DateTimeOps.MINUTE, "minute({0})");
        add(Ops.DateTimeOps.HOUR, "hour({0})");
        add(Ops.DateTimeOps.WEEK, "week({0})");
        add(Ops.DateTimeOps.MONTH, "month({0})");
        add(Ops.DateTimeOps.YEAR, "year({0})");
        add(Ops.DateTimeOps.YEAR_MONTH, "yearMonth({0})");
        add(Ops.DateTimeOps.YEAR_WEEK, "yearweek({0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "dayofweek({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "dayofmonth({0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "dayofyear({0})");

        add(Ops.DateTimeOps.ADD_YEARS, "add_years({0},{1})");
        add(Ops.DateTimeOps.ADD_MONTHS, "add_months({0},{1})");
        add(Ops.DateTimeOps.ADD_WEEKS, "add_weeks({0},{1})");
        add(Ops.DateTimeOps.ADD_DAYS, "add_days({0},{1})");
        add(Ops.DateTimeOps.ADD_HOURS, "add_hours({0},{1})");
        add(Ops.DateTimeOps.ADD_MINUTES, "add_minutes({0},{1})");
        add(Ops.DateTimeOps.ADD_SECONDS, "add_seconds({0},{1})");

        add(Ops.DateTimeOps.DIFF_YEARS, "diff_years({0},{1})");
        add(Ops.DateTimeOps.DIFF_MONTHS, "diff_months({0},{1})");
        add(Ops.DateTimeOps.DIFF_WEEKS, "diff_weeks({0},{1})");
        add(Ops.DateTimeOps.DIFF_DAYS, "diff_days({0},{1})");
        add(Ops.DateTimeOps.DIFF_HOURS, "diff_hours({0},{1})");
        add(Ops.DateTimeOps.DIFF_MINUTES, "diff_minutes({0},{1})");
        add(Ops.DateTimeOps.DIFF_SECONDS, "diff_seconds({0},{1})");

        add(Ops.DateTimeOps.TRUNC_YEAR, "trunc_year({0})");
        add(Ops.DateTimeOps.TRUNC_MONTH, "trunc_month({0})");
        add(Ops.DateTimeOps.TRUNC_WEEK, "trunc_week({0})");
        add(Ops.DateTimeOps.TRUNC_DAY, "trunc_day({0})");
        add(Ops.DateTimeOps.TRUNC_HOUR, "trunc_hour({0})");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "trunc_minute({0})");
        add(Ops.DateTimeOps.TRUNC_SECOND, "trunc_second({0})");

        // math
        add(Ops.MathOps.ABS, "abs({0})");
        add(Ops.MathOps.ACOS, "acos({0})");
        add(Ops.MathOps.ASIN, "asin({0})");
        add(Ops.MathOps.ATAN, "atan({0})");
        add(Ops.MathOps.CEIL, "ceil({0})");
        add(Ops.MathOps.COS, "cos({0})");
        add(Ops.MathOps.COSH, "cosh({0})");
        add(Ops.MathOps.COT, "cot({0})");
        add(Ops.MathOps.COTH, "coth({0})");
        add(Ops.MathOps.DEG, "degrees({0})");
        add(Ops.MathOps.TAN, "tan({0})");
        add(Ops.MathOps.TANH, "tanh({0})");
        add(Ops.MathOps.SQRT, "sqrt({0})");
        add(Ops.MathOps.SIGN, "sign({0})");
        add(Ops.MathOps.SIN, "sin({0})");
        add(Ops.MathOps.SINH, "sinh({0})");
        add(Ops.MathOps.ROUND, "round({0})");
        add(Ops.MathOps.ROUND2, "round({0},{1})");
        add(Ops.MathOps.RAD, "radians({0})");
        add(Ops.MathOps.RANDOM, "random()");
        add(Ops.MathOps.RANDOM2, "random({0})");
        add(Ops.MathOps.POWER, "pow({0},{1})");
        add(Ops.MathOps.MIN, "min({0},{1})");
        add(Ops.MathOps.MAX, "max({0},{1})");
        add(Ops.MathOps.LOG, "log({0},{1})");
        add(Ops.MathOps.LN, "ln({0})");
        add(Ops.MathOps.FLOOR, "floor({0})");
        add(Ops.MathOps.EXP, "exp({0})");

        // path types
        add(PathType.PROPERTY, "{0}.{1s}");
        add(PathType.VARIABLE, "{0s}");
        add(PathType.DELEGATE, "{0}");
        add(Ops.ORDINAL, "ordinal({0})");
//        add(Ops.DELEGATE, "{0}");

        for (PathType type : new PathType[] {
                PathType.LISTVALUE,
                PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "{0}.get({1})");
        }
        add(PathType.ARRAYVALUE, "{0}[{1}]");
        add(PathType.COLLECTION_ANY, "any({0})");
        add(PathType.LISTVALUE_CONSTANT, "{0}.get({1s})"); // serialized constant
        add(PathType.ARRAYVALUE_CONSTANT, "{0}[{1s}]");    // serialized constant

        // case
        add(Ops.CASE, "case {0} end", 30);
        add(Ops.CASE_WHEN,  "when {0} then {1} {2}", 30);
        add(Ops.CASE_ELSE,  "else {0}", 30);

        // case for
        add(Ops.CASE_EQ, "case {0} {1} end", 30);
        add(Ops.CASE_EQ_WHEN,  "when {1} then {2} {3}", 30);
        add(Ops.CASE_EQ_ELSE,  "else {0}", 30);

        // coalesce
        add(Ops.COALESCE, "coalesce({0})");

        add(Ops.NULLIF, "nullif({0},{1})");

        // subquery
        add(Ops.EXISTS, "exists {0}");

        // numeric aggregates
        add(Ops.AggOps.BOOLEAN_ALL, "all({0})");
        add(Ops.AggOps.BOOLEAN_ANY, "any({0})");
        add(Ops.AggOps.AVG_AGG, "avg({0})");
        add(Ops.AggOps.MAX_AGG, "max({0})");
        add(Ops.AggOps.MIN_AGG, "min({0})");
        add(Ops.AggOps.SUM_AGG, "sum({0})");
        add(Ops.AggOps.COUNT_AGG, "count({0})");
        add(Ops.AggOps.COUNT_DISTINCT_AGG, "count(distinct {0})");
        add(Ops.AggOps.COUNT_DISTINCT_ALL_AGG, "count(distinct *)");
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
        templates.put(op, templateFactory.create(pattern));
        if (!precedence.containsKey(op)) {
            precedence.put(op, -1);
        }
    }

    protected final void add(Operator<?> op, String pattern, int pre) {
        templates.put(op, templateFactory.create(pattern));
        precedence.put(op, pre);
    }

    protected final void add(Map<Operator<?>, String> ops) {
        for (Map.Entry<Operator<?>, String> entry : ops.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    public final char getEscapeChar() {
        return escape;
    }

    protected String escapeForLike(String str) {
        final StringBuilder rv = new StringBuilder(str.length() + 3);
        for (char ch : str.toCharArray()) {
            if (ch == escape || ch == '%' || ch == '_') {
                rv.append(escape);
            }
            rv.append(ch);
        }
        return rv.toString();
    }

    @Nullable
    public final Template getTemplate(Operator<?> op) {
        return templates.get(op);
    }

    public final int getPrecedence(Operator<?> op) {
        return precedence.get(op).intValue();
    }

}
