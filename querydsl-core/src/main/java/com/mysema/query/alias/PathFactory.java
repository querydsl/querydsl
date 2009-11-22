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
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PList;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;

/**
 * PathFactory creates path expressions for domain type instances
 * 
 * @author tiwe
 * @version $Id$
 */
interface PathFactory {

    /**
     * @param <D>
     * @param arg
     * @return
     */
    <D> Expr<D> createAny(D arg);

    /**
     * @param arg
     * @return
     */
    PBoolean createBoolean(Boolean arg);


    /**
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
    @SuppressWarnings("unchecked")
    <D extends Comparable> PDate<D> createDate(D arg);

    /**
     * @param <D>
     * @param arg
     * @return
     */
    @SuppressWarnings("unchecked")
    <D extends Comparable> PTime<D> createTime(D arg);

    /**
     * @param <D>
     * @param arg
     * @return
     */
    @SuppressWarnings("unchecked")
    <D extends Comparable> PDateTime<D> createDateTime(D arg);

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
    <D> PList<D,?> createList(List<D> arg);

    /**
     * @param <K>
     * @param <V>
     * @param arg
     * @return
     */
    <K, V> PMap<K, V,?> createMap(Map<K, V> arg);

    /**
     * @param <D>
     * @param arg
     * @return
     */
    <D> PCollection<D> createEntityCollection(Collection<D> arg);

    /**
     * @param arg
     * @return
     */
    PString createString(String arg);


}