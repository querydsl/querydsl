package com.mysema.query.apt;

import javax.lang.model.type.TypeMirror;

/**
 * @author tiwe
 *
 */
public class UnsupportedTypeException extends RuntimeException {

    private static final long serialVersionUID = 1082936662325717262L;

    public UnsupportedTypeException(TypeMirror mirror){
        super("Unsupported type " + mirror);
    }
}
