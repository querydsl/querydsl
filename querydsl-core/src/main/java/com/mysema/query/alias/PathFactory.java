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
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PEntityMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PStringArray;
import com.mysema.query.types.path.PTime;

/**
 * PathFactory creates path expressions for domain type instances
 * 
 * @author tiwe
 * @version $Id$
 */
interface PathFactory {

    <D> Expr<D> createAny(D arg);

    PBoolean createBoolean(Boolean arg);

    PBooleanArray createBooleanArray(Boolean[] args);

    <D extends Number & Comparable<?>> PNumber<D> createNumber(D arg);

    <D extends Comparable<?>> PComparable<D> createComparable(D arg);

    @SuppressWarnings("unchecked")
    <D extends Comparable> PDate<D> createDate(D arg);

    @SuppressWarnings("unchecked")
    <D extends Comparable> PTime<D> createTime(D arg);

    @SuppressWarnings("unchecked")
    <D extends Comparable> PDateTime<D> createDateTime(D arg);

    <D> PEntity<D> createEntity(D arg);

    <D> PEntityList<D> createEntityList(List<D> arg);

    <K, V> PEntityMap<K, V> createEntityMap(Map<K, V> arg);

    <D> PEntityCollection<D> createEntityCollection(Collection<D> arg);

    <D extends Comparable<?>> PComparableArray<D> createComparableArray(D[] args);

    PString createString(String arg);

    PStringArray createStringArray(String[] args);

}