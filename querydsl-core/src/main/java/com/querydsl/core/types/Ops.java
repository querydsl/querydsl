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
    EQ(Boolean.class),
    NE(Boolean.class),
    IS_NULL(Boolean.class),
    IS_NOT_NULL(Boolean.class),
    INSTANCE_OF(Boolean.class),
    NUMCAST(Number.class),
    STRING_CAST(String.class),
    ALIAS(Object.class),
    LIST(Object.class),
    SINGLETON(Object.class),
    ORDINAL(Integer.class),
    WRAPPED(Object.class),

    // collection
    IN(Boolean.class), // cmp. contains
    NOT_IN(Boolean.class),
    COL_IS_EMPTY(Boolean.class),
    COL_SIZE(Integer.class),

    // array
    ARRAY_SIZE(Number.class),

    // map
    CONTAINS_KEY(Boolean.class),
    CONTAINS_VALUE(Boolean.class),
    MAP_SIZE(Integer.class),
    MAP_IS_EMPTY(Boolean.class),

    // Boolean
    AND(Boolean.class),
    NOT(Boolean.class),
    OR(Boolean.class),
    XNOR(Boolean.class),
    XOR(Boolean.class),

    // Comparable
    BETWEEN(Boolean.class),
    GOE(Boolean.class),
    GT(Boolean.class),
    LOE(Boolean.class),
    LT(Boolean.class),

    // Number
    NEGATE(Number.class),
    ADD(Number.class),
    DIV(Number.class),
    MULT(Number.class),
    SUB(Number.class),
    MOD(Number.class),

    // String
    CHAR_AT(Character.class),
    CONCAT(String.class),
    LOWER(String.class),
    SUBSTR_1ARG(String.class),
    SUBSTR_2ARGS(String.class),
    TRIM(String.class),
    UPPER(String.class),
    MATCHES(Boolean.class),
    MATCHES_IC(Boolean.class),
    STRING_LENGTH(Integer.class),
    STRING_IS_EMPTY(Boolean.class),
    STARTS_WITH(Boolean.class),
    STARTS_WITH_IC(Boolean.class),
    INDEX_OF_2ARGS(Integer.class),
    INDEX_OF(Integer.class),
    EQ_IGNORE_CASE(Boolean.class),
    ENDS_WITH(Boolean.class),
    ENDS_WITH_IC(Boolean.class),
    STRING_CONTAINS(Boolean.class),
    STRING_CONTAINS_IC(Boolean.class),
    LIKE(Boolean.class),
    LIKE_ESCAPE(Boolean.class),

    // case
    CASE(Object.class),
    CASE_WHEN(Object.class),
    CASE_ELSE(Object.class),

    // case for eq
    CASE_EQ(Object.class),
    CASE_EQ_WHEN(Object.class),
    CASE_EQ_ELSE(Object.class),

    // coalesce
    COALESCE(Object.class),
    NULLIF(Object.class),

    // subquery operations
    EXISTS(Boolean.class);

    private final Class<?> type;

    private Ops(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

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
    public enum AggOps implements Operator {
        BOOLEAN_ALL(Boolean.class),
        BOOLEAN_ANY(Boolean.class),
        MAX_AGG(Comparable.class),
        MIN_AGG(Comparable.class),
        AVG_AGG(Number.class),
        SUM_AGG(Number.class),
        COUNT_AGG(Number.class),
        COUNT_DISTINCT_AGG(Number.class),
        COUNT_DISTINCT_ALL_AGG(Number.class),
        COUNT_ALL_AGG(Number.class);

        private final Class<?> type;

        private AggOps(Class<?> type) {
            this.type = type;
        }

        public Class<?> getType() {
            return type;
        }
    }

    /**
     * Quantification operators
     */
    public enum QuantOps implements Operator {
        AVG_IN_COL(Number.class),
        MAX_IN_COL(Comparable.class),
        MIN_IN_COL(Comparable.class),
        ANY(Object.class),
        ALL(Object.class);

        private final Class<?> type;

        private QuantOps(Class<?> type) {
            this.type = type;
        }

        public Class<?> getType() {
            return type;
        }
    }

    /**
     * Date and time operators
     */
    public enum DateTimeOps implements Operator {
        DATE(Comparable.class),
        CURRENT_DATE(Comparable.class),
        CURRENT_TIME(Comparable.class),
        CURRENT_TIMESTAMP(Comparable.class),
        ADD_YEARS(Comparable.class),
        ADD_MONTHS(Comparable.class),
        ADD_WEEKS(Comparable.class),
        ADD_DAYS(Comparable.class),
        ADD_HOURS(Comparable.class),
        ADD_MINUTES(Comparable.class),
        ADD_SECONDS(Comparable.class),
        DIFF_YEARS(Comparable.class),
        DIFF_MONTHS(Comparable.class),
        DIFF_WEEKS(Comparable.class),
        DIFF_DAYS(Comparable.class),
        DIFF_HOURS(Comparable.class),
        DIFF_MINUTES(Comparable.class),
        DIFF_SECONDS(Comparable.class),
        TRUNC_YEAR(Comparable.class),
        TRUNC_MONTH(Comparable.class),
        TRUNC_WEEK(Comparable.class),
        TRUNC_DAY(Comparable.class),
        TRUNC_HOUR(Comparable.class),
        TRUNC_MINUTE(Comparable.class),
        TRUNC_SECOND(Comparable.class),
        HOUR(Integer.class),
        MINUTE(Integer.class),
        MONTH(Integer.class),
        SECOND(Integer.class),
        MILLISECOND(Integer.class),
        SYSDATE(Comparable.class),
        YEAR(Integer.class),
        WEEK(Integer.class),
        YEAR_MONTH(Integer.class),
        YEAR_WEEK(Integer.class),
        DAY_OF_WEEK(Integer.class),
        DAY_OF_MONTH(Integer.class),
        DAY_OF_YEAR(Integer.class);

        private final Class<?> type;

        private DateTimeOps(Class<?> type) {
            this.type = type;
        }

        public Class<?> getType() {
            return type;
        }
    }

    /**
     * Math operators
     *
     */
    public enum MathOps implements Operator {
        ABS(Number.class),
        ACOS(Number.class),
        ASIN(Number.class),
        ATAN(Number.class),
        CEIL(Number.class),
        COS(Number.class),
        TAN(Number.class),
        SQRT(Number.class),
        SIN(Number.class),
        ROUND(Number.class),
        ROUND2(Number.class),
        RANDOM(Number.class),
        RANDOM2(Number.class),
        POWER(Number.class),
        MIN(Number.class),
        MAX(Number.class),
        LOG(Number.class),
        FLOOR(Number.class),
        EXP(Number.class),
        COSH(Number.class),
        COT(Number.class),
        COTH(Number.class),
        DEG(Number.class),
        LN(Number.class),
        RAD(Number.class),
        SIGN(Number.class),
        SINH(Number.class),
        TANH(Number.class);

        private final Class<?> type;

        private MathOps(Class<?> type) {
            this.type = type;
        }

        public Class<?> getType() {
            return type;
        }
    }

    /**
     * String operators
     */
    public enum StringOps implements Operator {
        LEFT(String.class),
        RIGHT(String.class),
        LTRIM(String.class),
        RTRIM(String.class),
        LPAD(String.class),
        RPAD(String.class),
        LPAD2(String.class),
        RPAD2(String.class),
        LOCATE(Number.class),
        LOCATE2(Number.class);

        private final Class<?> type;

        private StringOps(Class<?> type) {
            this.type = type;
        }

        public Class<?> getType() {
            return type;
        }
    }

}
