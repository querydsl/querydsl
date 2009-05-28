/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Grammar;

/**
 * EBoolean represents boolean expressions
 * 
 * @author tiwe
 * @see java.lang.Boolean
 * 
 */
public abstract class EBoolean extends EComparable<Boolean> {
    private EBoolean not;

    public EBoolean() {
        super(Boolean.class);
    }

    /**
     * Create an intersection of this and the given expression 
     * 
     * @param right right hand side of the union
     * @return
     */
    public final EBoolean and(EBoolean right) {
        return Grammar.and(this, right);
    }

    /**
     * Create a negation of this boolean expression
     * 
     * @return
     */
    public final EBoolean not() {
        if (not == null){
            not = Grammar.not(this);
        }            
        return not;
    }

    /**
     * Create a union of this and the given expression
     * 
     * @param right right hand side of the union
     * @return
     */
    public final EBoolean or(EBoolean right) {
        return Grammar.or(this, right);
    }
}