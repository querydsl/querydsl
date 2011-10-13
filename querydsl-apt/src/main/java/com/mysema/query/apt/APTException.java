/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt;

/**
 * @author tiwe
 *
 */
public class APTException extends RuntimeException {

    private static final long serialVersionUID = -8456970330509020462L;

    public APTException(String msg) {
        super(msg);
    }

    public APTException(String msg, Throwable t) {
        super(msg, t);
    }

    public APTException(Throwable t) {
        super(t);
    }

}
