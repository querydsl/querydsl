/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.Collections;

/**
 * NullExpression defines a general null expression
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class NullExpression<T> extends TemplateExpressionImpl<T>{
    
    private static final Template NULL_TEMPLATE = TemplateFactory.DEFAULT.create("null");
    
    private static final long serialVersionUID = -5311968198973316411L;

    public static final NullExpression<Object> DEFAULT = new NullExpression<Object>(Object.class);
    
    public NullExpression(Class<? extends T> type) {
        super(type, NULL_TEMPLATE, Collections.<Expression<?>>emptyList());
    }

}
