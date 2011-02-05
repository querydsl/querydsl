/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.List;

import javax.annotation.Nullable;

/**
 * PredicateOperation provides a Boolean typed Operation implementation 
 * 
 * @author tiwe
 *
 */
public class PredicateOperation extends OperationImpl<Boolean> implements Predicate{
    
    private static final long serialVersionUID = -5371430939203772072L;

    @Nullable
    private Predicate not;
    
    public PredicateOperation(Operator<Boolean> operator, Expression<?>... args){
        super(Boolean.class, operator, args);
    }

    public PredicateOperation(Operator<Boolean> operator, List<Expression<?>> args){
        super(Boolean.class, operator, args);
    }
    
    @Override
    public Predicate not() {
        if (not == null){
            not = new PredicateOperation(Ops.NOT, this);
        }
        return not;
    }

}
