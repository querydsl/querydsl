/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.List;

import javax.annotation.Nullable;

/**
 * SimpleProjectable defines a simpler projection interface than {@link Projectable}.
 * 
 * @author tiwe
 * @see Projectable
 */
public interface SimpleProjectable<T> {
    
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
     * Get the projection as a unique result
     * 
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
