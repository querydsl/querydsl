/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class NullExpr<T> extends PSimple<T>{

    public static final NullExpr<Object> DEFAULT = new NullExpr<Object>(Object.class);

    private static final long serialVersionUID = -5311968198973316411L;

    public NullExpr(Class<? extends T> type) {
        super(type, "null");
    }

}
