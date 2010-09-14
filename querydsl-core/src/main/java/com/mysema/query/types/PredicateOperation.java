/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

/**
 * @author tiwe
 *
 */
public class PredicateOperation extends OperationMixin<Boolean >implements Predicate{
    
    private static final long serialVersionUID = -5371430939203772072L;

    public PredicateOperation(Operator<Boolean> operator, Expression<?>... args){
        super(Boolean.class, operator, args);
    }

    @Override
    public Predicate not() {
        return new PredicateOperation(Ops.NOT, this);
    }

}
