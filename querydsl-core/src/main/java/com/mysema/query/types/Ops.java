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

import static java.util.Collections.unmodifiableList;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Ops provides the operators for the fluent query grammar.
 *
 * @author tiwe
 */
public final class Ops {

    private static final List<Class<?>> BOOLEAN_X_2 = unmodifiableList(Arrays.<Class<?>> asList(Boolean.class, Boolean.class));

    private static final List<Class<?>> COMPARABLE_X_2 = unmodifiableList(Arrays.<Class<?>> asList(Comparable.class, Comparable.class));

    private static final List<Class<?>> COMPARABLE_X_3 = unmodifiableList(Arrays.<Class<?>> asList(Comparable.class, Comparable.class,Comparable.class));

    private static final List<Class<?>> OBJECT_X_2 = unmodifiableList(Arrays.<Class<?>> asList(Object.class, Object.class));

    private static final List<Class<?>> NUMBER_X_2 = unmodifiableList(Arrays.<Class<?>> asList(Number.class, Number.class));

    private static final List<Class<?>> STRING_X_2 = unmodifiableList(Arrays.<Class<?>> asList(String.class, String.class));
    
    private static final List<Class<?>> STRING_X_3 = unmodifiableList(Arrays.<Class<?>> asList(String.class, String.class, String.class));

    // general
    public static final Operator<Boolean> EQ = new OperatorImpl<Boolean>("EQ",OBJECT_X_2);
    
    public static final Operator<Boolean> NE = new OperatorImpl<Boolean>("NE",OBJECT_X_2);
    
    public static final Operator<Boolean> IS_NULL = new OperatorImpl<Boolean>("IS_NULL",Object.class);
    
    public static final Operator<Boolean> IS_NOT_NULL = new OperatorImpl<Boolean>("IS_NOT_NULL",Object.class);    
    
    public static final Operator<Boolean> INSTANCE_OF = new OperatorImpl<Boolean>("INSTANCE_OF");    
    
    public static final Operator<Number>  NUMCAST = new OperatorImpl<Number>("NUMCAST");
    
    public static final Operator<String>  STRING_CAST = new OperatorImpl<String>("STING_CAST",Object.class);
    
    public static final Operator<Object>  ALIAS = new OperatorImpl<Object>("ALIAS");
    
    public static final Operator<Object>  LIST = new OperatorImpl<Object>("LIST");
    
    public static final Operator<Integer> ORDINAL = new OperatorImpl<Integer>("ORDINAL");
    
    public static final Operator<Object>  DELEGATE = new OperatorImpl<Object>("DELEGATE");
    
    public static final Operator<Object>  WRAPPED = new OperatorImpl<Object>("WRAPPED");

    
    // collection
    public static final Operator<Boolean> IN = new OperatorImpl<Boolean>("IN",OBJECT_X_2); // cmp. contains
    
    public static final Operator<Boolean> COL_IS_EMPTY = new OperatorImpl<Boolean>("COL_IS_EMPTY",Object.class);
    
    public static final Operator<Number>  COL_SIZE = new OperatorImpl<Number>("COL_SIZE",Object.class);
    
    
    // array
    public static final Operator<Number>  ARRAY_SIZE = new OperatorImpl<Number>("ARRAY_SIZE",Object.class);

    // map
    public static final Operator<Boolean> CONTAINS_KEY = new OperatorImpl<Boolean>("CONTAINS_KEY",OBJECT_X_2);

    public static final Operator<Boolean> CONTAINS_VALUE = new OperatorImpl<Boolean>("CONTAINS_VALUE",OBJECT_X_2);
    
    public static final Operator<Number>  MAP_SIZE = new OperatorImpl<Number>("MAP_SIZE",Object.class);
    
    public static final Operator<Boolean> MAP_IS_EMPTY = new OperatorImpl<Boolean>("MAP_IS_EMPTY",Object.class);

    // Boolean
    public static final Operator<Boolean> AND = new OperatorImpl<Boolean>("AND",BOOLEAN_X_2);
    
    public static final Operator<Boolean> NOT = new OperatorImpl<Boolean>("NOT",Boolean.class);
    
    public static final Operator<Boolean> OR = new OperatorImpl<Boolean>("OR",BOOLEAN_X_2);
    
    public static final Operator<Boolean> XNOR = new OperatorImpl<Boolean>("XNOR",BOOLEAN_X_2);
    
    public static final Operator<Boolean> XOR = new OperatorImpl<Boolean>("XOR",BOOLEAN_X_2);

