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
 * Ops provides the operators for the fluent querydsl grammar.
 *
 * @author tiwe
 */
public final class Ops {

    private static final String NS = Ops.class.getName();

    // general
    public static final Operator<Boolean> EQ = new OperatorImpl<Boolean>(NS, "EQ");

    public static final Operator<Boolean> NE = new OperatorImpl<Boolean>(NS, "NE");

    public static final Operator<Boolean> IS_NULL = new OperatorImpl<Boolean>(NS, "IS_NULL");

    public static final Operator<Boolean> IS_NOT_NULL = new OperatorImpl<Boolean>(NS, "IS_NOT_NULL");

    public static final Operator<Boolean> INSTANCE_OF = new OperatorImpl<Boolean>(NS, "INSTANCE_OF");

    public static final Operator<Number>  NUMCAST = new OperatorImpl<Number>(NS, "NUMCAST");

    public static final Operator<String>  STRING_CAST = new OperatorImpl<String>(NS, "STRING_CAST");

    public static final Operator<Object>  ALIAS = new OperatorImpl<Object>(NS, "ALIAS");

    public static final Operator<Object>  LIST = new OperatorImpl<Object>(NS, "LIST");

    public static final Operator<Object>  SINGLETON = new OperatorImpl<Object>(NS, "SINGLETON");

    public static final Operator<Integer> ORDINAL = new OperatorImpl<Integer>(NS, "ORDINAL");

    public static final Operator<Object>  WRAPPED = new OperatorImpl<Object>(NS, "WRAPPED");


    // collection
    public static final Operator<Boolean> IN = new OperatorImpl<Boolean>(NS, "IN"); // cmp. contains

    public static final Operator<Boolean> NOT_IN = new OperatorImpl<Boolean>(NS, "NOT_IN");

    public static final Operator<Boolean> COL_IS_EMPTY = new OperatorImpl<Boolean>(NS, "COL_IS_EMPTY");

    public static final Operator<Number>  COL_SIZE = new OperatorImpl<Number>(NS, "COL_SIZE");


    // array
    public static final Operator<Number>  ARRAY_SIZE = new OperatorImpl<Number>(NS, "ARRAY_SIZE");

    // map
    public static final Operator<Boolean> CONTAINS_KEY = new OperatorImpl<Boolean>(NS, "CONTAINS_KEY");

    public static final Operator<Boolean> CONTAINS_VALUE = new OperatorImpl<Boolean>(NS, "CONTAINS_VALUE");

    public static final Operator<Number>  MAP_SIZE = new OperatorImpl<Number>(NS, "MAP_SIZE");

    public static final Operator<Boolean> MAP_IS_EMPTY = new OperatorImpl<Boolean>(NS, "MAP_IS_EMPTY");

    // Boolean
    public static final Operator<Boolean> AND = new OperatorImpl<Boolean>(NS, "AND");

    public static final Operator<Boolean> NOT = new OperatorImpl<Boolean>(NS, "NOT");

    public static final Operator<Boolean> OR = new OperatorImpl<Boolean>(NS, "OR");

    public static final Operator<Boolean> XNOR = new OperatorImpl<Boolean>(NS, "XNOR");

    public static final Operator<Boolean> XOR = new OperatorImpl<Boolean>(NS, "XOR");

    // Comparable
    public static final Operator<Boolean> BETWEEN = new OperatorImpl<Boolean>(NS, "BETWEEN");

    public static final Operator<Boolean> GOE = new OperatorImpl<Boolean>(NS, "GOE");

    public static final Operator<Boolean> GT = new OperatorImpl<Boolean>(NS, "GT");

    public static final Operator<Boolean> LOE = new OperatorImpl<Boolean>(NS, "LOE");

    public static final Operator<Boolean> LT = new OperatorImpl<Boolean>(NS, "LT");

    // Number
    public static final Operator<Number>  NEGATE = new OperatorImpl<Number>(NS, "NEGATE");

    public static final Operator<Number>  ADD = new OperatorImpl<Number>(NS, "ADD");

