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

import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * Ops provides the operators for the fluent query grammar.
 *
 * @author tiwe
 */
public enum Ops implements Operator {

    // general
    EQ,
    NE,
    IS_NULL,
    IS_NOT_NULL,
    INSTANCE_OF,
    NUMCAST,
    STRING_CAST,
    ALIAS,
    LIST,
    SINGLETON,
    ORDINAL,
    WRAPPED,

    // collection
    IN, // cmp. contains
    NOT_IN,
    COL_IS_EMPTY,
    COL_SIZE,

    // array
    ARRAY_SIZE,

    // map
    CONTAINS_KEY,
    CONTAINS_VALUE,
    MAP_SIZE,
    MAP_IS_EMPTY,

    // Boolean
    AND,
    NOT,
    OR,
    XNOR,
    XOR,

    // Comparable
    BETWEEN,
    GOE,
    GT,
    LOE,
    LT,

    // Number
    NEGATE,
    ADD,
    DIV,
    MULT,
    SUB,
    MOD,

    // String
    CHAR_AT,
    CONCAT,
    LOWER,
    SUBSTR_1ARG,
    SUBSTR_2ARGS,
    TRIM,
    UPPER,
    MATCHES,
    MATCHES_IC,
    STRING_LENGTH,
    STRING_IS_EMPTY,
    STARTS_WITH,
    STARTS_WITH_IC,
    INDEX_OF_2ARGS,
    INDEX_OF,
    EQ_IGNORE_CASE,
    ENDS_WITH,
    ENDS_WITH_IC,
    STRING_CONTAINS,
    STRING_CONTAINS_IC,
    LIKE,
    LIKE_ESCAPE,

    // case
    CASE,
    CASE_WHEN,
    CASE_ELSE,

    // case for eq
    CASE_EQ,
    CASE_EQ_WHEN,
    CASE_EQ_ELSE,

    // coalesce
    COALESCE,
    NULLIF,

    // subquery operations
    EXISTS;

    public static final Set<Operator> equalsOps = ImmutableSet.<Operator>of(EQ);

    public static final Set<Operator> notEqualsOps = ImmutableSet.<Operator>of(NE);

    public static final Set<Operator> compareOps = ImmutableSet.<Operator>of(EQ, NE, LT, GT, GOE, LOE);

    public static final Set<Operator> aggOps = ImmutableSet.<Operator>of(
            Ops.AggOps.AVG_AGG,
            Ops.AggOps.COUNT_AGG,
            Ops.AggOps.COUNT_DISTINCT_AGG,
            Ops.AggOps.MAX_AGG,
            Ops.AggOps.MIN_AGG,
            Ops.AggOps.SUM_AGG);

    /**
     * Aggregation operators
     */
    @SuppressWarnings("unchecked")
    public enum AggOps implements Operator {
        BOOLEAN_ALL,
        BOOLEAN_ANY,
        MAX_AGG,
        MIN_AGG,
        AVG_AGG,
        SUM_AGG,
        COUNT_AGG,
        COUNT_DISTINCT_AGG,
        COUNT_DISTINCT_ALL_AGG,
        COUNT_ALL_AGG
    }

    /**
     * Quantification operators
     */
    @SuppressWarnings("unchecked")
    public enum QuantOps implements Operator {
        AVG_IN_COL,
        MAX_IN_COL,
        MIN_IN_COL,
        ANY,
        ALL
    }

    /**
     * Date and time operators
     */
    @SuppressWarnings("unchecked")
    public enum DateTimeOps implements Operator {
        DATE,
        CURRENT_DATE,
        CURRENT_TIME,
        CURRENT_TIMESTAMP,
        ADD_YEARS,
        ADD_MONTHS,
        ADD_WEEKS,
        ADD_DAYS,
        ADD_HOURS,
        ADD_MINUTES,
        ADD_SECONDS,
        DIFF_YEARS,
        DIFF_MONTHS,
        DIFF_WEEKS,
        DIFF_DAYS,
        DIFF_HOURS,
        DIFF_MINUTES,
        DIFF_SECONDS,
        TRUNC_YEAR,
        TRUNC_MONTH,
        TRUNC_WEEK,
        TRUNC_DAY,
        TRUNC_HOUR,
        TRUNC_MINUTE,
        TRUNC_SECOND,
        HOUR,
        MINUTE,
        MONTH,
        SECOND,
        MILLISECOND,
        SYSDATE,
        YEAR,
        WEEK,
        YEAR_MONTH,
        YEAR_WEEK,
        DAY_OF_WEEK,
        DAY_OF_MONTH,
        DAY_OF_YEAR
    }

    /**
     * Math operators
     *
     */
    public enum MathOps implements Operator {
        ABS,
        ACOS,
        ASIN,
        ATAN,
        CEIL,
        COS,
        TAN,
        SQRT,
        SIN,
        ROUND,
        ROUND2,
        RANDOM,
        RANDOM2,
        POWER,
        MIN,
        MAX,
        LOG,
        FLOOR,
        EXP,
        COSH,
        COT,
        COTH,
        DEG,
        LN,
        RAD,
        SIGN,
        SINH,
        TANH
    }

    /**
     * String operators
     */
    public enum StringOps implements Operator {
        LEFT,
        RIGHT,
        LTRIM,
        RTRIM,
        LPAD,
        RPAD,
        LPAD2,
        RPAD2,
        LOCATE,
        LOCATE2
    }

}
