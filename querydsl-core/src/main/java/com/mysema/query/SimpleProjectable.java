/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.List;

/**
 * @author tiwe
 *
 */
public interface SimpleProjectable<T> {
    
    List<T> list();

    List<T> listDistinct();

    T uniqueResult();

    SearchResults<T> listResults();

    SearchResults<T> listDistinctResults();

    long count();

    long countDistinct();

}
