/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PBooleanArray;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PComparableArray;
import com.mysema.query.types.path.PComponentList;
import com.mysema.query.types.path.PComponentMap;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PStringArray;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.PathMetadata;

/**
 * Alias provides alias factory methods
 * 
 * @author tiwe
 * @version $Id$
 */
public final class Alias {
    
    private Alias(){}

    private static final AliasFactory aliasFactory = new SimpleAliasFactory();

    private static final PathFactory pathFactory = new AliasAwarePathFactory(aliasFactory);

    private static final PSimple<Object> it = new PSimple<Object>(Object.class,PathMetadata.forVariable("it"));

    /**
     * Create an alias for the given class with the given name
     * 
     * @param <A>
     * @param cl
     * @param var
     * @return
     */
    public static <A> A alias(Class<A> cl, String var) {
        return aliasFactory.createAliasForVar(cl, var);
    }

    /**
     * Create an alias for the given class with the given name
     * 
     * @param <A>
     * @param cl
     * @param expr
     * @return
     */
    public static <A> A alias(Class<A> cl, Expr<? extends A> expr) {
        return aliasFactory.createAliasForExpr(cl, expr);
    }

    public static void resetAlias() {        
        aliasFactory.reset();
    }

    public static PBoolean $(Boolean arg) {
        return pathFactory.createBoolean(arg);
    }

    public static <D extends Comparable<?>> PComparable<D> $(D arg) {
        return pathFactory.createComparable(arg);
    }

    public static PNumber<BigDecimal> $(BigDecimal arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<BigInteger> $(BigInteger arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Byte> $(Byte arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Double> $(Double arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Float> $(Float arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Integer> $(Integer arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Long> $(Long arg) {
        return pathFactory.createNumber(arg);
    }

    public static PNumber<Short> $(Short arg) {
        return pathFactory.createNumber(arg);
    }

    public static PString $(String arg) {
        return pathFactory.createString(arg);
    }

    public static PBooleanArray $(Boolean[] args) {
        return pathFactory.createBooleanArray(args);
    }

    public static <D extends Comparable<?>> PComparableArray<D> $(D[] args) {
        return pathFactory.createComparableArray(args);
    }

    public static PStringArray $(String[] args) {
        return pathFactory.createStringArray(args);
    }

    public static <D> PEntityCollection<D> $(Collection<D> args) {
        return pathFactory.createEntityCollection(args);
    }
    
    public static PDateTime<java.util.Date> $(java.util.Date arg) {
        return pathFactory.createDateTime(arg);
    }
    
    public static PDate<java.sql.Date> $(java.sql.Date arg) {
        return pathFactory.createDate(arg);
    }
    
    public static PDateTime<Timestamp> $(Timestamp arg) {
        return pathFactory.createDateTime(arg);
    }
    
    public static PTime<Time> $(Time arg) {
        return pathFactory.createTime(arg);
    }

    public static <K, V> PComponentMap<K, V> $(Map<K, V> args) {
        return pathFactory.createMap(args);
    }

    public static <D> PComponentList<D> $(List<D> args) {
        return pathFactory.createList(args);
    }

    public static <D> PEntity<D> $(D arg) {
        return pathFactory.createEntity(arg);
    }

    /**
     * Convert the given alias to an expression
     * 
     * @param <D>
     * @param arg
     * @return
     */
    public static <D> Expr<D> getAny(D arg) {
        return pathFactory.createAny(arg);
    }

    /**
     * Convert the given alias to an expression
     * 
     * @param <D>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <D> PSimple<D> $() {
        return (PSimple<D>) it;
    }

}
