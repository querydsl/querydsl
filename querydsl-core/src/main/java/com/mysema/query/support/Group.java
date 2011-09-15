/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;

/**
 * An interface for grouped results. Group identifier is always the first element.
 * 
 * @author sasa
 */
public interface Group {
    
    /**
     * Get an element of first row of this group by index
     *
     * @param <T>
     * @param index
     * @param type
     * @return
     */
    @Nullable
    <T> T get(int index, Class<T> type);

    /**
     * Get an element of first row of this group by expr
     *
     * @param <T>
     * @param expr
     * @return
     */
    @Nullable
    <T> T get(Expression<T> expr);

    /**
     * Get a list of elements (all rows) of this group by expression
     * 
     * @param <T>
     * @param index
     * @param type
     * @return
     */
    @Nullable
    <T> List<T> getList(int index, Class<T> type);
    

    /**
     * Get a list of elements (all rows) of this group by expression
     *
     * @param <T>
     * @param expr
     * @return
     */
    @Nullable
    <T> List<T> getList(Expression<T> expr);
    
    /**
     * Returns the i'th row of this group.
     * 
     * @param i 0 &lt;= i &lt; size()
     * @throws IndexOutOfBoundsException if i &lt; 0 or size() <= i
     * @return i'th row as a tuple of this group
     */
    Tuple getRow(int i);
    
    /**
     *
     * @return
     */
    int size();
    
}
