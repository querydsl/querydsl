/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import com.mysema.query.types.Expression;

/**
 * Defines the way results of a given expression are grouped.  
 * 
 * @author sasa
 *
 * @param <T> Expression type
 * @param <R> Target type (e.g. T, Set&lt;T> or List&lt;T>) 
 */
public interface GroupDefinition<T, R> {
    
    /**
     * @return Expression wrapped by this group definition
     */
    Expression<T> getExpression();
    
    /**
     * @return a new GroupCollector to collect values belonging to this group.
     */
    GroupCollector<R> createGroupCollector();
    
}