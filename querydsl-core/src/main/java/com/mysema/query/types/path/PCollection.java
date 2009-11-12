/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.ECollection;

/**
 * PCollection represents collection typed paths
 * 
 * @author tiwe
 * 
 * @param <E>
 * @see java.util.Collection
 */
public interface PCollection<E> extends Path<java.util.Collection<E>>, ECollection<E> {
   
}