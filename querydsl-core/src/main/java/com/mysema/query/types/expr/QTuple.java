/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.Tuple;
import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;

/**
 * QTuple represents a projection of type Tuple
 * 
 * @author tiwe
 * 
 */
public class QTuple extends EConstructor<Tuple> {

    public QTuple(Expr<?>... args) {
        super(Tuple.class, new Class[0], args);
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
            public <T> T get(Expr<T> expr) {
                int index = getArgs().indexOf(expr);
                return index != -1 ? (T) args[index] : null;
            }

            @Override
            public Object[] toArray() {
                return args;
            }

        };
    }

}
