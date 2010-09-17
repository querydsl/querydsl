package com.mysema.query.types;

/**
 * @author tiwe
 *
 * @param <T>
 */
public interface ParametrizedExpression<T> extends Expression<T> {
    
    /**
     * @param index
     * @return
     */
    Class<?> getParameter(int index);

}
