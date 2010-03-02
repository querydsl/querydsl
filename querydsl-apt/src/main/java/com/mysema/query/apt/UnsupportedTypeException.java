/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import javax.lang.model.type.TypeMirror;

/**
 * @author tiwe
 *
 */
public class UnsupportedTypeException extends APTException {

    private static final long serialVersionUID = 1082936662325717262L;

    public UnsupportedTypeException(TypeMirror mirror){
        super("Unsupported type " + mirror);
    }
}
