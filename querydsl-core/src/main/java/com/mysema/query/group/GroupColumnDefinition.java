/*
 * Copyright (c) 2010 Mysema Ltd.
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
public interface GroupColumnDefinition<T, R> {
    
    Expression<T> getExpression();
    
    GroupColumn<R> createGroupColumn();
    
}