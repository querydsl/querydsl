/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Collection;
import java.util.List;

import com.mysema.query.ExprFactory;
import com.mysema.query.alias.AliasAwareExprFactory;
import com.mysema.query.alias.AliasFactory;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
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
        return exprFactory.create(arg);
    }
    
    public static <D extends Comparable<D>> EComparable<D> $(D arg){
        return exprFactory.create(arg);
    }
    
    public static ExtString $(String arg){
        return exprFactory.create(arg);
    }

    public static PBooleanArray $(Boolean[] args){
        return exprFactory.create(args);
    }
    
    public static <D extends Comparable<D>> PComparableArray<D> $(D[] args){
        return exprFactory.create(args);
    }
    
    public static PStringArray $(String[] args){
        return exprFactory.create(args);
    }
    
    public static <D> PEntityCollection<D> $(Collection<D> args){
        return exprFactory.create(args);
    }
    
    public static <D> PEntityList<D> $(List<D> args){
        return exprFactory.create(args);
    }
    
    public static <D> PEntity<D> $(D arg){
        return exprFactory.create(arg);
    }

    @SuppressWarnings("unchecked")
    public static <D> PSimple<D> $(){
        return (PSimple<D>) it;
    }
    
}
