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
package com.mysema.query.types;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Ops provides the operators for the fluent query grammar.
 *
 * @author tiwe
 */
public final class Ops {

    // general
    public static final Operator<Boolean> EQ = new OperatorImpl<Boolean>("EQ");
    
    public static final Operator<Boolean> NE = new OperatorImpl<Boolean>("NE");
    
    public static final Operator<Boolean> IS_NULL = new OperatorImpl<Boolean>("IS_NULL");
    
    public static final Operator<Boolean> IS_NOT_NULL = new OperatorImpl<Boolean>("IS_NOT_NULL");    
    
    public static final Operator<Boolean> INSTANCE_OF = new OperatorImpl<Boolean>("INSTANCE_OF");    
    
    public static final Operator<Number>  NUMCAST = new OperatorImpl<Number>("NUMCAST");
    
    public static final Operator<String>  STRING_CAST = new OperatorImpl<String>("STING_CAST");
    
    public static final Operator<Object>  ALIAS = new OperatorImpl<Object>("ALIAS");
    
    public static final Operator<Object>  LIST = new OperatorImpl<Object>("LIST");
    
    public static final Operator<Integer> ORDINAL = new OperatorImpl<Integer>("ORDINAL");
    
//    public static final Operator<Object>  DELEGATE = new OperatorImplImpl<Object>("DELEGATE");
    
    public static final Operator<Object>  WRAPPED = new OperatorImpl<Object>("WRAPPED");

    
    // collection
    public static final Operator<Boolean> IN = new OperatorImpl<Boolean>("IN"); // cmp. contains
    
    public static final Operator<Boolean> COL_IS_EMPTY = new OperatorImpl<Boolean>("COL_IS_EMPTY");
    
    public static final Operator<Number>  COL_SIZE = new OperatorImpl<Number>("COL_SIZE");
    
    
    // array
    public static final Operator<Number>  ARRAY_SIZE = new OperatorImpl<Number>("ARRAY_SIZE");

    // map
    public static final Operator<Boolean> CONTAINS_KEY = new OperatorImpl<Boolean>("CONTAINS_KEY");

    public static final Operator<Boolean> CONTAINS_VALUE = new OperatorImpl<Boolean>("CONTAINS_VALUE");
    
    public static final Operator<Number>  MAP_SIZE = new OperatorImpl<Number>("MAP_SIZE");
    
    public static final Operator<Boolean> MAP_IS_EMPTY = new OperatorImpl<Boolean>("MAP_IS_EMPTY");

    // Boolean
    public static final Operator<Boolean> AND = new OperatorImpl<Boolean>("AND");
    
    public static final Operator<Boolean> NOT = new OperatorImpl<Boolean>("NOT");
    
    public static final Operator<Boolean> OR = new OperatorImpl<Boolean>("OR");
    
    public static final Operator<Boolean> XNOR = new OperatorImpl<Boolean>("XNOR");
    
    public static final Operator<Boolean> XOR = new OperatorImpl<Boolean>("XOR");

    // Comparable
    public static final Operator<Boolean> BETWEEN = new OperatorImpl<Boolean>("BETWEEN");
    
    public static final Operator<Boolean> GOE = new OperatorImpl<Boolean>("GOE");
    
    public static final Operator<Boolean> GT = new OperatorImpl<Boolean>("GT");
    
    public static final Operator<Boolean> LOE = new OperatorImpl<Boolean>("LOE");
    
    public static final Operator<Boolean> LT = new OperatorImpl<Boolean>("LT");

    // Number
    public static final Operator<Number>  NEGATE = new OperatorImpl<Number>("NEGATE");
    
    public static final Operator<Number>  ADD = new OperatorImpl<Number>("ADD");
    
    public static final Operator<Number>  DIV = new OperatorImpl<Number>("DIV");
    
    public static final Operator<Number>  MULT = new OperatorImpl<Number>("MULT");
    
    public static final Operator<Number>  SUB = new OperatorImpl<Number>("SUB");
    
    public static final Operator<Number>  MOD = new OperatorImpl<Number>("MOD");

    // String
    public static final Operator<Character> CHAR_AT = new OperatorImpl<Character>("CHAR_AT");
    
