/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.Expr;

public interface PMap<K, V> extends Path<java.util.Map<K, V>> {
	Expr<V> get(Expr<K> key);

	Expr<V> get(K key);

	Class<K> getKeyType();

	Class<V> getValueType();
}