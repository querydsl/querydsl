package com.mysema.query.collections;

import java.util.List;
import java.util.Map;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expr;

/**
 * @author tiwe
 *
 */
public interface QueryEngine {
    
    /**
     * Default instance
     */
    QueryEngine DEFAULT = new DefaultQueryEngine(new DefaultEvaluatorFactory(ColQueryTemplates.DEFAULT));
    
    /**
     * @param metadata
     * @param iterables
     * @return
     */
    long count(QueryMetadata metadata, Map<Expr<?>, Iterable<?>> iterables);
    
    /**
     * @param metadata
     * @param iterables
     * @return
     */
    List<?> list(QueryMetadata metadata, Map<Expr<?>, Iterable<?>> iterables);        

}