    public static final Operator<String>  CONCAT = new OperatorImpl<String>("CONCAT");
    
    public static final Operator<String>  LOWER = new OperatorImpl<String>("LOWER");
    
    public static final Operator<String>  SUBSTR_1ARG = new OperatorImpl<String>("SUBSTR");
    
    public static final Operator<String>  SUBSTR_2ARGS = new OperatorImpl<String>("SUBSTR2");
    
    public static final Operator<String>  TRIM = new OperatorImpl<String>("TRIM");
    
    public static final Operator<String>  UPPER = new OperatorImpl<String>("UPPER");
    
    public static final Operator<Boolean> MATCHES = new OperatorImpl<Boolean>("MATCHES");
    
    public static final Operator<Boolean> MATCHES_IC = new OperatorImpl<Boolean>("MATCHES_IC");
    
    public static final Operator<Number>  STRING_LENGTH = new OperatorImpl<Number>("STRING_LENGTH");
    
    public static final Operator<Boolean> STRING_IS_EMPTY = new OperatorImpl<Boolean>("STRING_IS_EMPTY");
    
    public static final Operator<Boolean> STARTS_WITH = new OperatorImpl<Boolean>("STARTS_WITH");
    
    public static final Operator<Boolean> STARTS_WITH_IC = new OperatorImpl<Boolean>("STATS_WITH_IC");
    
    public static final Operator<Number>  INDEX_OF_2ARGS = new OperatorImpl<Number>("INDEX_OF2");
    
    public static final Operator<Number>  INDEX_OF = new OperatorImpl<Number>("INDEX_OF");
    
    public static final Operator<Boolean> EQ_IGNORE_CASE = new OperatorImpl<Boolean>("EQ_IGNORE_CASE");
    
    public static final Operator<Boolean> ENDS_WITH = new OperatorImpl<Boolean>("ENDS_WITH");
    
    public static final Operator<Boolean> ENDS_WITH_IC = new OperatorImpl<Boolean>("ENDS_WITH_IC");
    
    public static final Operator<Boolean> STRING_CONTAINS = new OperatorImpl<Boolean>("STRING_CONTAINS");
    
    public static final Operator<Boolean> STRING_CONTAINS_IC = new OperatorImpl<Boolean>("STRING_CONTAINS_IC");
    
    public static final Operator<Boolean> LIKE = new OperatorImpl<Boolean>("LIKE");
    
    public static final Operator<Boolean> LIKE_ESCAPE = new OperatorImpl<Boolean>("LIKE_ESCAPE");

    // case
    public static final Operator<Object>  CASE = new OperatorImpl<Object>("CASE");
    
    public static final Operator<Object>  CASE_WHEN = new OperatorImpl<Object>("CASE_WHEN");
    
    public static final Operator<Object>  CASE_ELSE = new OperatorImpl<Object>("CASE_ELSE");

    // case for eq
    public static final Operator<Object>  CASE_EQ = new OperatorImpl<Object>("CASE_EQ");
    
    public static final Operator<Object>  CASE_EQ_WHEN = new OperatorImpl<Object>("CASE_EQ_WHEN");
    
    public static final Operator<Object>  CASE_EQ_ELSE = new OperatorImpl<Object>("CASE_EQ_ELSE");

    // coalesce
    public static final Operator<Object>  COALESCE = new OperatorImpl<Object>("COALESCE");
    
    public static final Operator<Object>  NULLIF = new OperatorImpl<Object>("NULLIF");

    // subquery operations
    public static final Operator<Boolean> EXISTS = new OperatorImpl<Boolean>("EXISTS");

    public static final List<Operator<?>> equalsOps = ImmutableList.<Operator<?>>of(EQ);

    public static final List<Operator<?>> notEqualsOps = ImmutableList.<Operator<?>>of(NE);

    public static final List<Operator<?>> compareOps = ImmutableList.<Operator<?>>of(EQ, NE, LT, GT, GOE, LOE);

    /**
     * Aggregation operators
     */
    @SuppressWarnings("unchecked")
    public static final class AggOps{
    
