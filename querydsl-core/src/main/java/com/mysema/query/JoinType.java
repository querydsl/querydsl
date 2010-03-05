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
     * full join
     */
    FULLJOIN;
    
}