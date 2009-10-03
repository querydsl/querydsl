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
public class TypeArgumentsException extends RuntimeException{
    
    private static final long serialVersionUID = -3344464172475025628L;

    public TypeArgumentsException(String name) {
        super("Insufficient type arguments for " + name);

    }

}
