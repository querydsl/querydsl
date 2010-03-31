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
    Operator<Boolean> EQ_PRIMITIVE = new OperatorImpl<Boolean>(OBJECT_X_2);
    Operator<Boolean> EQ_OBJECT = new OperatorImpl<Boolean>(OBJECT_X_2);
    Operator<Boolean> IS_NOT_NULL = new OperatorImpl<Boolean>(Object.class);
    Operator<Boolean> IS_NULL = new OperatorImpl<Boolean>(Object.class);
    Operator<Boolean> INSTANCE_OF = new OperatorImpl<Boolean>();
    Operator<Boolean> NE_PRIMITIVE = new OperatorImpl<Boolean>(OBJECT_X_2);
    Operator<Boolean> NE_OBJECT = new OperatorImpl<Boolean>(OBJECT_X_2);
    Operator<Number> NUMCAST = new OperatorImpl<Number>();
    Operator<String> STRING_CAST = new OperatorImpl<String>(Object.class);
    Operator<Object> ALIAS = new OperatorImpl<Object>();
    Operator<Object> LIST = new OperatorImpl<Object>();
        
    // collection
    Operator<Boolean> IN = new OperatorImpl<Boolean>(OBJECT_X_2); // cmp. contains
    Operator<Boolean> COL_IS_EMPTY = new OperatorImpl<Boolean>(Object.class);
    Operator<Number> COL_SIZE = new OperatorImpl<Number>(Object.class);

    // array
    Operator<Number> ARRAY_SIZE = new OperatorImpl<Number>(Object.class);
    
    // map
    Operator<Boolean> CONTAINS_KEY = new OperatorImpl<Boolean>(OBJECT_X_2);
    Operator<Boolean> CONTAINS_VALUE = new OperatorImpl<Boolean>(OBJECT_X_2);
    Operator<Number> MAP_SIZE = new OperatorImpl<Number>(Object.class);
    Operator<Boolean> MAP_ISEMPTY = new OperatorImpl<Boolean>(Object.class);
    
    // Boolean
    Operator<Boolean> AND = new OperatorImpl<Boolean>(BOOLEAN_X_2);
    Operator<Boolean> NOT = new OperatorImpl<Boolean>(Boolean.class);
    Operator<Boolean> OR = new OperatorImpl<Boolean>(BOOLEAN_X_2);
    Operator<Boolean> XNOR = new OperatorImpl<Boolean>(BOOLEAN_X_2);
    Operator<Boolean> XOR = new OperatorImpl<Boolean>(BOOLEAN_X_2);
    
    // Comparable
    Operator<Boolean> BETWEEN = new OperatorImpl<Boolean>(COMPARABLE_X_3);
    Operator<Boolean> GOE = new OperatorImpl<Boolean>(COMPARABLE_X_2);
    Operator<Boolean> GT = new OperatorImpl<Boolean>(COMPARABLE_X_2);
    Operator<Boolean> LOE = new OperatorImpl<Boolean>(COMPARABLE_X_2);
    Operator<Boolean> LT = new OperatorImpl<Boolean>(COMPARABLE_X_2);
    
    // Date / Comparable
    Operator<Boolean> AFTER = new OperatorImpl<Boolean>(COMPARABLE_X_2);
    Operator<Boolean> BEFORE = new OperatorImpl<Boolean>(COMPARABLE_X_2);
    Operator<Boolean> AOE = new OperatorImpl<Boolean>(COMPARABLE_X_2);
    Operator<Boolean> BOE = new OperatorImpl<Boolean>(COMPARABLE_X_2);

    // Number
    Operator<Number> ADD = new OperatorImpl<Number>(NUMBER_X_2);
    Operator<Number> DIV = new OperatorImpl<Number>(NUMBER_X_2);
    Operator<Number> MULT = new OperatorImpl<Number>(NUMBER_X_2);
    Operator<Number> SUB = new OperatorImpl<Number>(NUMBER_X_2);
    Operator<Number> MOD = new OperatorImpl<Number>(NUMBER_X_2);

    // String
    Operator<Character> CHAR_AT = new OperatorImpl<Character>();
    Operator<String> CONCAT = new OperatorImpl<String>(STRING_X_2);
    Operator<String> LOWER = new OperatorImpl<String>(String.class);
    Operator<String> SUBSTR_1ARG = new OperatorImpl<String>();
    Operator<String> SUBSTR_2ARGS = new OperatorImpl<String>();
    Operator<String> TRIM = new OperatorImpl<String>(String.class);
    Operator<String> UPPER = new OperatorImpl<String>(String.class);
    Operator<Boolean> MATCHES = new OperatorImpl<Boolean>(STRING_X_2);
    Operator<Number> STRING_LENGTH = new OperatorImpl<Number>(String.class);
    Operator<Boolean> STRING_IS_EMPTY = new OperatorImpl<Boolean>(String.class);
    Operator<Boolean> STARTS_WITH = new OperatorImpl<Boolean>(STRING_X_2);
    Operator<Boolean> STARTS_WITH_IC = new OperatorImpl<Boolean>(STRING_X_2);
    Operator<Number> INDEX_OF_2ARGS = new OperatorImpl<Number>();
    Operator<Number> INDEX_OF = new OperatorImpl<Number>();
    Operator<Boolean> EQ_IGNORE_CASE = new OperatorImpl<Boolean>(STRING_X_2);
    Operator<Boolean> ENDS_WITH = new OperatorImpl<Boolean>(STRING_X_2);
    Operator<Boolean> ENDS_WITH_IC = new OperatorImpl<Boolean>(STRING_X_2);    
    Operator<Boolean> STRING_CONTAINS = new OperatorImpl<Boolean>(STRING_X_2);
    Operator<Boolean> STRING_CONTAINS_IC = new OperatorImpl<Boolean>(STRING_X_2);
    Operator<Boolean> LIKE = new OperatorImpl<Boolean>(STRING_X_2);
        
    // case
    Operator<Object> CASE = new OperatorImpl<Object>(Object.class);
    Operator<Object> CASE_WHEN = new OperatorImpl<Object>();
    Operator<Object> CASE_ELSE = new OperatorImpl<Object>(Object.class);
    
    // case for eq
    Operator<Object> CASE_EQ = new OperatorImpl<Object>(Object.class);
    Operator<Object> CASE_EQ_WHEN = new OperatorImpl<Object>();
    Operator<Object> CASE_EQ_ELSE = new OperatorImpl<Object>(Object.class);
    
    // coalesce
    Operator<Object> COALESCE = new OperatorImpl<Object>(Object.class);
    
    // subquery operations
    Operator<Boolean> EXISTS = new OperatorImpl<Boolean>(Object.class);

    List<Operator<?>> equalsOps = unmodifiableList(Arrays.<Operator<?>> asList(EQ_OBJECT, EQ_PRIMITIVE));

    List<Operator<?>> notEqualsOps = unmodifiableList(Arrays.<Operator<?>> asList(NE_OBJECT, NE_PRIMITIVE));

    List<Operator<?>> compareOps = unmodifiableList(Arrays.<Operator<?>> asList(EQ_OBJECT, EQ_PRIMITIVE,LT, GT, GOE, LOE));

    /**
     * Aggreation operators
     */
    @SuppressWarnings("unchecked")
    interface AggOps{
        Operator<Comparable> MAX_AGG = new OperatorImpl<Comparable>(Comparable.class);
        Operator<Comparable> MIN_AGG = new OperatorImpl<Comparable>(Comparable.class);        
        Operator<Number> AVG_AGG = new OperatorImpl<Number>(Number.class);        
        Operator<Number> SUM_AGG = new OperatorImpl<Number>(Number.class);
        Operator<Number> COUNT_AGG = new OperatorImpl<Number>(Object.class);
        Operator<Number> COUNT_DISTINCT_AGG = new OperatorImpl<Number>(Object.class);
        Operator<Number> COUNT_ALL_AGG = new OperatorImpl<Number>();
//        ENumber<Long> COUNT_ALL_AGG_EXPR = ONumber.create(Long.class, COUNT_ALL_AGG);
    }
    
    /**
     * Date and time operators
     */
    @SuppressWarnings("unchecked")
    interface DateTimeOps {        
        Operator<Comparable> CURRENT_DATE = new OperatorImpl<Comparable>();
        Operator<Comparable> CURRENT_TIME = new OperatorImpl<Comparable>();
        Operator<Comparable> CURRENT_TIMESTAMP = new OperatorImpl<Comparable>();
        Operator<Integer> HOUR = new OperatorImpl<Integer>(java.util.Date.class);
        Operator<Integer> MINUTE = new OperatorImpl<Integer>(java.util.Date.class);
        Operator<Integer> MONTH = new OperatorImpl<Integer>(java.util.Date.class);
        Operator<Integer> SECOND = new OperatorImpl<Integer>(java.util.Date.class);
        Operator<Integer> MILLISECOND = new OperatorImpl<Integer>(java.util.Date.class);
        Operator<Comparable> SYSDATE = new OperatorImpl<Comparable>();
        Operator<Integer> YEAR = new OperatorImpl<Integer>(java.util.Date.class);
        Operator<Integer> YEAR_MONTH = new OperatorImpl<Integer>(java.util.Date.class);
        Operator<Integer> WEEK = new OperatorImpl<Integer>(java.util.Date.class);
        Operator<Integer> DAY_OF_WEEK = new OperatorImpl<Integer>(java.util.Date.class);
        Operator<Integer> DAY_OF_MONTH = new OperatorImpl<Integer>(java.util.Date.class);
        Operator<Integer> DAY_OF_YEAR = new OperatorImpl<Integer>(java.util.Date.class);
    }

    /**
     * Math operators
     * 
     */
    interface MathOps {
        Operator<Number> ABS = new OperatorImpl<Number>(Number.class);
        Operator<Number> ACOS = new OperatorImpl<Number>(Number.class);
        Operator<Number> ASIN = new OperatorImpl<Number>(Number.class);
        Operator<Number> ATAN = new OperatorImpl<Number>(Number.class);
        Operator<Number> CEIL = new OperatorImpl<Number>(Number.class);
        Operator<Number> COS = new OperatorImpl<Number>(Number.class);
        Operator<Number> TAN = new OperatorImpl<Number>(Number.class);
        Operator<Number> SQRT = new OperatorImpl<Number>(Number.class);
        Operator<Number> SIN = new OperatorImpl<Number>(Number.class);
        Operator<Number> ROUND = new OperatorImpl<Number>(Number.class);
        Operator<Number> RANDOM = new OperatorImpl<Number>();
        Operator<Number> POWER = new OperatorImpl<Number>(NUMBER_X_2);
        Operator<Number> MIN = new OperatorImpl<Number>(NUMBER_X_2);
        Operator<Number> MAX = new OperatorImpl<Number>(NUMBER_X_2);
        Operator<Number> LOG10 = new OperatorImpl<Number>(Number.class);
        Operator<Number> LOG = new OperatorImpl<Number>(Number.class);
        Operator<Number> FLOOR = new OperatorImpl<Number>(Number.class);        
        Operator<Number> EXP = new OperatorImpl<Number>(Number.class);        
    }

    /**
     * String operators
     */
    interface StringOps {
        Operator<String> LTRIM = new OperatorImpl<String>(String.class);
        Operator<String> RTRIM = new OperatorImpl<String>(String.class);
        Operator<String> SPACE = new OperatorImpl<String>(Integer.class);
        Operator<String[]> SPLIT = new OperatorImpl<String[]>(STRING_X_2);
        Operator<Number> LAST_INDEX_2ARGS = new OperatorImpl<Number>();
        Operator<Number> LAST_INDEX = new OperatorImpl<Number>(STRING_X_2);
    }
    
    /**
     * Quantification operators
     */
    @SuppressWarnings("unchecked")
    interface QuantOps {
        Operator<Comparable> AVG_IN_COL = new OperatorImpl<Comparable>(Collection.class);
        Operator<Comparable> MAX_IN_COL = new OperatorImpl<Comparable>(Collection.class);
        Operator<Comparable> MIN_IN_COL = new OperatorImpl<Comparable>(Collection.class);

        // some / any = true for any
        // all = true for all
        // exists = true is subselect matches
        // not exists = true if subselect doesn't match
        Operator<Object> ANY = new OperatorImpl<Object>(Object.class);
        Operator<Object> ALL = new OperatorImpl<Object>(Object.class);        
    }
}
