/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
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
 * @version $Id$
 */
public interface Ops {

    List<Class<?>> BOOLEAN_X_2 = unmodifiableList(Arrays.<Class<?>> asList(Boolean.class, Boolean.class));

    List<Class<?>> COMPARABLE_X_2 = unmodifiableList(Arrays.<Class<?>> asList(Comparable.class, Comparable.class));

    List<Class<?>> COMPARABLE_X_3 = unmodifiableList(Arrays.<Class<?>> asList(Comparable.class, Comparable.class,Comparable.class));

    List<Class<?>> OBJECT_X_2 = unmodifiableList(Arrays.<Class<?>> asList(Object.class, Object.class));

    List<Class<?>> NUMBER_X_2 = unmodifiableList(Arrays.<Class<?>> asList(Number.class, Number.class));

    List<Class<?>> STRING_X_2 = unmodifiableList(Arrays.<Class<?>> asList(String.class, String.class));

    // general
    Operator<Boolean> EQ_PRIMITIVE = new OperatorImpl<Boolean>("EQ_PRIMITIVE",OBJECT_X_2);
    Operator<Boolean> EQ_OBJECT = new OperatorImpl<Boolean>("EQ_OBJECT",OBJECT_X_2);
    Operator<Boolean> IS_NOT_NULL = new OperatorImpl<Boolean>("IS_NOT_NULL",Object.class);
    Operator<Boolean> IS_NULL = new OperatorImpl<Boolean>("IS_NULL",Object.class);
    Operator<Boolean> INSTANCE_OF = new OperatorImpl<Boolean>("INSTANCE_OF");
    Operator<Boolean> NE_PRIMITIVE = new OperatorImpl<Boolean>("NE_PRIMITIVE",OBJECT_X_2);
    Operator<Boolean> NE_OBJECT = new OperatorImpl<Boolean>("NE_OBJECT",OBJECT_X_2);
    Operator<Number> NUMCAST = new OperatorImpl<Number>("NUMCAST");
    Operator<String> STRING_CAST = new OperatorImpl<String>("STING_CAST",Object.class);
    Operator<Object> ALIAS = new OperatorImpl<Object>("ALIAS");
    Operator<Object> LIST = new OperatorImpl<Object>("LIST");
    Operator<Integer> ORDINAL = new OperatorImpl<Integer>("ORDINAL");
    Operator<Object> DELEGATE = new OperatorImpl<Object>("DELEGATE");

    // collection
    Operator<Boolean> IN = new OperatorImpl<Boolean>("IN",OBJECT_X_2); // cmp. contains
    Operator<Boolean> COL_IS_EMPTY = new OperatorImpl<Boolean>("COL_IS_EMPTY",Object.class);
    Operator<Number> COL_SIZE = new OperatorImpl<Number>("COL_SIZE",Object.class);

    // array
    Operator<Number> ARRAY_SIZE = new OperatorImpl<Number>("ARRAY_SIZE",Object.class);

    // map
    Operator<Boolean> CONTAINS_KEY = new OperatorImpl<Boolean>("CONTAINS_KEY",OBJECT_X_2);
    Operator<Boolean> CONTAINS_VALUE = new OperatorImpl<Boolean>("CONTAINS_VALUE",OBJECT_X_2);
    Operator<Number> MAP_SIZE = new OperatorImpl<Number>("MAP_SIZE",Object.class);
    Operator<Boolean> MAP_IS_EMPTY = new OperatorImpl<Boolean>("MAP_IS_EMPTY",Object.class);

    // Boolean
    Operator<Boolean> AND = new OperatorImpl<Boolean>("AND",BOOLEAN_X_2);
    Operator<Boolean> NOT = new OperatorImpl<Boolean>("NOT",Boolean.class);
    Operator<Boolean> OR = new OperatorImpl<Boolean>("OR",BOOLEAN_X_2);
    Operator<Boolean> XNOR = new OperatorImpl<Boolean>("XNOR",BOOLEAN_X_2);
    Operator<Boolean> XOR = new OperatorImpl<Boolean>("XOR",BOOLEAN_X_2);

