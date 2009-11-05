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
import com.mysema.query.types.path.PComponentMap;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PStringArray;
import com.mysema.query.types.path.PTime;

/**
 * AliasAwareExprFactory extends the SimpleExprFactory to return thread bound
 * alias expressions, when present
 * 
 * @author tiwe
 * @version $Id$
 */
class AliasAwarePathFactory implements PathFactory {

    private SimplePathFactory factory = new SimplePathFactory();
    
    private final AliasFactory aliasFactory;

    public AliasAwarePathFactory(AliasFactory aliasFactory) {
        this.aliasFactory = aliasFactory;
    }

    @SuppressWarnings("unchecked")
    public <D> Expr<D> createAny(D arg) {
        Expr<D> current = (Expr<D>) aliasFactory.getCurrentAndReset();
        if (current != null) {
            return current;
        } else if (arg instanceof ManagedObject) {
            return (Expr<D>) ((ManagedObject) arg).__mappedPath();
        } else {
            throw new IllegalArgumentException("No path mapped to " + arg);
        }
    }

    public PBoolean createBoolean(Boolean arg) {
        PBoolean rv = aliasFactory.<PBoolean> getCurrentAndReset();
        return rv != null ? rv : factory.createBoolean(arg);
    }

    public PBooleanArray createBooleanArray(Boolean[] args) {
        PBooleanArray rv = aliasFactory.<PBooleanArray> getCurrentAndReset();
        return rv != null ? rv : factory.createBooleanArray(args);
    }

    public <D extends Comparable<?>> PComparable<D> createComparable(D arg) {
        PComparable<D> rv = aliasFactory.<PComparable<D>> getCurrentAndReset();
        return rv != null ? rv : factory.createComparable(arg);
    }

    public <D extends Comparable<?>> PComparableArray<D> createComparableArray(D[] args) {
        PComparableArray<D> rv = aliasFactory.<PComparableArray<D>> getCurrentAndReset();
        return rv != null ? rv : factory.createComparableArray(args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PDate<D> createDate(D arg) {
        PDate<D> rv = aliasFactory.<PDate<D>>getCurrentAndReset();
        return rv != null ? rv : factory.createDate(arg);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PDateTime<D> createDateTime(D arg) {
        PDateTime<D> rv = aliasFactory.<PDateTime<D>>getCurrentAndReset();
        return rv != null ? rv : factory.createDateTime(arg);
    }

    @SuppressWarnings("unchecked")
    public <D> PEntity<D> createEntity(D arg) {
        PEntity<D> rv = aliasFactory.<PEntity<D>> getCurrentAndReset();
        if (rv != null) {
            return rv;
        } else if (arg instanceof ManagedObject) {
            return (PEntity<D>) ((ManagedObject) arg).__mappedPath();
        } else {
            return factory.createEntity(arg);
        }
    }

    public <D> PEntityCollection<D> createEntityCollection(Collection<D> arg) {
        PEntityCollection<D> rv = aliasFactory.<PEntityCollection<D>> getCurrentAndReset();
        return rv != null ? rv : factory.createEntityCollection(arg);
    }

    public <D> PEntityList<D,?> createList(List<D> arg) {
        PEntityList<D,?> rv = aliasFactory.<PEntityList<D,?>> getCurrentAndReset();
        return rv != null ? rv : factory.createList(arg);
    }

    public <K, V> PComponentMap<K, V> createMap(Map<K, V> arg) {
        PComponentMap<K, V> rv = aliasFactory.<PComponentMap<K, V>> getCurrentAndReset();
        return rv != null ? rv : factory.createMap(arg);
    }

    public <D extends Number & Comparable<?>> PNumber<D> createNumber(D arg) {
        PNumber<D> rv = aliasFactory.<PNumber<D>> getCurrentAndReset();
        return rv != null ? rv : factory.createNumber(arg);
    }

    public PString createString(String arg) {
        PString rv = aliasFactory.<PString> getCurrentAndReset();
        return rv != null ? rv : factory.createString(arg);
    }

    public PStringArray createStringArray(String[] args) {
        PStringArray rv = aliasFactory.<PStringArray> getCurrentAndReset();
        return rv != null ? rv : factory.createStringArray(args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PTime<D> createTime(D arg) {
        PTime<D> rv = aliasFactory.<PTime<D>>getCurrentAndReset();
        return rv != null ? rv : factory.createTime(arg);
    }

}
