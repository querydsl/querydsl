/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.Map;

/**
 * MapExpression represents java.util.Map typed expressions
 *
 * @author tiwe
 *
 * @param <K> key type
 * @param <V> value type
 * @see java.util.Map
 */
public interface MapExpression<K, V> extends ParametrizedExpression<Map<K,V>>{

}
