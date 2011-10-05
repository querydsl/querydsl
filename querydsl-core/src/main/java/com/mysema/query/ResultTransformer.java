/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;


/**
 * Executes query on a Projectable and transforms results into T. This can be used for example
 * to group projected columns or to filter out duplicate results.
 * 
 * @see com.mysema.query.group.GroupBy
 * @author sasa
 *
 * @param <T> Transformations target type
 */
public interface ResultTransformer<T> {

    T transform(Projectable projectable);
    
}
