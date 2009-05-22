/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mysema.query.types.expr.Expr;

/**
 * PathFactory creates path expressions for domain type instances
 * 
 * @author tiwe
 * @version $Id$
 */
public interface PathFactory {

	<D> Expr<D> createAny(D arg);

	PBoolean createBoolean(Boolean arg);

	PBooleanArray createBooleanArray(Boolean[] args);

	<D extends Number & Comparable<?>> PNumber<D> createNumber(D arg);

	<D extends Comparable<?>> PComparable<D> createComparable(D arg);

	<D> PEntity<D> createEntity(D arg);

	<D> PEntityList<D> createEntityList(List<D> arg);

	<K, V> PEntityMap<K, V> createEntityMap(Map<K, V> arg);

	<D> PEntityCollection<D> createEntityCollection(Collection<D> arg);

	<D extends Comparable<?>> PComparableArray<D> createComparableArray(D[] args);

	PString createString(String arg);

	PStringArray createStringArray(String[] args);

}