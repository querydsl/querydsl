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
    Op<java.lang.Boolean> EQ = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> IN = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> ISNOTNULL = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> ISNULL = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> ISTYPEOF = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> NE = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> NOTIN =  new Op<java.lang.Boolean>();
    
    // Boolean
    Op<java.lang.Boolean> AND = new Op<java.lang.Boolean>(); 
    Op<java.lang.Boolean> NOT = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> OR = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> XNOR = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> XOR = new Op<java.lang.Boolean>();
     
    // Comparable
    Op<java.lang.Boolean> BETWEEN = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> GOE = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> GT = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> LOE = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> LT = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> NOTBETWEEN = new Op<java.lang.Boolean>();
    
    // Date
    Op<java.lang.Boolean> AFTER = new Op<java.lang.Boolean>();
    Op<java.lang.Boolean> BEFORE = new Op<java.lang.Boolean>();    
     
    // Number
    Op<java.lang.Number> ADD = new Op<java.lang.Number>();          
    Op<java.lang.Number> DIV = new Op<java.lang.Number>();        
    Op<java.lang.Number> MOD = new Op<java.lang.Number>();
    Op<java.lang.Number> MULT = new Op<java.lang.Number>();
    Op<java.lang.Number> SQRT = new Op<java.lang.Number>();
    Op<java.lang.Number> SUB = new Op<java.lang.Number>();
    
    // String
    Op<java.lang.String> CONCAT = new Op<java.lang.String>();
    Op<java.lang.Boolean> LIKE = new Op<java.lang.Boolean>();
    Op<java.lang.String> LOWER = new Op<java.lang.String>();
    Op<java.lang.String> SUBSTR1ARG = new Op<java.lang.String>();
    Op<java.lang.String> SUBSTR2ARGS = new Op<java.lang.String>();
    Op<java.lang.String> TRIM = new Op<java.lang.String>();
    Op<java.lang.String> UPPER = new Op<java.lang.String>();
       
}
