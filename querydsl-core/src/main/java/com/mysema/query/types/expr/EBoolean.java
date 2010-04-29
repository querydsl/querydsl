/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import javax.annotation.Nullable;

import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;


/**
 * EBoolean represents boolean expressions
 * 
 * @author tiwe
 * @see java.lang.Boolean
 * 
 */
public abstract class EBoolean extends EComparable<Boolean> {
    
    private static final long serialVersionUID = 3797956062512074164L;
    
    @Nullable
    public static EBoolean allOf(EBoolean... exprs){
        EBoolean rv = null;
        for (EBoolean b : exprs){
            rv = rv == null ? b : rv.and(b);
        }
        return rv;
    }

    @Nullable
    public static EBoolean anyOf(EBoolean... exprs){
        EBoolean rv = null;
        for (EBoolean b : exprs){
            rv = rv == null ? b : rv.or(b);
        }
        return rv;
    }
    
    @Nullable 
    private volatile EBoolean not;

    public EBoolean() {
        super(Boolean.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public EBoolean as(Path<Boolean> alias) {
        return OBoolean.create((Operator)Ops.ALIAS, this, alias.asExpr());
    }

    /**
     * Get an intersection of this and the given expression 
     * 
     * @param right right hand side of the union
     * @return this && right
     */
    public EBoolean and(@Nullable EBoolean right) {
        if (right != null){
            return OBoolean.create(Ops.AND, this, right);    
        }else{
            return this;
        }        
    }

    /**
     * Get a negation of this boolean expression
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
     * Get a union of this and the given expression
     * 
     * @param right right hand side of the union
     * @return this || right
     */
    public EBoolean or(@Nullable EBoolean right) {
        if (right != null){
            return OBoolean.create(Ops.OR, this, right);    
        }else{
            return this;
        }
        
    }
}