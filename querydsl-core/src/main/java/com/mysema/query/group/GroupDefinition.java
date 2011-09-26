/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;

/**
 * Defines the way results of a given expression are grouped. GroupDefinition is also used
 * to access values of a given GroupDefinition within a Group. This resembles 
 * closely the way Expressions are used to access values of a {@link Tuple}. 
 * GroupDefinitions are stateless wrappers for Expressions that know how to 
 * collect row values into a group.
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
     * @return a new stateful GroupCollector to collect values belonging to this group.
     */
    GroupCollector<R> createGroupCollector();
    
}