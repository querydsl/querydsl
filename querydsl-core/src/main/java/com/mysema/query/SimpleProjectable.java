/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author tiwe
 *
 */
public interface SimpleProjectable<T> {
    
    /**
     * @return
     */
    List<T> list();

    /**
     * @return
     */
    List<T> listDistinct();

    /**
     * @return
     */
    @Nullable
    T uniqueResult();

    /**
     * @return
     */
    SearchResults<T> listResults();

    /**
     * @return
     */
    SearchResults<T> listDistinctResults();

    /**
     * @return
     */
    long count();

    /**
     * @return
     */
    long countDistinct();

}
