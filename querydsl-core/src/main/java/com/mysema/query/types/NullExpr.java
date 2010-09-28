/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.Collections;

/**
 * NullExpr defines a general null expression
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class NullExpr<T> extends TemplateExpressionImpl<T>{
    
    public static final NullExpr<Object> DEFAULT = new NullExpr<Object>(Object.class);

    private static final long serialVersionUID = -5311968198973316411L;

    private static final Template NULL_TEMPLATE = TemplateFactory.DEFAULT.create("null");
    
    public NullExpr(Class<? extends T> type) {
        super(type, Collections.<Expression<?>>emptyList(), NULL_TEMPLATE);
    }

}
