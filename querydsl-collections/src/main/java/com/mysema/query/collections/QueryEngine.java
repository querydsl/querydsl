/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.List;
import java.util.Map;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expr;

/**
 * QueryEngine defines an interface for the evaluation of ColQuery queries
 * 
 * @author tiwe
 *
 */
public interface QueryEngine {
    
    /**
     * Default instance
     */
    QueryEngine DEFAULT = new DefaultQueryEngine(new DefaultEvaluatorFactory(ColQueryTemplates.DEFAULT));
    
    /**
     * Evaluate the given query and return the count of matched rows
     * 
     * @param metadata
     * @param iterables
     * @return
     */
    long count(QueryMetadata metadata, Map<Expr<?>, Iterable<?>> iterables);
    
    /**
     * Evaluate the given query and return the projection as a list
     * 
     * @param metadata
     * @param iterables
     * @return
     */
    <T> List<T> list(QueryMetadata metadata, Map<Expr<?>, Iterable<?>> iterables, Expr<T> projection);
    
}