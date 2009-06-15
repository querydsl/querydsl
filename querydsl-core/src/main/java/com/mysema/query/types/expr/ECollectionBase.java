/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Collection;

import com.mysema.query.types.Grammar;

/**
 * @author tiwe
 *
 * @param <D>
 */
public abstract  class ECollectionBase<D> extends Expr<java.util.Collection<D>> implements ECollection<D> {

    public ECollectionBase(Class<? extends Collection<D>> type) {
        super(type);
    }
   
    private ENumber<Integer> size;    
    private EBoolean empty;
    private EBoolean notEmpty;

    
    @Override
    public final EBoolean contains(D child) {
        return Grammar.in(child, this);
    }

    @Override
    public final EBoolean contains(Expr<D> child) {
        return Grammar.in(child, this);
    }
    
    @Override
    public final EBoolean isEmpty() {
        if (empty == null){
            empty = Grammar.isEmpty(this); 
        }
        return empty;
    }

    @Override
    public final EBoolean isNotEmpty() {
        if (notEmpty == null){
            notEmpty = Grammar.isEmpty(this).not(); 
        }
        return notEmpty; 
    }

    @Override
    public final ENumber<Integer> size() {
        if (size == null) {
            size = Grammar.size(this);
        }
        return size;
    }

}