    // Comparable
    Operator<Boolean> BETWEEN = new OperatorImpl<Boolean>("BETWEEN",COMPARABLE_X_3);
    Operator<Boolean> GOE = new OperatorImpl<Boolean>("GOE",COMPARABLE_X_2);
    Operator<Boolean> GT = new OperatorImpl<Boolean>("GT",COMPARABLE_X_2);
    Operator<Boolean> LOE = new OperatorImpl<Boolean>("LOE",COMPARABLE_X_2);
    Operator<Boolean> LT = new OperatorImpl<Boolean>("LT",COMPARABLE_X_2);

    // Date / Comparable
    Operator<Boolean> AFTER = new OperatorImpl<Boolean>("AFTER",COMPARABLE_X_2);
    Operator<Boolean> BEFORE = new OperatorImpl<Boolean>("BEFORE",COMPARABLE_X_2);
    Operator<Boolean> AOE = new OperatorImpl<Boolean>("AOE",COMPARABLE_X_2);
    Operator<Boolean> BOE = new OperatorImpl<Boolean>("BOE",COMPARABLE_X_2);

    // Number
    Operator<Number> ADD = new OperatorImpl<Number>("ADD",NUMBER_X_2);
    Operator<Number> DIV = new OperatorImpl<Number>("DIV",NUMBER_X_2);
    Operator<Number> MULT = new OperatorImpl<Number>("MULT",NUMBER_X_2);
    Operator<Number> SUB = new OperatorImpl<Number>("SUB",NUMBER_X_2);
    Operator<Number> MOD = new OperatorImpl<Number>("MOD",NUMBER_X_2);

    // String
    Operator<Character> CHAR_AT = new OperatorImpl<Character>("CHAR_AT");
    Operator<String> CONCAT = new OperatorImpl<String>("CONCAT",STRING_X_2);
    Operator<String> LOWER = new OperatorImpl<String>("LOWER",String.class);
    Operator<String> SUBSTR_1ARG = new OperatorImpl<String>("SUBSTR");
    Operator<String> SUBSTR_2ARGS = new OperatorImpl<String>("SUBSTR2");
    Operator<String> TRIM = new OperatorImpl<String>("TRIM",String.class);
    Operator<String> UPPER = new OperatorImpl<String>("UPPER",String.class);
    Operator<Boolean> MATCHES = new OperatorImpl<Boolean>("MATCHES",STRING_X_2);
    Operator<Number> STRING_LENGTH = new OperatorImpl<Number>("STRING_LENGTH",String.class);
    Operator<Boolean> STRING_IS_EMPTY = new OperatorImpl<Boolean>("STRING_IS_EMPTY",String.class);
    Operator<Boolean> STARTS_WITH = new OperatorImpl<Boolean>("STARTS_WITH",STRING_X_2);
    Operator<Boolean> STARTS_WITH_IC = new OperatorImpl<Boolean>("STATS_WITH_IC",STRING_X_2);
    Operator<Number> INDEX_OF_2ARGS = new OperatorImpl<Number>("INDEX_OF2");
    Operator<Number> INDEX_OF = new OperatorImpl<Number>("INDEX_OF");
    Operator<Boolean> EQ_IGNORE_CASE = new OperatorImpl<Boolean>("EQ_IGNORE_CASE",STRING_X_2);
    Operator<Boolean> ENDS_WITH = new OperatorImpl<Boolean>("ENDS_WITH",STRING_X_2);
    Operator<Boolean> ENDS_WITH_IC = new OperatorImpl<Boolean>("ENDS_WITH_IC",STRING_X_2);
    Operator<Boolean> STRING_CONTAINS = new OperatorImpl<Boolean>("STRING_CONTAINS",STRING_X_2);
    Operator<Boolean> STRING_CONTAINS_IC = new OperatorImpl<Boolean>("STRING_CONTAINS_IC",STRING_X_2);
    Operator<Boolean> LIKE = new OperatorImpl<Boolean>("LIKE",STRING_X_2);

