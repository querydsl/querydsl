package com.mysema.codegen;

public class CodegenException extends RuntimeException{

    private static final long serialVersionUID = -8704782349669898467L;

    public CodegenException(String msg){
        super(msg);
    }
    
    public CodegenException(String msg, Throwable t){
        super(msg, t);
    }
    
    public CodegenException(Throwable t){
        super(t);
    }
}
