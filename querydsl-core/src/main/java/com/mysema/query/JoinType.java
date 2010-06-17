/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import net.jcip.annotations.Immutable;

/**
 * JoinType defines the supported join types
 * 
 * @author tiwe
 */
@Immutable
public enum JoinType {
    /**
     * cross join
     */
    DEFAULT, 
    /**
     * inner join
     */
    INNERJOIN,
    /**
     * join
     */
    JOIN,
    /**
     * left join
     */
    LEFTJOIN,
    /**
     * right join
     */
    RIGHTJOIN,    
    /**
     * full join
     */
    FULLJOIN;
    
}