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
public interface GroupCollector<R> {
    
    /**
     * Add given value to this group
     * 
     * @param o
     */
    void add(Object o);

    /**
     * @return Value of this group.
     */
    R get();
    
}