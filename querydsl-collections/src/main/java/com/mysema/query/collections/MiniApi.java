/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Arrays;
import java.util.Collection;

import com.mysema.query.alias.Alias;
import com.mysema.query.collections.dml.ColDeleteClause;
import com.mysema.query.collections.dml.ColUpdateClause;
import com.mysema.query.collections.impl.ColQueryImpl;
import com.mysema.query.collections.impl.EvaluatorFactory;
import com.mysema.query.types.path.Path;

/**
 * MiniApi provides static convenience methods for query construction
 * 
 * @author tiwe
 * @version $Id$
 */
public final class MiniApi {
    
    private MiniApi(){}
    
    private static final ColQueryTemplates templates = new ColQueryTemplates();

    private static final EvaluatorFactory evaluatorFactory = new EvaluatorFactory(templates);
    
    private static ColQuery query(){
        return new ColQueryImpl(evaluatorFactory);
    }
    
    public static <A> ColDeleteClause<A> delete(Path<A> path, Collection<A> col){
        return new ColDeleteClause<A>(evaluatorFactory, path, col);
    }
    
    public static <A> ColUpdateClause<A> update(Path<A> path, Iterable<A> col){
        return new ColUpdateClause<A>(evaluatorFactory, path, col);
    }
    
    public static <A> ColQuery from(Path<A> path, A... arr) {
        return query().from(path, Arrays.asList(arr));
    }

    public static <A> ColQuery from(Path<A> path, Iterable<A> col) {
        return query().from(path, col);
    }

    public static <A> ColQuery from(A alias, Iterable<A> col) {
        return query().from(Alias.$(alias), col);
    }

}
