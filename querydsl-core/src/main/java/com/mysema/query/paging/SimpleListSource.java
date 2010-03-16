/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.paging;

import java.util.Arrays;
import java.util.List;

import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class SimpleListSource<T> implements ListSource<T> {
    
    private final List<T> list;
    
    public SimpleListSource(List<T> list){
        this.list = Assert.notNull(list);
    }
    
    public SimpleListSource(T... args){
        this(Arrays.asList(args));
    }

    @Override
    public List<T> getResults(int fromIndex, int toIndex) {
        return list.subList(fromIndex, Math.min(list.size(), toIndex));
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public long size() {
        return list.size();
    }

}
