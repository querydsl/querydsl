package com.mysema.query.types;

public class ExprException extends RuntimeException{

    private static final long serialVersionUID = 6031724386976562965L;

    public ExprException(String msg){
        super(msg);
    }
    
    public ExprException(String msg, Throwable t){
        super(msg, t);
    }
    
    public ExprException(Throwable t){
        super(t);
    }

}
