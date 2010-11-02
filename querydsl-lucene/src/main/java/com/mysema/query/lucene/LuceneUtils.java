/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.lucene;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;

import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Utility methods to create filter expressions for Lucene queries that are not covered by the Querydsl standard expression model
 * 
 * @author tiwe
 *
 */
public final class LuceneUtils {
    
    /**
     * Create a fuzzy query
     * 
     * @param path
     * @param value
     * @return
     */
    public static BooleanExpression fuzzyLike(Path<String> path, String value){
        Term term = new Term(path.getMetadata().getExpression().toString(), value);
        return new QueryElement(new FuzzyQuery(term));
    }
    
    /**
     * Create a fuzzy query
     * 
     * @param path
     * @param value
     * @param minimumSimilarity
     * @return
     */
    public static BooleanExpression fuzzyLike(Path<String> path, String value, float minimumSimilarity){
        Term term = new Term(path.getMetadata().getExpression().toString(), value);
        return new QueryElement(new FuzzyQuery(term, minimumSimilarity));
    }
    
    /**
     * Create a fuzzy query
     * 
     * @param path
     * @param value
     * @param minimumSimilarity
     * @param prefixLength
     * @return
     */
    public static BooleanExpression fuzzyLike(Path<String> path, String value, float minimumSimilarity, int prefixLength){
        Term term = new Term(path.getMetadata().getExpression().toString(), value);
        return new QueryElement(new FuzzyQuery(term, minimumSimilarity, prefixLength));
    }
    
    private LuceneUtils(){}

}
