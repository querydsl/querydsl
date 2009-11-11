/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;


/**
 * EBoolean represents boolean expressions
 * 
 * @author tiwe
 * @see java.lang.Boolean
 * 
 */
@SuppressWarnings("serial")
public abstract class EBoolean extends EComparable<Boolean> {
    
    public static final EBoolean FALSE = new EBooleanConst(Boolean.FALSE);
    
    public static final EBoolean TRUE = new EBooleanConst(Boolean.TRUE);
    
    public static final EBoolean __create(Boolean b){
        return b.booleanValue() ? TRUE : FALSE;
    }

    private volatile EBoolean not;

    public EBoolean() {
        super(Boolean.class);
    }

    /**
     * Create an intersection of this and the given expression 
     * 
     * @param right right hand side of the union
     * @return this && right
     */
    public EBoolean and(EBoolean right) {
        return OBoolean.create(Ops.AND, this, right);
    }

    /**
     * Create a negation of this boolean expression
     * 
     * @return !this
     */
    public EBoolean not() {
        if (not == null){
            not = OBoolean.create(Ops.NOT, this);
        }            
        return not;
    }
    
    /**
     * Create a union of this and the given expression
     * 
     * @param right right hand side of the union
     * @return this || right
     */
    public EBoolean or(EBoolean right) {
        return OBoolean.create(Ops.OR, this, right);
    }
}