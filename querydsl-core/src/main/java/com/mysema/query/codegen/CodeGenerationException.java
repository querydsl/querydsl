/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public class CodeGenerationException extends RuntimeException{

    private static final long serialVersionUID = -7692082275150033814L;

    CodeGenerationException(String msg){
        super(msg);
    }
    
    CodeGenerationException(String msg, Throwable t){
        super(msg, t);
    }
    
    CodeGenerationException(Throwable t){
        super(t);
    }
}
