/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.util.Collections;

import com.mysema.query.types.Expression;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.custom.SimpleTemplate;

/**
 * NullExpr defines a general null expression
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class NullExpr<T> extends SimpleTemplate<T>{

    public static final NullExpr<Object> DEFAULT = new NullExpr<Object>(Object.class);

    private static final long serialVersionUID = -5311968198973316411L;

    public NullExpr(Class<? extends T> type) {
        super(type,TemplateFactory.DEFAULT.create("null"), Collections.<Expression<?>>emptyList());
    }

}
