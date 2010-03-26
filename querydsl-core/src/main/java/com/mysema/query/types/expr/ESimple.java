/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nullable;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;

/**
 * ESimple is the base class for Expr implementations. It provides default implementations
 * for most of the abstract methods in {@link Expr}
 * 
 * @author tiwe
 *
 * @param <D>
 */
public abstract class ESimple<D> extends Expr<D> {
    

    private static final long serialVersionUID = -4405387187738167105L;
    
    @Nullable
    private volatile ENumber<Long> count;

    @Nullable
    private volatile ENumber<Long> countDistinct;

    public ESimple(Class<? extends D> type) {
        super(type);
    }

    @Override
    public ENumber<Long> count(){
        if (count == null){
            count = ONumber.create(Long.class, Ops.AggOps.COUNT_AGG, this);
        }
        return count;
    }
    
    @Override
    public ENumber<Long> countDistinct(){
        if (countDistinct == null){
          countDistinct = ONumber.create(Long.class, Ops.AggOps.COUNT_DISTINCT_AGG, this);
        }
        return countDistinct;
    }
    
    @Override
    public EBoolean eq(D right) {
        return eq(ExprConst.create(right));
    }
    
    @Override
    public EBoolean eq(Expr<? super D> right) {
        if (primitive) {
            return OBoolean.create(Ops.EQ_PRIMITIVE, this, right);
        } else {
            return OBoolean.create(Ops.EQ_OBJECT, this, right);
        }
    }
    
    @Override
    public EBoolean in(Collection<? extends D> right) {
        if (right.size() == 1){
            return eq(right.iterator().next());
        }else{
            return OBoolean.create(Ops.IN, this, ExprConst.create(right));    
        }        
    }
    
    @Override
    public EBoolean in(D... right) {
        if (right.length == 1){
            return eq(right[0]);
        }else{
            return OBoolean.create(Ops.IN, this, ExprConst.create(Arrays.asList(right)));    
        }        
    }
    
    @Override
    public EBoolean in(ECollection<?,? extends D> right) {
        return OBoolean.create(Ops.IN, this, (Expr<?>)right);
    }
    
    @Override
    public EBoolean ne(D right) {
        return ne(ExprConst.create(right));
    }

    @Override
    public EBoolean ne(Expr<? super D> right) {
        if (primitive) {
            return OBoolean.create(Ops.NE_PRIMITIVE, this, right);
        } else {
            return OBoolean.create(Ops.NE_OBJECT, this, right);
        }
    }
    
    /**
     * Get a case expression builder
     * 
     * @param other
     * @return
     */
    public CaseForEqBuilder<D> when(D other){
        return new CaseForEqBuilder<D>(this, ExprConst.create(other));
    }
    
    /**
     * Get a case expression builder
     * 
     * @param other
     * @return
     */
    public CaseForEqBuilder<D> when(Expr<? extends D> other){
        return new CaseForEqBuilder<D>(this, other);
    }

}
