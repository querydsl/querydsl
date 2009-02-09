/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import com.mysema.query.ExprFactory;
import com.mysema.query.alias.AliasAwareExprFactory;
import com.mysema.query.alias.AliasFactory;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.ExtTypes.ExtString;
import com.mysema.query.grammar.types.Path.*;

/**
 * GrammarWithAlias extends the base grammar to include alias factory methods
 *
 * @author tiwe
 * @version $Id$
 */
public class GrammarWithAlias extends Grammar{

    private static final AliasFactory aliasFactory = new AliasFactory();
    
    private static final ExprFactory exprFactory = new AliasAwareExprFactory(aliasFactory);
    
    private static final PSimple<Object> it = new PSimple<Object>(Object.class,PathMetadata.forVariable("it"));
    
    public static <A> A alias(Class<A> cl, String var){
        return aliasFactory.createAliasForVar(cl, var);
    }
        
    public static EBoolean $(Boolean arg){
        return exprFactory.createBoolean(arg);
    }
    
    public static <D extends Comparable<? super D>> EComparable<D> $(D arg){
        return exprFactory.createComparable(arg);
    }
    
//    BigDecimal
//    BigInteger
//    byte
//    double
//    float
//    integer
//    long
//    short
    
    public static ENumber<BigDecimal> $(BigDecimal arg){
        return exprFactory.createNumber(arg);
    }
    
    public static ENumber<BigInteger> $(BigInteger arg){
        return exprFactory.createNumber(arg);
    }
    
    public static ENumber<Byte> $(Byte arg){
        return exprFactory.createNumber(arg);
    }
    
    public static ENumber<Double> $(Double arg){
        return exprFactory.createNumber(arg);
    }
    
    public static ENumber<Float> $(Float arg){
        return exprFactory.createNumber(arg);
    }
    
    public static ENumber<Integer> $(Integer arg){
        return exprFactory.createNumber(arg);
    }
    
    public static ENumber<Long> $(Long arg){
        return exprFactory.createNumber(arg);
    }
    
    public static ENumber<Short> $(Short arg){
        return exprFactory.createNumber(arg);
    }    
    
    public static ExtString $(String arg){
        return exprFactory.createString(arg);
    }

    public static PBooleanArray $(Boolean[] args){
        return exprFactory.createBooleanArray(args);
    }
    
    public static <D extends Comparable<? super D>> PComparableArray<D> $(D[] args){
        return exprFactory.createComparableArray(args);
    }
    
    public static PStringArray $(String[] args){
        return exprFactory.createStringArray(args);
    }
    
    public static <D> PEntityCollection<D> $(Collection<D> args){
        return exprFactory.createEntityCollection(args);
    }
    
    public static <D> PEntityList<D> $(List<D> args){
        return exprFactory.createEntityList(args);
    }
    
    public static <D> PEntity<D> $(D arg){
        return exprFactory.createEntity(arg);
    }

    @SuppressWarnings("unchecked")
    public static <D> PSimple<D> $(){
        return (PSimple<D>) it;
    }
    
}
