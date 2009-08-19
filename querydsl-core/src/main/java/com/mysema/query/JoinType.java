/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import net.jcip.annotations.Immutable;

/**
 * JoinType defines the supported join types
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public enum JoinType {
    /**
     * 
     */
    DEFAULT("JOIN"), 
    /**
     * 
     */
    INNERJOIN("INNER JOIN"),
    /**
     * 
     */
    JOIN("JOIN"),
    /**
     * 
     */
    LEFTJOIN("LEFTJOIN"),
    /**
     * 
     */
    FULLJOIN("FULL JOIN");
    
    private final String str;
    
    private JoinType(String str){
        this.str = str;
    }
    
    @Override 
    public String toString(){
        return str;
    }
}