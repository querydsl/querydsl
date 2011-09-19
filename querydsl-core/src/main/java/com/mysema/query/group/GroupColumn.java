/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

/**
 *
 * @author sasa
 * @param <R>
 */
public interface GroupColumn<R> {
    
    void add(Object o);

    R get();
    
}