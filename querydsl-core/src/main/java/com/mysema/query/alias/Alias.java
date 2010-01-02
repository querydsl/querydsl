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
import java.util.Set;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PList;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSet;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * Alias provides alias factory methods
 * 
 * @author tiwe
 * @version $Id$
 */
public final class Alias {
    
    private Alias(){}

    private static final AliasFactory aliasFactory = new SimpleAliasFactory();

    private static final PSimple<Object> it = new PSimple<Object>(Object.class, PathMetadataFactory.forVariable("it"));

    /**
     * Create an alias for the given class with the given name
     * 
     * @param <A>
     * @param cl
     * @param var
     * @return
     */
    public static <A> A alias(Class<A> cl, String var) {
        return aliasFactory.createAliasForVariable(cl, var);
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
        return aliasFactory.<PBoolean> getCurrentAndReset();
    }

    public static <D extends Comparable<?>> PComparable<D> $(D arg) {
        return aliasFactory.<PComparable<D>> getCurrentAndReset();
    }

    public static PNumber<BigDecimal> $(BigDecimal arg) {
        return aliasFactory.<PNumber<BigDecimal>> getCurrentAndReset();
    }

    public static PNumber<BigInteger> $(BigInteger arg) {
        return aliasFactory.<PNumber<BigInteger>> getCurrentAndReset();
    }

    public static PNumber<Byte> $(Byte arg) {
        return aliasFactory.<PNumber<Byte>> getCurrentAndReset();
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

    public static PNumber<Long> $(Long arg) {
        return aliasFactory.<PNumber<Long>> getCurrentAndReset();
    }

    public static PNumber<Short> $(Short arg) {
        return aliasFactory.<PNumber<Short>> getCurrentAndReset();
    }

    public static PString $(String arg) {
        return aliasFactory.<PString> getCurrentAndReset();
    }

    public static <D> PCollection<D> $(Collection<D> args) {
        return aliasFactory.<PCollection<D>> getCurrentAndReset();
    }
    
    public static <D> PSet<D> $(Set<D> args) {
        return aliasFactory.<PSet<D>> getCurrentAndReset();
    }
    
    public static PDateTime<java.util.Date> $(java.util.Date arg) {
        return aliasFactory.<PDateTime<java.util.Date>> getCurrentAndReset();
    }
    
    public static PDate<java.sql.Date> $(java.sql.Date arg) {
        return aliasFactory.<PDate<java.sql.Date>> getCurrentAndReset();
    }
    
    public static PDateTime<Timestamp> $(Timestamp arg) {
        return aliasFactory.<PDateTime<Timestamp>> getCurrentAndReset();
    }
    
    public static PTime<Time> $(Time arg) {
        return aliasFactory.<PTime<Time>> getCurrentAndReset();
    }

    public static <K, V> PMap<K, V,?> $(Map<K, V> args) {
        return aliasFactory.<PMap<K, V,?>> getCurrentAndReset();
    }

    public static <D> PList<D,?> $(List<D> args) {
        return aliasFactory.<PList<D,?>> getCurrentAndReset();
    }

    @SuppressWarnings("unchecked")
    public static <D> PEntity<D> $(D arg) {
        PEntity<D> rv = aliasFactory.<PEntity<D>> getCurrentAndReset();
        if (rv != null) {
            return rv;
        } else if (arg instanceof ManagedObject) {
            return (PEntity<D>) ((ManagedObject) arg).__mappedPath();
        } else {
            return null;
        }
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
