/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;

/**
 * Tuple defines an interface for generic query result projection
 *
 * @author tiwe
 *
 */
public interface Tuple {

    /**
     * Get a Tuple element by index
     *
     * @param <T>
     * @param index
     * @param type
     * @return
     */
    @Nullable
    <T> T get(int index, Class<T> type);

    /**
     * Get a tuple element by expression
     *
     * @param <T>
     * @param expr
     * @return
     */
    @Nullable
    <T> T get(Expression<T> expr);

    /**
     * Get the content as an Object array
     *
     * @return
     */
    Object[] toArray();
    
   /**
    * All Tuples should override equals and hashCode. For compatibility
    * across different Tuple implementations, equality check should use
    * {@link java.util.Arrays#equals(Object[], Object[])} with {@link #toArray()} as parameters.
    *
    * @see Object#equals(Object)
    */
   boolean equals(Object o);
        
   /**
    * All Tuples should override equals and hashCode. For compatibility
    * across different Tuple implementations, hashCode should use
    * {@link java.util.Arrays#hashCode(Object[])} with {@link #toArray()} as parameter.
    *
    * @see Object#hashCode()
    */
   int hashCode();
    

}