        public static final Operator<Boolean> BOOLEAN_ALL = new OperatorImpl<Boolean>("BOOLEAN_ALL");
        
        public static final Operator<Boolean> BOOLEAN_ANY = new OperatorImpl<Boolean>("BOOLEAN_ANY");
        
        public static final Operator<Comparable> MAX_AGG = new OperatorImpl<Comparable>("MAX_AGG");
        
        public static final Operator<Comparable> MIN_AGG = new OperatorImpl<Comparable>("MIN_AGG");
        
        public static final Operator<Number> AVG_AGG = new OperatorImpl<Number>("AVG_AGG");
        
        public static final Operator<Number> SUM_AGG = new OperatorImpl<Number>("SUM_AGG");
        
        public static final Operator<Long> COUNT_AGG = new OperatorImpl<Long>("COUNT_AGG");
        
        public static final Operator<Long> COUNT_DISTINCT_AGG = new OperatorImpl<Long>("COUNT_DISTINCT_AGG");
        
        public static final Operator<Long> COUNT_DISTINCT_ALL_AGG = new OperatorImpl<Long>("COUNT_DISTINCT_ALL_AGG");
        
        public static final Operator<Long> COUNT_ALL_AGG = new OperatorImpl<Long>("COUNT_ALL_AGG");
        
        private AggOps() {}
    }
    
    /**
     * Quantification operators
     */
    @SuppressWarnings("unchecked")
    public static final class QuantOps {
        
        public static final Operator<Comparable> AVG_IN_COL = new OperatorImpl<Comparable>("AVG_IN_COL");
        
        public static final Operator<Comparable> MAX_IN_COL = new OperatorImpl<Comparable>("MAX_IN_COL");
        
        public static final Operator<Comparable> MIN_IN_COL = new OperatorImpl<Comparable>("MIN_IN_COL");

        public static final Operator<Object> ANY = new OperatorImpl<Object>("ANY");
        
        public static final Operator<Object> ALL = new OperatorImpl<Object>("ALL");
        
        private QuantOps() {}
    }

    /**
     * Date and time operators
     */
    @SuppressWarnings("unchecked")
    public static final class DateTimeOps {
        
        public static final Operator<Comparable> CURRENT_DATE = new OperatorImpl<Comparable>("CURRENT_DATE");
        
        public static final Operator<Comparable> CURRENT_TIME = new OperatorImpl<Comparable>("CURRENT_TIME");
        
        public static final Operator<Comparable> CURRENT_TIMESTAMP = new OperatorImpl<Comparable>("CURRENT_TIMESTAMP");
        
        public static final Operator<Comparable> ADD_YEARS = new OperatorImpl<Comparable>("ADD_YEARS");
        
        public static final Operator<Comparable> ADD_MONTHS = new OperatorImpl<Comparable>("ADD_MONTHS");
        
        public static final Operator<Comparable> ADD_WEEKS = new OperatorImpl<Comparable>("ADD_WEEKS");
        
        public static final Operator<Comparable> ADD_DAYS = new OperatorImpl<Comparable>("ADD_DAYS");
        
        public static final Operator<Comparable> ADD_HOURS = new OperatorImpl<Comparable>("ADD_HOURS");        
        
        public static final Operator<Comparable> ADD_MINUTES = new OperatorImpl<Comparable>("ADD_MINUTES");
        
        public static final Operator<Comparable> ADD_SECONDS = new OperatorImpl<Comparable>("ADD_MINUTES");
        
        public static final Operator<Integer> HOUR = new OperatorImpl<Integer>("HOUR");
        
        public static final Operator<Integer> MINUTE = new OperatorImpl<Integer>("MINUTE");
        
        public static final Operator<Integer> MONTH = new OperatorImpl<Integer>("MONTH");
        
        public static final Operator<Integer> SECOND = new OperatorImpl<Integer>("SECOND");
        
        public static final Operator<Integer> MILLISECOND = new OperatorImpl<Integer>("MILLISECOND");
        
        public static final Operator<Comparable> SYSDATE = new OperatorImpl<Comparable>("SYSDATE");
        
        public static final Operator<Integer> YEAR = new OperatorImpl<Integer>("YEAR");
        