    public static final Operator<Number>  DIV = new OperatorImpl<Number>(NS, "DIV");

    public static final Operator<Number>  MULT = new OperatorImpl<Number>(NS, "MULT");

    public static final Operator<Number>  SUB = new OperatorImpl<Number>(NS, "SUB");

    public static final Operator<Number>  MOD = new OperatorImpl<Number>(NS, "MOD");

    // String
    public static final Operator<Character> CHAR_AT = new OperatorImpl<Character>(NS, "CHAR_AT");

    public static final Operator<String>  CONCAT = new OperatorImpl<String>(NS, "CONCAT");

    public static final Operator<String>  LOWER = new OperatorImpl<String>(NS, "LOWER");

    public static final Operator<String>  SUBSTR_1ARG = new OperatorImpl<String>(NS, "SUBSTR");

    public static final Operator<String>  SUBSTR_2ARGS = new OperatorImpl<String>(NS, "SUBSTR2");

    public static final Operator<String>  TRIM = new OperatorImpl<String>(NS, "TRIM");

    public static final Operator<String>  UPPER = new OperatorImpl<String>(NS, "UPPER");

    public static final Operator<Boolean> MATCHES = new OperatorImpl<Boolean>(NS, "MATCHES");

    public static final Operator<Boolean> MATCHES_IC = new OperatorImpl<Boolean>(NS, "MATCHES_IC");

    public static final Operator<Number>  STRING_LENGTH = new OperatorImpl<Number>(NS, "STRING_LENGTH");

    public static final Operator<Boolean> STRING_IS_EMPTY = new OperatorImpl<Boolean>(NS, "STRING_IS_EMPTY");

    public static final Operator<Boolean> STARTS_WITH = new OperatorImpl<Boolean>(NS, "STARTS_WITH");

    public static final Operator<Boolean> STARTS_WITH_IC = new OperatorImpl<Boolean>(NS, "STATS_WITH_IC");

    public static final Operator<Number>  INDEX_OF_2ARGS = new OperatorImpl<Number>(NS, "INDEX_OF2");

    public static final Operator<Number>  INDEX_OF = new OperatorImpl<Number>(NS, "INDEX_OF");

    public static final Operator<Boolean> EQ_IGNORE_CASE = new OperatorImpl<Boolean>(NS, "EQ_IGNORE_CASE");

    public static final Operator<Boolean> ENDS_WITH = new OperatorImpl<Boolean>(NS, "ENDS_WITH");

    public static final Operator<Boolean> ENDS_WITH_IC = new OperatorImpl<Boolean>(NS, "ENDS_WITH_IC");

    public static final Operator<Boolean> STRING_CONTAINS = new OperatorImpl<Boolean>(NS, "STRING_CONTAINS");

    public static final Operator<Boolean> STRING_CONTAINS_IC = new OperatorImpl<Boolean>(NS, "STRING_CONTAINS_IC");

    public static final Operator<Boolean> LIKE = new OperatorImpl<Boolean>(NS, "LIKE");

    public static final Operator<Boolean> LIKE_ESCAPE = new OperatorImpl<Boolean>(NS, "LIKE_ESCAPE");

    // case
    public static final Operator<Object>  CASE = new OperatorImpl<Object>(NS, "CASE");

    public static final Operator<Object>  CASE_WHEN = new OperatorImpl<Object>(NS, "CASE_WHEN");

    public static final Operator<Object>  CASE_ELSE = new OperatorImpl<Object>(NS, "CASE_ELSE");

    // case for eq
    public static final Operator<Object>  CASE_EQ = new OperatorImpl<Object>(NS, "CASE_EQ");

    public static final Operator<Object>  CASE_EQ_WHEN = new OperatorImpl<Object>(NS, "CASE_EQ_WHEN");

    public static final Operator<Object>  CASE_EQ_ELSE = new OperatorImpl<Object>(NS, "CASE_EQ_ELSE");

    // coalesce
    public static final Operator<Object>  COALESCE = new OperatorImpl<Object>(NS, "COALESCE");

