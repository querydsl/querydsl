package com.mysema.query.types;

/**
 * Defines a custom projection for an Expression type
 * 
 * @author tiwe
 *
 */
// TODO : maybe find a better name
public interface ProjectionRole<T> extends Expression<T> {

    /**
     * Return the custom projection
     * 
     * @return
     */
    Expression<T> getProjection();
    
}