    // Comparable
    public static final Operator<Boolean> BETWEEN = new OperatorImpl<Boolean>("BETWEEN",COMPARABLE_X_3);
    
    public static final Operator<Boolean> GOE = new OperatorImpl<Boolean>("GOE",COMPARABLE_X_2);
    
    public static final Operator<Boolean> GT = new OperatorImpl<Boolean>("GT",COMPARABLE_X_2);
    
    public static final Operator<Boolean> LOE = new OperatorImpl<Boolean>("LOE",COMPARABLE_X_2);
    
    public static final Operator<Boolean> LT = new OperatorImpl<Boolean>("LT",COMPARABLE_X_2);

    // Number
    public static final Operator<Number>  NEGATE = new OperatorImpl<Number>("NEGATE",Number.class);
    
    public static final Operator<Number>  ADD = new OperatorImpl<Number>("ADD",NUMBER_X_2);
    
    public static final Operator<Number>  DIV = new OperatorImpl<Number>("DIV",NUMBER_X_2);
    
    public static final Operator<Number>  MULT = new OperatorImpl<Number>("MULT",NUMBER_X_2);
    
    public static final Operator<Number>  SUB = new OperatorImpl<Number>("SUB",NUMBER_X_2);
    
    public static final Operator<Number>  MOD = new OperatorImpl<Number>("MOD",NUMBER_X_2);

    // String
    public static final Operator<Character> CHAR_AT = new OperatorImpl<Character>("CHAR_AT");
    
    public static final Operator<String>  CONCAT = new OperatorImpl<String>("CONCAT",STRING_X_2);
    
    public static final Operator<String>  LOWER = new OperatorImpl<String>("LOWER",String.class);
    
    public static final Operator<String>  SUBSTR_1ARG = new OperatorImpl<String>("SUBSTR");
    
    public static final Operator<String>  SUBSTR_2ARGS = new OperatorImpl<String>("SUBSTR2");
    
    public static final Operator<String>  TRIM = new OperatorImpl<String>("TRIM",String.class);
    
    public static final Operator<String>  UPPER = new OperatorImpl<String>("UPPER",String.class);
    
    public static final Operator<Boolean> MATCHES = new OperatorImpl<Boolean>("MATCHES",STRING_X_2);
    
    public static final Operator<Boolean> MATCHES_IC = new OperatorImpl<Boolean>("MATCHES_IC",STRING_X_2);
    
    public static final Operator<Number>  STRING_LENGTH = new OperatorImpl<Number>("STRING_LENGTH",String.class);
    
    public static final Operator<Boolean> STRING_IS_EMPTY = new OperatorImpl<Boolean>("STRING_IS_EMPTY",String.class);
    
    public static final Operator<Boolean> STARTS_WITH = new OperatorImpl<Boolean>("STARTS_WITH",STRING_X_2);
    
    public static final Operator<Boolean> STARTS_WITH_IC = new OperatorImpl<Boolean>("STATS_WITH_IC",STRING_X_2);
    
    public static final Operator<Number>  INDEX_OF_2ARGS = new OperatorImpl<Number>("INDEX_OF2");
    
    public static final Operator<Number>  INDEX_OF = new OperatorImpl<Number>("INDEX_OF");
    
    public static final Operator<Boolean> EQ_IGNORE_CASE = new OperatorImpl<Boolean>("EQ_IGNORE_CASE",STRING_X_2);
    
    public static final Operator<Boolean> ENDS_WITH = new OperatorImpl<Boolean>("ENDS_WITH",STRING_X_2);
    
    public static final Operator<Boolean> ENDS_WITH_IC = new OperatorImpl<Boolean>("ENDS_WITH_IC",STRING_X_2);
    
    public static final Operator<Boolean> STRING_CONTAINS = new OperatorImpl<Boolean>("STRING_CONTAINS",STRING_X_2);
    
    public static final Operator<Boolean> STRING_CONTAINS_IC = new OperatorImpl<Boolean>("STRING_CONTAINS_IC",STRING_X_2);
    
    public static final Operator<Boolean> LIKE = new OperatorImpl<Boolean>("LIKE",STRING_X_2);
    
    public static final Operator<Boolean> LIKE_ESCAPE = new OperatorImpl<Boolean>("LIKE_ESCAPE",STRING_X_3);

    // case
    public static final Operator<Object>  CASE = new OperatorImpl<Object>("CASE",Object.class);
    
    public static final Operator<Object>  CASE_WHEN = new OperatorImpl<Object>("CASE_WHEN");
    
