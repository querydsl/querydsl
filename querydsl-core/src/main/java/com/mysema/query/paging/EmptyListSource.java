/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.paging;

import java.util.Collections;
import java.util.List;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class EmptyListSource<T> implements ListSource<T> {
    
    @Override
    public List<T> getResults(int fromIndex, int toIndex) {
        return Collections.emptyList();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public long size() {
        return 0l;
    }

}