        public static final Operator<Integer> YEAR_MONTH = new OperatorImpl<Integer>("YEAR_MONTH");
        
        public static final Operator<Integer> WEEK = new OperatorImpl<Integer>("WEEK");
        
        public static final Operator<Integer> DAY_OF_WEEK = new OperatorImpl<Integer>("DAY_OF_WEEK");
        
        public static final Operator<Integer> DAY_OF_MONTH = new OperatorImpl<Integer>("DAY_OF_MONTH");
        
        public static final Operator<Integer> DAY_OF_YEAR = new OperatorImpl<Integer>("DAY_OF_YEAR");
        
        private DateTimeOps() {}
    }

    /**
     * Math operators
     *
     */
    public static final class MathOps {
        
        public static final Operator<Number> ABS = new OperatorImpl<Number>("ABS");
        
        public static final Operator<Number> ACOS = new OperatorImpl<Number>("ACOS");
        
        public static final Operator<Number> ASIN = new OperatorImpl<Number>("ASIN");
        
        public static final Operator<Number> ATAN = new OperatorImpl<Number>("ATAN");
        
        public static final Operator<Number> CEIL = new OperatorImpl<Number>("CEIL");
        
        public static final Operator<Number> COS = new OperatorImpl<Number>("COS");
        
        public static final Operator<Number> TAN = new OperatorImpl<Number>("TAN");
        
        public static final Operator<Number> SQRT = new OperatorImpl<Number>("SQRT");
        
        public static final Operator<Number> SIN = new OperatorImpl<Number>("SIN");
        
        public static final Operator<Number> ROUND = new OperatorImpl<Number>("ROUND");
        
        public static final Operator<Number> RANDOM = new OperatorImpl<Number>("RANDOM");
        
        public static final Operator<Number> RANDOM2 = new OperatorImpl<Number>("RANDOM2");
        
        public static final Operator<Number> POWER = new OperatorImpl<Number>("POWER");
        
        public static final Operator<Number> MIN = new OperatorImpl<Number>("MIN");
        
        public static final Operator<Number> MAX = new OperatorImpl<Number>("MAX");
        
        public static final Operator<Number> LOG = new OperatorImpl<Number>("LOG");
        
        public static final Operator<Number> FLOOR = new OperatorImpl<Number>("FLOOR");
        
        public static final Operator<Number> EXP = new OperatorImpl<Number>("EXP");
        
        public static final Operator<Number> COSH = new OperatorImpl<Number>("COSH");
        
        public static final Operator<Number> COT = new OperatorImpl<Number>("COT");
        
        public static final Operator<Number> COTH = new OperatorImpl<Number>("COTH");
        
        public static final Operator<Number> DEG = new OperatorImpl<Number>("DEG");
        
        public static final Operator<Number> LN = new OperatorImpl<Number>("LN");
        
        public static final Operator<Number> RAD = new OperatorImpl<Number>("RAD");
        
        public static final Operator<Number> SIGN = new OperatorImpl<Number>("SIGN");
        
        public static final Operator<Number> SINH = new OperatorImpl<Number>("SINH");
        
        public static final Operator<Number> TANH = new OperatorImpl<Number>("TANH");
        
        private MathOps() {}
    }

    /**
     * String operators
     */
    public static final class StringOps {
        
        public static final Operator<String> LTRIM = new OperatorImpl<String>("LTRIM");
        
        public static final Operator<String> RTRIM = new OperatorImpl<String>("RTRIM");
        
        public static final Operator<String> LPAD = new OperatorImpl<String>("LPAD");
        
        public static final Operator<String> RPAD = new OperatorImpl<String>("RPAD");
        
        public static final Operator<String> LPAD2 = new OperatorImpl<String>("LPAD2");
        
        public static final Operator<String> RPAD2 = new OperatorImpl<String>("RPAD2");
        
        public static final Operator<Number> LOCATE = new OperatorImpl<Number>("LOCATE");
        
        public static final Operator<Number> LOCATE2 = new OperatorImpl<Number>("LOCATE2");
        
        private StringOps() {}
    }

    
    private Ops() {}
}
