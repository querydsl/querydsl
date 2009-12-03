/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.paging;

import java.util.List;

/**
 * ListSource provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface ListSource<T>{
    
    boolean isEmpty();
    
    long size();
    
    List<T> getResults(int fromIndex, int toIndex);

}
