/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

/**
 * A stateful collector of column values for a group.
 * 
 * @author sasa
 * @param <R> Target type (e.g. List, Set)
 */
public interface GroupCollector<T,R> {
    
    /**
     * Add given value to this group
     * 
     * @param o
     */
    void add(T o);

    /**
     * @return Value of this group.
     */
    R get();
    
}