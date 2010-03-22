/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import javax.annotation.Nullable;

import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OString;
import com.mysema.query.types.operation.Ops;


/**
 * EComparableBase represents comparable expressions
 * 
 * @author tiwe
 * 
 * @param <D> Java type
 * @see java.lang.Comparable
 */
@SuppressWarnings({"unchecked"})
public abstract class EComparableBase<D extends Comparable> extends Expr<D> {

    private static final long serialVersionUID = 1460921109546656911L;

    @Nullable
    private volatile OrderSpecifier<D> asc, desc;
    
    @Nullable
    private volatile EString stringCast;

    public EComparableBase(Class<? extends D> type) {
        super(type);
    }
      

    /**
     * Get an OrderSpecifier for ascending order of this expression
     * 
     * @return
     */
    public final OrderSpecifier<D> asc() {
        if (asc == null){
            asc = new OrderSpecifier<D>(Order.ASC, this);
        }            
        return asc;
    }
       
    /**
     * Create a cast expression to the given numeric type
     * 
     * @param <A>
     * @param type
     * @return
     */
    public <A extends Number & Comparable<? super A>> ENumber<A> castToNum(Class<A> type) {
        return ONumber.create(type, Ops.NUMCAST, this, ExprConst.create(type));
    }

    /**
     * Get an OrderSpecifier for descending order of this expression
     * 
     * @return
     */
    public final OrderSpecifier<D> desc() {
        if (desc == null){
            desc = new OrderSpecifier<D>(Order.DESC, this);
        }            
        return desc;
    }

    /**
     * Get a cast to String expression 
     * 
     * @see     java.lang.Object#toString()
     * @return
     */
    public EString stringValue() {
        if (stringCast == null){
            stringCast = OString.create(Ops.STRING_CAST, this);
        }            
        return stringCast;
    }

}