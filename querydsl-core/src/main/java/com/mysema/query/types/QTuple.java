/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.Tuple;

/**
 * QTuple represents a projection of type Tuple
 * 
 * @author tiwe
 * 
 */
public class QTuple implements FactoryExpression<Tuple>{

    private final List<Expression<?>> args;
    
    public QTuple(Expression<?>... args) {
        this.args = Arrays.asList(args);
    }
    
    public QTuple(Expression<?>[]... args) {
        this.args = new ArrayList<Expression<?>>();
        for (Expression<?>[] exprs: args){
            this.args.addAll(Arrays.asList(exprs));
        }
    }

    private static final long serialVersionUID = -2640616030595420465L;

    @SuppressWarnings("unchecked")
    @Override
    public Tuple newInstance(final Object... args) {
        return new Tuple() {

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
            public Object[] toArray() {
                return args;
            }

        };
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof QTuple){
            QTuple c = (QTuple)obj;
            return args.equals(c.args)
                && getType().equals(c.getType());
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return getType().hashCode();
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    public Class<? extends Tuple> getType() {
        return Tuple.class;
    }

}
