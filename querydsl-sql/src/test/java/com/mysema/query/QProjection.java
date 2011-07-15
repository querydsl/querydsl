/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionBase;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Visitor;

public class QProjection extends ExpressionBase<Projection> implements FactoryExpression<Projection>{

    private static final long serialVersionUID = -7330905848558102164L;

    private final List<Expression<?>> args;
    
    public QProjection(Expression<?>... args) {
        super(Projection.class);
        this.args = Arrays.asList(args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Projection newInstance(final Object... args){
        return new Projection(){

            @Override
            public <T> T get(int index, Class<T> type) {
                return (T) args[index];
            }

            @Override
            public <T> T get(Expression<T> expr) {
                int index = getArgs().indexOf(expr);
                return index != -1 ? (T) args[index] : null;
            }

            @Override
            public <T> Expression<T> getExpr(Expression<T> expr){
                T val = get(expr);
                return val != null ? SimpleConstant.create(val) : null;
            }

            @Override
            public <T> Expression<T> getExpr(int index, Class<T> type){
                T val = (T)args[index];
                return val != null ? SimpleConstant.create(val) : null;
            }

            @Override
            public Object[] toArray() {
                return args;
            }

        };
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

}