    // case
    Operator<Object> CASE = new OperatorImpl<Object>("CASE",Object.class);
    Operator<Object> CASE_WHEN = new OperatorImpl<Object>("CASE_WHEN");
    Operator<Object> CASE_ELSE = new OperatorImpl<Object>("CASE_ELSE",Object.class);

    // case for eq
    Operator<Object> CASE_EQ = new OperatorImpl<Object>("CASE_EQ",Object.class);
    Operator<Object> CASE_EQ_WHEN = new OperatorImpl<Object>("CASE_EQ_WHEN");
    Operator<Object> CASE_EQ_ELSE = new OperatorImpl<Object>("CASE_EQ_ELSE",Object.class);

    // coalesce
    Operator<Object> COALESCE = new OperatorImpl<Object>("COALESCE",Object.class);

    // subquery operations
    Operator<Boolean> EXISTS = new OperatorImpl<Boolean>("EXISTS",Object.class);

    List<Operator<?>> equalsOps = unmodifiableList(Arrays.<Operator<?>> asList(EQ_OBJECT, EQ_PRIMITIVE));

    List<Operator<?>> notEqualsOps = unmodifiableList(Arrays.<Operator<?>> asList(NE_OBJECT, NE_PRIMITIVE));

    List<Operator<?>> compareOps = unmodifiableList(Arrays.<Operator<?>> asList(EQ_OBJECT, EQ_PRIMITIVE,LT, GT, GOE, LOE));

    /**
     * Aggreation operators
     */
    @SuppressWarnings("unchecked")
    interface AggOps{
        Operator<Comparable> MAX_AGG = new OperatorImpl<Comparable>("MAX_AGG",Comparable.class);
        Operator<Comparable> MIN_AGG = new OperatorImpl<Comparable>("MIN_AGG",Comparable.class);
        Operator<Number> AVG_AGG = new OperatorImpl<Number>("AVG_AGG",Number.class);
        Operator<Number> SUM_AGG = new OperatorImpl<Number>("SUM_AGG",Number.class);
        Operator<Number> COUNT_AGG = new OperatorImpl<Number>("COUNT_AGG",Object.class);
        Operator<Number> COUNT_DISTINCT_AGG = new OperatorImpl<Number>("COUNT_DISTINCT_AGG",Object.class);
        Operator<Number> COUNT_ALL_AGG = new OperatorImpl<Number>("COUNT_ALL_AGG");
    }

    /**
     * Date and time operators
     */
    @SuppressWarnings("unchecked")
    interface DateTimeOps {
        Operator<Comparable> CURRENT_DATE = new OperatorImpl<Comparable>("CURRENT_DATE");
        Operator<Comparable> CURRENT_TIME = new OperatorImpl<Comparable>("CURRENT_TIME");
        Operator<Comparable> CURRENT_TIMESTAMP = new OperatorImpl<Comparable>("CURRENT_TIMESTAMP");
        Operator<Integer> HOUR = new OperatorImpl<Integer>("HOUR",java.util.Date.class);
        Operator<Integer> MINUTE = new OperatorImpl<Integer>("MINUTE",java.util.Date.class);
        Operator<Integer> MONTH = new OperatorImpl<Integer>("MONTH",java.util.Date.class);
        Operator<Integer> SECOND = new OperatorImpl<Integer>("SECOND",java.util.Date.class);
        Operator<Integer> MILLISECOND = new OperatorImpl<Integer>("MILLISECOND",java.util.Date.class);
        Operator<Comparable> SYSDATE = new OperatorImpl<Comparable>("SYSDATE");
        Operator<Integer> YEAR = new OperatorImpl<Integer>("YEAR",java.util.Date.class);
        Operator<Integer> YEAR_MONTH = new OperatorImpl<Integer>("YEAR_MONTH",java.util.Date.class);
        Operator<Integer> WEEK = new OperatorImpl<Integer>("WEEK",java.util.Date.class);
        Operator<Integer> DAY_OF_WEEK = new OperatorImpl<Integer>("DAY_OF_WEEK",java.util.Date.class);
        Operator<Integer> DAY_OF_MONTH = new OperatorImpl<Integer>("DAY_OF_MONTH",java.util.Date.class);
        Operator<Integer> DAY_OF_YEAR = new OperatorImpl<Integer>("DAY_OF_YEAR",java.util.Date.class);
    }

