/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

/**
 * Ops provides the operators for the fluent query grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Ops {

    /**
     * The Class Op.
     */
    public static class Op<RT>{}
    
    // general
    Op<Boolean> EQ_PRIMITIVE = new Op<Boolean>();
    Op<Boolean> EQ_OBJECT = new Op<Boolean>();
    Op<Boolean> IN = new Op<Boolean>();
    Op<Boolean> ISNOTNULL = new Op<Boolean>();
    Op<Boolean> ISNULL = new Op<Boolean>();
    Op<Boolean> ISTYPEOF = new Op<Boolean>();
    Op<Boolean> NE_PRIMITIVE = new Op<Boolean>();
    Op<Boolean> NE_OBJECT = new Op<Boolean>();
    Op<Boolean> NOTIN =  new Op<Boolean>();
    
    // Boolean
    Op<Boolean> AND = new Op<Boolean>(); 
    Op<Boolean> NOT = new Op<Boolean>();
    Op<Boolean> OR = new Op<Boolean>();
    Op<Boolean> XNOR = new Op<Boolean>();
    Op<Boolean> XOR = new Op<Boolean>();
     
    // Comparable
    Op<Boolean> BETWEEN = new Op<Boolean>();
    Op<Boolean> GOE = new Op<Boolean>();
    Op<Boolean> GT = new Op<Boolean>();
    Op<Boolean> LOE = new Op<Boolean>();
    Op<Boolean> LT = new Op<Boolean>();
    Op<Boolean> NOTBETWEEN = new Op<Boolean>();
    
    // Date
    Op<Boolean> AFTER = new Op<Boolean>();
    Op<Boolean> BEFORE = new Op<Boolean>();    
     
    // Number
    Op<Number> ADD = new Op<Number>();          
    Op<Number> DIV = new Op<Number>();        
    Op<Number> MOD = new Op<Number>();
    Op<Number> MULT = new Op<Number>();
    Op<Number> SQRT = new Op<Number>();
    Op<Number> SUB = new Op<Number>();
    
    // String
    Op<Character> CHAR_AT = new Op<Character>();
    Op<String> CONCAT = new Op<String>();
    Op<Boolean> LIKE = new Op<Boolean>();
    Op<String> LOWER = new Op<String>();
    Op<String> SUBSTR1ARG = new Op<String>();
    Op<String> SUBSTR2ARGS = new Op<String>();
    Op<String> SPLIT = new Op<String>();
    Op<String> TRIM = new Op<String>();
    Op<String> UPPER = new Op<String>();
    Op<Boolean> MATCHES = new Op<Boolean>();
    Op<Number> STRING_LENGTH = new Op<Number>();  
    Op<Number> LAST_INDEX_2ARGS = new Op<Number>();  
    Op<Number> LAST_INDEX = new Op<Number>();  
    Op<Boolean> ISEMPTY = new Op<Boolean>();
    Op<Boolean> STARTSWITH = new Op<Boolean>();
    Op<Number> INDEXOF_2ARGS = new Op<Number>();  
    Op<Number> INDEXOF = new Op<Number>();  
    Op<Boolean> EQ_IGNORECASE = new Op<Boolean>();
    Op<Boolean> ENDSWITH = new Op<Boolean>();
    Op<Boolean> CONTAINS = new Op<Boolean>();
}
