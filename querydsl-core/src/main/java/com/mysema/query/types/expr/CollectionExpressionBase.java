/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.annotation.Nullable;

import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.PathMetadata;

/**
 * CollectionExpressionBase is an abstract base class for CollectionExpression implementations
 *
 * @author tiwe
 *
 * @param <D>
 */
public abstract class CollectionExpressionBase<C extends Collection<E>, E> extends SimpleExpression<C> implements CollectionExpression<C,E> {

    private static final long serialVersionUID = 691230660037162054L;

    @Nullable
    private volatile BooleanExpression empty;

    @Nullable
    private volatile NumberExpression<Integer> size;
    
    @Nullable
    private transient volatile Constructor<?> constructor;
    
    public CollectionExpressionBase(Class<? extends C> type) {
        super(type);
    }

    public abstract SimpleExpression<E> any();
    
    public final BooleanExpression contains(E child) {
        return contains(new ConstantImpl<E>(child));
    }

    public final BooleanExpression contains(Expression<E> child) {
        return BooleanOperation.create(Ops.IN, child, this);
    }

    public abstract Class<E> getElementType();
    
    public final BooleanExpression isEmpty() {
        if (empty == null){
            empty = BooleanOperation.create(Ops.COL_IS_EMPTY, this);
        }
        return empty;
    }

    public final BooleanExpression isNotEmpty() {
        return isEmpty().not();
    }

    public final NumberExpression<Integer> size() {
        if (size == null) {
            size = NumberOperation.create(Integer.class, Ops.COL_SIZE, this);
        }
        return size;
    }

    @SuppressWarnings("unchecked")
    protected <Q extends SimpleExpression<E>> Q newInstance(Class<Q> queryType, boolean typed, PathMetadata<?> pm) throws NoSuchMethodException,
        InstantiationException, IllegalAccessException,
        InvocationTargetException {
        if (constructor == null) {
            if (typed){
                constructor = queryType.getConstructor(Class.class, PathMetadata.class);
            }else{
                constructor = queryType.getConstructor(PathMetadata.class);
            }
        }
        if (typed){
            return (Q)constructor.newInstance(getElementType(), pm);
        }else{
            return (Q)constructor.newInstance(pm);
        }
    }
    
}
