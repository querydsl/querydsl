package com.mysema.query.group;

import com.mysema.query.types.Expression;

/**
 * Defines the way results of a given expression are grouped. GroupExpressions are also used
 * to access values of a given GroupExpression within a Group.  
 * GroupExpressions are stateless wrappers for Expressions that know how to 
 * collect row values into a group.
 * 
 * @author sasa
 * @author tiwe
 *
 * @param <T>
 */
public interface GroupExpression<T,R> extends Expression<R> {

    /**
     * @return Expression wrapped by this group definition
     */
    Expression<T> getExpression();
    
    /**
     * @return a new GroupCollector to collect values belonging to this group.
     */
    GroupCollector<T, R> createGroupCollector();
    
    
}
