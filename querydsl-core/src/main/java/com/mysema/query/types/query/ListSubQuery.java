/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.query;

import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.SubQueryExpressionImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.CollectionExpressionBase;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;

/**
 * List result subquery
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public final class ListSubQuery<T> extends CollectionExpressionBase<List<T>,T> implements ExtendedSubQueryExpression<List<T>>{

    private static final long serialVersionUID = 3399354334765602960L;

    private final Class<T> elementType;

    private final SubQueryExpressionImpl<List<T>> subQueryMixin;

    @Nullable
    private volatile BooleanExpression exists;

    @SuppressWarnings("unchecked")
    public ListSubQuery(Class<T> elementType, QueryMetadata md) {
        super((Class)List.class);
        this.elementType = elementType;
        this.subQueryMixin = new SubQueryExpressionImpl<List<T>>((Class)List.class, md);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object o) {
       return subQueryMixin.equals(o);
    }

    @Override
    public BooleanExpression exists() {
        if (exists == null){
            exists = BooleanOperation.create(Ops.EXISTS, this);
        }
        return exists;
    }

    @Override
    public Class<T> getElementType() {
        return elementType;
    }

    @Override
    public QueryMetadata getMetadata() {
        return subQueryMixin.getMetadata();
    }

    @Override
    public int hashCode(){
        return subQueryMixin.hashCode();
    }

    @Override
    public BooleanExpression notExists() {
        return exists().not();
    }

    public SimpleExpression<?> as(Expression<?> alias) {
        return SimpleOperation.create(alias.getType(),Ops.ALIAS, this, alias);
    }

    @Override
    public Class<?> getParameter(int index) {
        if (index == 0){
            return elementType;
        }else{
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

}
