/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path.*;

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
    
    <K,V> PEntityMap<K,V> createEntityMap(Map<K,V> arg);
    
    <D> PEntityCollection<D> createEntityCollection(Collection<D> arg);

    <D extends Comparable<?>> PComparableArray<D> createComparableArray(D[] args);

    PString createString(String arg);

    PStringArray createStringArray(String[] args);

}