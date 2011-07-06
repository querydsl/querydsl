/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

/**
 * @author tiwe
 *
 */
public class NonUniqueResultException extends QueryException{

    private static final long serialVersionUID = -1757423191400510323L;

    public NonUniqueResultException() {
        super("Only one result is allowed for uniqueResult calls");
    }
    
    public NonUniqueResultException(String message) {
        super(message);
    }
    
}
