/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.EList;

/**
 * PList represents List typed paths
 * 
 * @author tiwe
 * 
 * @param <E> component type
 * @see java.util.List
 */
public interface PList<E> extends PCollection<E>, EList<E> {
    
}