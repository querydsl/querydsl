/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

/**
 * Ops provides the operators for the fluent query grammar
 *
 * @author tiwe
 * @version $Id$
 */
public class Ops {

    /**
     * Operators (the return type is encoded in the 1st generic parameter)
     */
    public interface Op<RT> {
        Op<Boolean> EQ = new OpImpl<Boolean>();
        Op<Boolean> INARRAY = new OpImpl<Boolean>();
        Op<Boolean> INELEMENTS = new OpImpl<Boolean>();
        Op<Boolean> ISNOTNULL = new OpImpl<Boolean>();
        Op<Boolean> ISNULL = new OpImpl<Boolean>();
        Op<Boolean> ISTYPEOF = new OpImpl<Boolean>();
        Op<Boolean> NE = new OpImpl<Boolean>();
    }
    
    /**
     * Boolean operators (operators used with boolean operands)
     */
    public interface OpBoolean<RT> extends OpComparable<RT>{ 
        OpBoolean<Boolean> AND = new OpBooleanImpl<Boolean>(); 
        OpBoolean<Boolean> NOT = new OpBooleanImpl<Boolean>();
        OpBoolean<Boolean> OR = new OpBooleanImpl<Boolean>();
        OpBoolean<Boolean> XNOR = new OpBooleanImpl<Boolean>();
        OpBoolean<Boolean> XOR = new OpBooleanImpl<Boolean>();
    }    
        
    static class OpBooleanImpl<RT> implements OpBoolean<RT>{}
    
    /**
     * Operators for Comparable objects
     */
    public interface OpComparable<RT> extends Op<RT>{
        OpComparable<Boolean> BETWEEN = new OpComparableImpl<Boolean>();
        OpComparable<Boolean> GOE = new OpComparableImpl<Boolean>();
        OpComparable<Boolean> GT = new OpComparableImpl<Boolean>();
        OpComparable<Boolean> LOE = new OpComparableImpl<Boolean>();
        OpComparable<Boolean> LT = new OpComparableImpl<Boolean>();
    }
    
    static class OpComparableImpl<RT> implements OpComparable<RT> {}
    
    /**
     * Date Operators (operators used with Date operands)
     */
    public interface OpDate<RT> extends OpComparable<RT>{       
        OpDate<Boolean> AFTER = new OpDateImpl<Boolean>();
        OpDate<Boolean> BEFORE = new OpDateImpl<Boolean>();        
    }
    
    static class OpDateImpl<RT> implements OpDate<RT>{}
    

    static class OpImpl<RT> implements Op<RT> {}
    
    /**
     * Numeric Operators (operators used with numeric operands)
     */
    public interface OpNumber<RT> extends OpComparable<RT>{
        OpNumber<Number> ADD = new OpNumberImpl<Number>();   
        OpNumber<Number> DIV = new OpNumberImpl<Number>();        
        OpNumber<Number> MOD = new OpNumberImpl<Number>();
        OpNumber<Number> MULT = new OpNumberImpl<Number>();
        OpNumber<Number> SUB = new OpNumberImpl<Number>();
    }
    
    static class OpNumberImpl<A> implements OpNumber<A> {}       
    
    /**
     * String Operators (operators used with String operands)
     */
    public interface OpString<RT> extends OpComparable<RT>{       
        OpString<String> CONCAT = new OpStringImpl<String>();
        OpString<Boolean> LIKE = new OpStringImpl<Boolean>();
        OpString<String> LOWER = new OpStringImpl<String>();
        OpString<String> SUBSTR = new OpStringImpl<String>();
        OpString<String> UPPER = new OpStringImpl<String>();
    }
    
    static class OpStringImpl<RT> implements OpString<RT>{}

}
