/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PBooleanArray;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PComparableArray;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PEntityMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PStringArray;

/**
 * PathFactory creates path expressions for domain type instances
 * 
 * @author tiwe
 * @version $Id$
 */
interface PathFactory {

    /**
     * 
     * @param <D>
     * @param arg
     * @return
     */
    <D> Expr<D> createAny(D arg);

    /**
     * 
     * @param arg
     * @return
     */
    PBoolean createBoolean(Boolean arg);

    /**
     * 
     * @param args
     * @return
     */
    PBooleanArray createBooleanArray(Boolean[] args);

    /**
     * 
     * @param <D>
     * @param arg
     * @return
     */
    <D extends Number & Comparable<?>> PNumber<D> createNumber(D arg);

    /**
     * @param <D>
     * @param arg
     * @return
     */
    <D extends Comparable<?>> PComparable<D> createComparable(D arg);

    /**
     * @param <D>
     * @param arg
     * @return
     */
    <D> PEntity<D> createEntity(D arg);

    /**
     * @param <D>
     * @param arg
     * @return
     */
    <D> PEntityList<D> createEntityList(List<D> arg);

    /**
     * @param <K>
     * @param <V>
     * @param arg
     * @return
     */
    <K, V> PEntityMap<K, V> createEntityMap(Map<K, V> arg);

    /**
     * @param <D>
     * @param arg
     * @return
     */
    <D> PEntityCollection<D> createEntityCollection(Collection<D> arg);

    /**
     * @param <D>
     * @param args
     * @return
     */
    <D extends Comparable<?>> PComparableArray<D> createComparableArray(D[] args);

    /**
     * @param arg
     * @return
     */
    PString createString(String arg);

    /**
     * @param args
     * @return
     */
    PStringArray createStringArray(String[] args);

}