    public static final Operator<Object>  CASE_ELSE = new OperatorImpl<Object>("CASE_ELSE",Object.class);

    // case for eq
    public static final Operator<Object>  CASE_EQ = new OperatorImpl<Object>("CASE_EQ",Object.class);
    
    public static final Operator<Object>  CASE_EQ_WHEN = new OperatorImpl<Object>("CASE_EQ_WHEN");
    
    public static final Operator<Object>  CASE_EQ_ELSE = new OperatorImpl<Object>("CASE_EQ_ELSE",Object.class);

    // coalesce
    public static final Operator<Object>  COALESCE = new OperatorImpl<Object>("COALESCE",Object.class);
    
    public static final Operator<Object>  NULLIF = new OperatorImpl<Object>("NULLIF", OBJECT_X_2);

    // subquery operations
    public static final Operator<Boolean> EXISTS = new OperatorImpl<Boolean>("EXISTS",Object.class);

    public static final List<Operator<?>> equalsOps = unmodifiableList(Arrays.<Operator<?>> asList(EQ));

    public static final List<Operator<?>> notEqualsOps = unmodifiableList(Arrays.<Operator<?>> asList(NE));

    public static final List<Operator<?>> compareOps = unmodifiableList(Arrays.<Operator<?>> asList(EQ, NE, LT, GT, GOE, LOE));

    /**
     * Aggregation operators
     */
    @SuppressWarnings("unchecked")
    public static final class AggOps{
    
        public static final Operator<Boolean> BOOLEAN_ALL = new OperatorImpl<Boolean>("BOOLEAN_ALL",Boolean.class);
        
        public static final Operator<Boolean> BOOLEAN_ANY = new OperatorImpl<Boolean>("BOOLEAN_ANY",Boolean.class);
        
        public static final Operator<Comparable> MAX_AGG = new OperatorImpl<Comparable>("MAX_AGG",Comparable.class);
        
        public static final Operator<Comparable> MIN_AGG = new OperatorImpl<Comparable>("MIN_AGG",Comparable.class);
        
        public static final Operator<Number> AVG_AGG = new OperatorImpl<Number>("AVG_AGG",Number.class);
        
        public static final Operator<Number> SUM_AGG = new OperatorImpl<Number>("SUM_AGG",Number.class);
        
        public static final Operator<Number> COUNT_AGG = new OperatorImpl<Number>("COUNT_AGG",Object.class);
        
        public static final Operator<Number> COUNT_DISTINCT_AGG = new OperatorImpl<Number>("COUNT_DISTINCT_AGG",Object.class);
        
        public static final Operator<Number> COUNT_ALL_AGG = new OperatorImpl<Number>("COUNT_ALL_AGG");
        
        private AggOps() {}
    }
    
    /**
     * Quantification operators
     */
    @SuppressWarnings("unchecked")
    public static final class QuantOps {
        
        public static final Operator<Comparable> AVG_IN_COL = new OperatorImpl<Comparable>("AVG_IN_COL",Collection.class);
        
        public static final Operator<Comparable> MAX_IN_COL = new OperatorImpl<Comparable>("MAX_IN_COL",Collection.class);
        
        public static final Operator<Comparable> MIN_IN_COL = new OperatorImpl<Comparable>("MIN_IN_COL",Collection.class);

        public static final Operator<Object> ANY = new OperatorImpl<Object>("ANY",Object.class);
        
        public static final Operator<Object> ALL = new OperatorImpl<Object>("ALL",Object.class);
        
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
        
        public static final Operator<Integer> HOUR = new OperatorImpl<Integer>("HOUR",java.util.Date.class);
        
        public static final Operator<Integer> MINUTE = new OperatorImpl<Integer>("MINUTE",java.util.Date.class);
        
        public static final Operator<Integer> MONTH = new OperatorImpl<Integer>("MONTH",java.util.Date.class);
        
        public static final Operator<Integer> SECOND = new OperatorImpl<Integer>("SECOND",java.util.Date.class);
        
        public static final Operator<Integer> MILLISECOND = new OperatorImpl<Integer>("MILLISECOND",java.util.Date.class);
        
        public static final Operator<Comparable> SYSDATE = new OperatorImpl<Comparable>("SYSDATE");
        
        public static final Operator<Integer> YEAR = new OperatorImpl<Integer>("YEAR",java.util.Date.class);
        
        public static final Operator<Integer> YEAR_MONTH = new OperatorImpl<Integer>("YEAR_MONTH",java.util.Date.class);
        
