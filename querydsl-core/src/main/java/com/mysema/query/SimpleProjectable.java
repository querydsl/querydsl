/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.util.List;

import javax.annotation.Nullable;

import com.mysema.commons.lang.CloseableIterator;

/**
 * SimpleProjectable defines a simpler projection interface than {@link Projectable}.
 *
 * @author tiwe
 * @see Projectable
 */
public interface SimpleProjectable<T> {
    
    /**
     * Get the projection as a typed closeable Iterator
     * 
     * @return
     */
    CloseableIterator<T> iterate();
    
    /**
     * Get the projection as a typed closeable Iterator with distinct elements
     * 
     * @return
     */
    CloseableIterator<T> iterateDistinct();

    /**
     * Get the projection as a typed List
     *
     * @return
     */
    List<T> list();

    /**
     * Get the projection as a typed List with distinct elements
     *
     * @return
     */
    List<T> listDistinct();

    /**
     * Get the projection as a unique result or null if no result is found
     * 
     * @throws NonUniqueResultException if there is more than one matching result
     * @return
     */
    @Nullable
    T uniqueResult();

    /**
     * Get the projection in {@link SearchResults} form
     *
     * @return
     */
    SearchResults<T> listResults();

    /**
     * Get the projection in {@link SearchResults} form with distinct element
     *
     * @return
     */
    SearchResults<T> listDistinctResults();

    /**
     * Get the count of matched elements
     *
     * @return
     */
    long count();

    /**
     * Get the count of distinct matched elements
     *
     * @return
     */
    long countDistinct();

}
