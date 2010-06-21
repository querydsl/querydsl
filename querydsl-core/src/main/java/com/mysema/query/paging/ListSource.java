/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.paging;

import java.util.List;

/**
 * ListSource provides a view on paged search results
 *
 * @author tiwe
 * @version $Id$
 */
public interface ListSource<T>{

    /**
     * true, if not results could be found, otherwise false
     *
     * @return
     */
    boolean isEmpty();

    /**
     * total amount of results
     *
     * @return
     */
    long size();

    /**
     * Get the results from the given start index to the given end index
     *
     * @param fromIndex start index
     * @param toIndex end index
     * @return
     */
    List<T> getResults(int fromIndex, int toIndex);

    /**
     * @param index
     * @return
     */
    T getResult(int index);
}
