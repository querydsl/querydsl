/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.EMap;

/**
 * PMap represents Map typed paths
 * 
 * @author tiwe
 * 
 * @param <K> key type
 * @param <V> value type
 * @see java.util.Map
 */
public interface PMap<K, V> extends Path<java.util.Map<K, V>>, EMap<K,V> {
    
}