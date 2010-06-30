/*
 * Copyright (c) 2010 Mysema Ltd.
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
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.types.Expr;
import com.mysema.query.types.path.*;

/**
 * Alias provides alias factory methods
 *
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("PMD")
public final class Alias {

    private static final AliasFactory aliasFactory = new AliasFactory();

    private static final PSimple<Object> it = new PSimple<Object>(Object.class, PathMetadataFactory.forVariable("it"));

    // exclude $-methods from Checkstyle checks
    //CHECKSTYLE:OFF
    /**
     * Convert the given alias to an expression
     *
     * @param <D>
     * @return
     */
    public static <D extends Expr<?>> D $() {
        return aliasFactory.<D>getCurrentAndReset();
    }

    public static <D> PArray<D> $(D[] arg){
    return aliasFactory.<PArray<D>> getCurrentAndReset();
    }

    public static PNumber<BigDecimal> $(BigDecimal arg) {
        return aliasFactory.<PNumber<BigDecimal>> getCurrentAndReset();
    }

    public static PNumber<BigInteger> $(BigInteger arg) {
        return aliasFactory.<PNumber<BigInteger>> getCurrentAndReset();
    }

    public static PBoolean $(Boolean arg) {
        return aliasFactory.<PBoolean> getCurrentAndReset();
    }

    public static PNumber<Byte> $(Byte arg) {
        return aliasFactory.<PNumber<Byte>> getCurrentAndReset();
    }

    public static <D> PCollection<D> $(Collection<D> args) {
        return aliasFactory.<PCollection<D>> getCurrentAndReset();
    }

    public static <D extends Comparable<?>> PComparable<D> $(D arg) {
        return aliasFactory.<PComparable<D>> getCurrentAndReset();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <D> PEntity<D> $(D arg) {
        PEntity<D> rv = aliasFactory.<PEntity<D>> getCurrentAndReset();
        if (rv != null) {
            return rv;
        }else if (arg instanceof PEntity<?>){    
            return (PEntity)arg;
        } else if (arg instanceof ManagedObject) {
            return (PEntity<D>) ((ManagedObject) arg).__mappedPath();
        } else {
            return null;
        }
    }

    public static PNumber<Double> $(Double arg) {
        return aliasFactory.<PNumber<Double>> getCurrentAndReset();
    }

    public static PNumber<Float> $(Float arg) {
        return aliasFactory.<PNumber<Float>> getCurrentAndReset();
    }

    public static PNumber<Integer> $(Integer arg) {
        return aliasFactory.<PNumber<Integer>> getCurrentAndReset();
    }

    public static PDate<java.sql.Date> $(java.sql.Date arg) {
        return aliasFactory.<PDate<java.sql.Date>> getCurrentAndReset();
    }

    public static PDateTime<java.util.Date> $(java.util.Date arg) {
        return aliasFactory.<PDateTime<java.util.Date>> getCurrentAndReset();
    }

    public static <D> PList<D,?> $(List<D> args) {
        return aliasFactory.<PList<D,?>> getCurrentAndReset();
    }

    public static PNumber<Long> $(Long arg) {
        return aliasFactory.<PNumber<Long>> getCurrentAndReset();
    }

    public static <K, V> PMap<K, V,?> $(Map<K, V> args) {
        return aliasFactory.<PMap<K, V,?>> getCurrentAndReset();
    }

    public static <D> PSet<D> $(Set<D> args) {
        return aliasFactory.<PSet<D>> getCurrentAndReset();
    }

    public static PNumber<Short> $(Short arg) {
        return aliasFactory.<PNumber<Short>> getCurrentAndReset();
    }

    public static PString $(String arg) {
        return aliasFactory.<PString> getCurrentAndReset();
    }

    public static PTime<Time> $(Time arg) {
        return aliasFactory.<PTime<Time>> getCurrentAndReset();
    }

    public static PDateTime<Timestamp> $(Timestamp arg) {
        return aliasFactory.<PDateTime<Timestamp>> getCurrentAndReset();
    }
    //CHECKSTYLE:ON

    public static <A> A alias(Class<A> cl) {
        return alias(cl, StringUtils.uncapitalize(cl.getSimpleName()));
    }

    public static <A> A alias(Class<A> cl, Expr<? extends A> expr) {
        return aliasFactory.createAliasForExpr(cl, expr);
    }

    public static <A> A alias(Class<A> cl, String var) {
        return aliasFactory.createAliasForVariable(cl, var);
    }

    /**
     * Convert the given alias to an expression
     *
     * @param <D>
     * @param arg
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <D> Expr<D> getAny(D arg) {
        Expr<D> current = (Expr<D>) aliasFactory.getCurrentAndReset();
        if (current != null) {
            return current;
        } else if (arg instanceof ManagedObject) {
            return (Expr<D>) ((ManagedObject) arg).__mappedPath();
        } else {
            throw new IllegalArgumentException("No path mapped to " + arg);
        }
    }

    public static void resetAlias() {
        aliasFactory.reset();
    }

    @SuppressWarnings("unchecked")
    public static <D> PSimple<D> var(){
        return (PSimple<D>) it;
    }

    @SuppressWarnings("unchecked")
    public static <D extends Comparable<?>> PComparable<D> var(D arg) {
        return new PComparable(arg.getClass(), "var"+arg);
    }

    @SuppressWarnings("unchecked")
    public static <D extends Number & Comparable<D>> PNumber<D> var(D arg) {
        return new PNumber(arg.getClass(), "var" + arg.getClass().getSimpleName()+arg);
    }

    @SuppressWarnings("unchecked")
    public static <D> PEntity<D> var(D arg) {
        String var = "var"+ arg.getClass().getSimpleName() + "_" + arg.toString().replace(' ', '_');
        return new PathBuilder(arg.getClass(), var);
    }

    public static PString var(String arg) {
        return new PString(arg.replace(' ', '_'));
    }

    private Alias(){}

}
