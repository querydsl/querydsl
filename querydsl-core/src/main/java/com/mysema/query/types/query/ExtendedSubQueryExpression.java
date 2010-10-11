package com.mysema.query.types.query;

import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Extensions to the SubQueryExpression interface
 * 
 * @author tiwe
 *
 * @param <T>
 */
public interface ExtendedSubQueryExpression<T> extends SubQueryExpression<T> {
    
    /**
     * Get an exists(this) expression for the subquery
     *
     * @return
     */
    BooleanExpression exists();

    /**
     * Get a not exists(this) expression for the subquery
     *
     * @return
     */
    BooleanExpression notExists();

}