        public static final Operator<Integer> WEEK = new OperatorImpl<Integer>("WEEK",java.util.Date.class);
        
        public static final Operator<Integer> DAY_OF_WEEK = new OperatorImpl<Integer>("DAY_OF_WEEK",java.util.Date.class);
        
        public static final Operator<Integer> DAY_OF_MONTH = new OperatorImpl<Integer>("DAY_OF_MONTH",java.util.Date.class);
        
        public static final Operator<Integer> DAY_OF_YEAR = new OperatorImpl<Integer>("DAY_OF_YEAR",java.util.Date.class);
        
        private DateTimeOps() {}
    }

    /**
     * Math operators
     *
     */
    public static final class MathOps {
        
        public static final Operator<Number> ABS = new OperatorImpl<Number>("ABS",Number.class);
        
        public static final Operator<Number> ACOS = new OperatorImpl<Number>("ACOS",Number.class);
        
        public static final Operator<Number> ASIN = new OperatorImpl<Number>("ASIN",Number.class);
        
        public static final Operator<Number> ATAN = new OperatorImpl<Number>("ATAN",Number.class);
        
        public static final Operator<Number> CEIL = new OperatorImpl<Number>("CEIL",Number.class);
        
        public static final Operator<Number> COS = new OperatorImpl<Number>("COS",Number.class);
        
        public static final Operator<Number> TAN = new OperatorImpl<Number>("TAN",Number.class);
        
        public static final Operator<Number> SQRT = new OperatorImpl<Number>("SQRT",Number.class);
        
        public static final Operator<Number> SIN = new OperatorImpl<Number>("SIN",Number.class);
        
        public static final Operator<Number> ROUND = new OperatorImpl<Number>("ROUND",Number.class);
        
        public static final Operator<Number> RANDOM = new OperatorImpl<Number>("RANDOM");
        
        public static final Operator<Number> POWER = new OperatorImpl<Number>("POWER",NUMBER_X_2);
        
        public static final Operator<Number> MIN = new OperatorImpl<Number>("MIN",NUMBER_X_2);
        
        public static final Operator<Number> MAX = new OperatorImpl<Number>("MAX",NUMBER_X_2);
        
        public static final Operator<Number> LOG = new OperatorImpl<Number>("LOG",NUMBER_X_2);
        
        public static final Operator<Number> FLOOR = new OperatorImpl<Number>("FLOOR",Number.class);
        
        public static final Operator<Number> EXP = new OperatorImpl<Number>("EXP",Number.class);
        
        public static final Operator<Number> COSH = new OperatorImpl<Number>("COSH",Number.class);
        
        public static final Operator<Number> COT = new OperatorImpl<Number>("COT",Number.class);
        
        public static final Operator<Number> COTH = new OperatorImpl<Number>("COTH",Number.class);
        
        public static final Operator<Number> DEG = new OperatorImpl<Number>("DEG",Number.class);
        
        public static final Operator<Number> LN = new OperatorImpl<Number>("LN",Number.class);
        
        public static final Operator<Number> RAD = new OperatorImpl<Number>("RAD",Number.class);
        
        public static final Operator<Number> SIGN = new OperatorImpl<Number>("SIGN",Number.class);
        
        public static final Operator<Number> SINH = new OperatorImpl<Number>("SINH",Number.class);
        
        public static final Operator<Number> TANH = new OperatorImpl<Number>("TANH",Number.class);
        
        private MathOps() {}
    }

    /**
     * String operators
     */
    public static final class StringOps {
        
        public static final Operator<String> LTRIM = new OperatorImpl<String>("LTRIM",String.class);
        
        public static final Operator<String> RTRIM = new OperatorImpl<String>("RTRIM",String.class);
        
        public static final Operator<String> LPAD = new OperatorImpl<String>("LPAD",String.class, Integer.class);
        
        public static final Operator<String> RPAD = new OperatorImpl<String>("RPAD",String.class, Integer.class);
        
        public static final Operator<String> LPAD2 = new OperatorImpl<String>("LPAD2",String.class, Integer.class, Character.class);
        
        public static final Operator<String> RPAD2 = new OperatorImpl<String>("RPAD2",String.class, Integer.class, Character.class);
        
        public static final Operator<Number> LOCATE = new OperatorImpl<Number>("LOCATE", STRING_X_2);
        
        public static final Operator<Number> LOCATE2 = new OperatorImpl<Number>("LOCATE2", String.class, String.class, Integer.class);
        
        private StringOps() {}
    }

    
    private Ops() {}
}
