/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

/**
 * @author tiwe
 *
 */
public class ExpressionException extends RuntimeException{

    private static final long serialVersionUID = 6031724386976562965L;

    public ExpressionException(String msg) {
        super(msg);
    }

    public ExpressionException(String msg, Throwable t) {
        super(msg, t);
    }

    public ExpressionException(Throwable t) {
        super(t);
    }

}
