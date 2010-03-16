/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

/**
 * @author tiwe
 *
 */
public class QueryException extends RuntimeException{

    private static final long serialVersionUID = 2345665389612058703L;
    
    public QueryException(String msg){
        super(msg);
    }
    
    public QueryException(String msg, Throwable t){
        super(msg, t);
    }
    
    public QueryException(Throwable t){
        super(t);
    }

}