    /**
     * Math operators
     *
     */
    interface MathOps {
        Operator<Number> ABS = new OperatorImpl<Number>("ABS",Number.class);
        Operator<Number> ACOS = new OperatorImpl<Number>("ACOS",Number.class);
        Operator<Number> ASIN = new OperatorImpl<Number>("ASIN",Number.class);
        Operator<Number> ATAN = new OperatorImpl<Number>("ATAN",Number.class);
        Operator<Number> CEIL = new OperatorImpl<Number>("CEIL",Number.class);
        Operator<Number> COS = new OperatorImpl<Number>("COS",Number.class);
        Operator<Number> TAN = new OperatorImpl<Number>("TAN",Number.class);
        Operator<Number> SQRT = new OperatorImpl<Number>("SQRT",Number.class);
        Operator<Number> SIN = new OperatorImpl<Number>("SIN",Number.class);
        Operator<Number> ROUND = new OperatorImpl<Number>("ROUND",Number.class);
        Operator<Number> RANDOM = new OperatorImpl<Number>("RANDOM");
        Operator<Number> POWER = new OperatorImpl<Number>("POWER",NUMBER_X_2);
        Operator<Number> MIN = new OperatorImpl<Number>("MIN",NUMBER_X_2);
        Operator<Number> MAX = new OperatorImpl<Number>("MAX",NUMBER_X_2);
        Operator<Number> LOG10 = new OperatorImpl<Number>("LOG10",Number.class);
        Operator<Number> LOG = new OperatorImpl<Number>("LOG",Number.class);
        Operator<Number> FLOOR = new OperatorImpl<Number>("FLOOR",Number.class);
        Operator<Number> EXP = new OperatorImpl<Number>("EXP",Number.class);
    }

    /**
     * String operators
     */
    interface StringOps {
        Operator<String> LTRIM = new OperatorImpl<String>("LTRIM",String.class);
        Operator<String> RTRIM = new OperatorImpl<String>("RTRIM",String.class);
        Operator<String> SPACE = new OperatorImpl<String>("SPACE",Integer.class);
        Operator<String[]> SPLIT = new OperatorImpl<String[]>("SPLIT",STRING_X_2);
        Operator<Number> LAST_INDEX_2ARGS = new OperatorImpl<Number>("LAST_INDEX2");
        Operator<Number> LAST_INDEX = new OperatorImpl<Number>("LAST_INDEX",STRING_X_2);
    }

    /**
     * Quantification operators
     */
    @SuppressWarnings("unchecked")
    interface QuantOps {
        Operator<Comparable> AVG_IN_COL = new OperatorImpl<Comparable>("AVG_IN_COL",Collection.class);
        Operator<Comparable> MAX_IN_COL = new OperatorImpl<Comparable>("MAX_IN_COL",Collection.class);
        Operator<Comparable> MIN_IN_COL = new OperatorImpl<Comparable>("MIN_IN_COL",Collection.class);

        // some / any = true for any
        // all = true for all
        // exists = true is subselect matches
        // not exists = true if subselect doesn't match
        Operator<Object> ANY = new OperatorImpl<Object>("ANY",Object.class);
        Operator<Object> ALL = new OperatorImpl<Object>("ALL",Object.class);
    }
}