    public static final Operator<Object>  NULLIF = new OperatorImpl<Object>(NS, "NULLIF");

    // subquery operations
    public static final Operator<Boolean> EXISTS = new OperatorImpl<Boolean>(NS, "EXISTS");

    public static final Set<Operator<?>> equalsOps = ImmutableSet.<Operator<?>>of(EQ);

    public static final Set<Operator<?>> notEqualsOps = ImmutableSet.<Operator<?>>of(NE);

    public static final Set<Operator<?>> compareOps = ImmutableSet.<Operator<?>>of(EQ, NE, LT, GT, GOE, LOE);

    public static final Set<Operator<?>> aggOps = ImmutableSet.of(
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
    public static final class AggOps{

        private static final String NS = AggOps.class.getName();

        public static final Operator<Boolean> BOOLEAN_ALL = new OperatorImpl<Boolean>(NS, "BOOLEAN_ALL");

        public static final Operator<Boolean> BOOLEAN_ANY = new OperatorImpl<Boolean>(NS, "BOOLEAN_ANY");

        public static final Operator<Comparable> MAX_AGG = new OperatorImpl<Comparable>(NS, "MAX_AGG");

        public static final Operator<Comparable> MIN_AGG = new OperatorImpl<Comparable>(NS, "MIN_AGG");

        public static final Operator<Number> AVG_AGG = new OperatorImpl<Number>(NS, "AVG_AGG");

        public static final Operator<Number> SUM_AGG = new OperatorImpl<Number>(NS, "SUM_AGG");

        public static final Operator<Long> COUNT_AGG = new OperatorImpl<Long>(NS, "COUNT_AGG");

        public static final Operator<Long> COUNT_DISTINCT_AGG = new OperatorImpl<Long>(NS, "COUNT_DISTINCT_AGG");

        public static final Operator<Long> COUNT_DISTINCT_ALL_AGG = new OperatorImpl<Long>(NS, "COUNT_DISTINCT_ALL_AGG");

        public static final Operator<Long> COUNT_ALL_AGG = new OperatorImpl<Long>(NS, "COUNT_ALL_AGG");

        private AggOps() {}
    }

    /**
     * Quantification operators
     */
    @SuppressWarnings("unchecked")
    public static final class QuantOps {

        private static final String NS = QuantOps.class.getName();

        public static final Operator<Comparable> AVG_IN_COL = new OperatorImpl<Comparable>(NS, "AVG_IN_COL");

        public static final Operator<Comparable> MAX_IN_COL = new OperatorImpl<Comparable>(NS, "MAX_IN_COL");

        public static final Operator<Comparable> MIN_IN_COL = new OperatorImpl<Comparable>(NS, "MIN_IN_COL");

        public static final Operator<Object> ANY = new OperatorImpl<Object>(NS, "ANY");

        public static final Operator<Object> ALL = new OperatorImpl<Object>(NS, "ALL");

        private QuantOps() {}
    }

    /**
     * Date and time operators
     */
    @SuppressWarnings("unchecked")
    public static final class DateTimeOps {

        private static final String NS = DateTimeOps.class.getName();

        public static final Operator<Comparable> DATE = new OperatorImpl<Comparable>(NS, "DATE");

        public static final Operator<Comparable> CURRENT_DATE = new OperatorImpl<Comparable>(NS, "CURRENT_DATE");

        public static final Operator<Comparable> CURRENT_TIME = new OperatorImpl<Comparable>(NS, "CURRENT_TIME");

        public static final Operator<Comparable> CURRENT_TIMESTAMP = new OperatorImpl<Comparable>(NS, "CURRENT_TIMESTAMP");

        public static final Operator<Comparable> ADD_YEARS = new OperatorImpl<Comparable>(NS, "ADD_YEARS");

        public static final Operator<Comparable> ADD_MONTHS = new OperatorImpl<Comparable>(NS, "ADD_MONTHS");

        public static final Operator<Comparable> ADD_WEEKS = new OperatorImpl<Comparable>(NS, "ADD_WEEKS");

        public static final Operator<Comparable> ADD_DAYS = new OperatorImpl<Comparable>(NS, "ADD_DAYS");

        public static final Operator<Comparable> ADD_HOURS = new OperatorImpl<Comparable>(NS, "ADD_HOURS");

        public static final Operator<Comparable> ADD_MINUTES = new OperatorImpl<Comparable>(NS, "ADD_MINUTES");

        public static final Operator<Comparable> ADD_SECONDS = new OperatorImpl<Comparable>(NS, "ADD_SECONDS");

        public static final Operator<Comparable> DIFF_YEARS = new OperatorImpl<Comparable>(NS, "DIFF_YEARS");

        public static final Operator<Comparable> DIFF_MONTHS = new OperatorImpl<Comparable>(NS, "DIFF_MONTHS");

        public static final Operator<Comparable> DIFF_WEEKS = new OperatorImpl<Comparable>(NS, "DIFF_WEEKS");

        public static final Operator<Comparable> DIFF_DAYS = new OperatorImpl<Comparable>(NS, "DIFF_DAYS");

        public static final Operator<Comparable> DIFF_HOURS = new OperatorImpl<Comparable>(NS, "DIFF_HOURS");

        public static final Operator<Comparable> DIFF_MINUTES = new OperatorImpl<Comparable>(NS, "DIFF_MINUTES");

        public static final Operator<Comparable> DIFF_SECONDS = new OperatorImpl<Comparable>(NS, "DIFF_SECONDS");

        public static final Operator<Comparable> TRUNC_YEAR = new OperatorImpl<Comparable>(NS, "TRUNC_YEAR");

        public static final Operator<Comparable> TRUNC_MONTH = new OperatorImpl<Comparable>(NS, "TRUNC_MONTH");

        public static final Operator<Comparable> TRUNC_WEEK = new OperatorImpl<Comparable>(NS, "TRUNC_WEEK");

        public static final Operator<Comparable> TRUNC_DAY = new OperatorImpl<Comparable>(NS, "TRUNC_DAY");

        public static final Operator<Comparable> TRUNC_HOUR = new OperatorImpl<Comparable>(NS, "TRUNC_HOUR");

        public static final Operator<Comparable> TRUNC_MINUTE = new OperatorImpl<Comparable>(NS, "TRUNC_MINUTE");

        public static final Operator<Comparable> TRUNC_SECOND = new OperatorImpl<Comparable>(NS, "TRUNC_SECOND");

        public static final Operator<Integer> HOUR = new OperatorImpl<Integer>(NS, "HOUR");

        public static final Operator<Integer> MINUTE = new OperatorImpl<Integer>(NS, "MINUTE");

        public static final Operator<Integer> MONTH = new OperatorImpl<Integer>(NS, "MONTH");

        public static final Operator<Integer> SECOND = new OperatorImpl<Integer>(NS, "SECOND");

        public static final Operator<Integer> MILLISECOND = new OperatorImpl<Integer>(NS, "MILLISECOND");

        public static final Operator<Comparable> SYSDATE = new OperatorImpl<Comparable>(NS, "SYSDATE");

        public static final Operator<Integer> YEAR = new OperatorImpl<Integer>(NS, "YEAR");

        public static final Operator<Integer> WEEK = new OperatorImpl<Integer>(NS, "WEEK");

        public static final Operator<Integer> YEAR_MONTH = new OperatorImpl<Integer>(NS, "YEAR_MONTH");

        public static final Operator<Integer> YEAR_WEEK = new OperatorImpl<Integer>(NS, "YEAR_WEEK");

        public static final Operator<Integer> DAY_OF_WEEK = new OperatorImpl<Integer>(NS, "DAY_OF_WEEK");

        public static final Operator<Integer> DAY_OF_MONTH = new OperatorImpl<Integer>(NS, "DAY_OF_MONTH");

        public static final Operator<Integer> DAY_OF_YEAR = new OperatorImpl<Integer>(NS, "DAY_OF_YEAR");

        private DateTimeOps() {}
    }

    /**
     * Math operators
     *
     */
    public static final class MathOps {

        private static final String NS = MathOps.class.getName();

        public static final Operator<Number> ABS = new OperatorImpl<Number>(NS, "ABS");

        public static final Operator<Number> ACOS = new OperatorImpl<Number>(NS, "ACOS");

        public static final Operator<Number> ASIN = new OperatorImpl<Number>(NS, "ASIN");

        public static final Operator<Number> ATAN = new OperatorImpl<Number>(NS, "ATAN");

        public static final Operator<Number> CEIL = new OperatorImpl<Number>(NS, "CEIL");

        public static final Operator<Number> COS = new OperatorImpl<Number>(NS, "COS");

        public static final Operator<Number> TAN = new OperatorImpl<Number>(NS, "TAN");

        public static final Operator<Number> SQRT = new OperatorImpl<Number>(NS, "SQRT");

        public static final Operator<Number> SIN = new OperatorImpl<Number>(NS, "SIN");

        public static final Operator<Number> ROUND = new OperatorImpl<Number>(NS, "ROUND");

        public static final Operator<Number> ROUND2 = new OperatorImpl<Number>(NS, "ROUND");

        public static final Operator<Number> RANDOM = new OperatorImpl<Number>(NS, "RANDOM");

        public static final Operator<Number> RANDOM2 = new OperatorImpl<Number>(NS, "RANDOM2");

        public static final Operator<Number> POWER = new OperatorImpl<Number>(NS, "POWER");

        public static final Operator<Number> MIN = new OperatorImpl<Number>(NS, "MIN");

        public static final Operator<Number> MAX = new OperatorImpl<Number>(NS, "MAX");

        public static final Operator<Number> LOG = new OperatorImpl<Number>(NS, "LOG");

        public static final Operator<Number> FLOOR = new OperatorImpl<Number>(NS, "FLOOR");

        public static final Operator<Number> EXP = new OperatorImpl<Number>(NS, "EXP");

        public static final Operator<Number> COSH = new OperatorImpl<Number>(NS, "COSH");

        public static final Operator<Number> COT = new OperatorImpl<Number>(NS, "COT");

        public static final Operator<Number> COTH = new OperatorImpl<Number>(NS, "COTH");

        public static final Operator<Number> DEG = new OperatorImpl<Number>(NS, "DEG");

        public static final Operator<Number> LN = new OperatorImpl<Number>(NS, "LN");

        public static final Operator<Number> RAD = new OperatorImpl<Number>(NS, "RAD");

        public static final Operator<Number> SIGN = new OperatorImpl<Number>(NS, "SIGN");

        public static final Operator<Number> SINH = new OperatorImpl<Number>(NS, "SINH");

        public static final Operator<Number> TANH = new OperatorImpl<Number>(NS, "TANH");

        private MathOps() {}
    }

    /**
     * String operators
     */
    public static final class StringOps {

        private static final String NS = StringOps.class.getName();

        public static final Operator<String> LEFT = new OperatorImpl<String>(NS, "LEFT");

        public static final Operator<String> RIGHT = new OperatorImpl<String>(NS, "RIGHT");

        public static final Operator<String> LTRIM = new OperatorImpl<String>(NS, "LTRIM");

        public static final Operator<String> RTRIM = new OperatorImpl<String>(NS, "RTRIM");

        public static final Operator<String> LPAD = new OperatorImpl<String>(NS, "LPAD");

        public static final Operator<String> RPAD = new OperatorImpl<String>(NS, "RPAD");

        public static final Operator<String> LPAD2 = new OperatorImpl<String>(NS, "LPAD2");

        public static final Operator<String> RPAD2 = new OperatorImpl<String>(NS, "RPAD2");

        public static final Operator<Number> LOCATE = new OperatorImpl<Number>(NS, "LOCATE");

        public static final Operator<Number> LOCATE2 = new OperatorImpl<Number>(NS, "LOCATE2");

        private StringOps() {}
    }

    private Ops() {}
}
