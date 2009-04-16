/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Iterator;
import java.util.List;

import com.mysema.query.grammar.types.Expr;

/**
 * Projectable defines default projection methods for Query implemnations
 *
 * @author tiwe
 * @version $Id$
 */
public interface Projectable {    
    /**
     * return the amount of matched rows
     */
    long count();
    
    /**
     * return the amount of distinct matched rows
     */
    long countDistinct();

    /**
     * iterate over the results with the given projection
     * 
     * @param first
     * @param second
     * @param rest
     * @return an Iterator over the projection
     */
    Iterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest);
    
    /**
     * iterate over the distinct results with the given projection
     * 
     * @param first
     * @param second
     * @param rest
     * @return an Iterator over the projection
     */
    Iterator<Object[]> iterateDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest);

    /**
     * iterate over the results with the given projection
     * 
     * @param <RT> generic type of the Iteratpr
     * @param projection
     * @return an Iterator over the projection
     */
    <RT> Iterator<RT> iterate(Expr<RT> projection);
    
    /**
     * iterate over the distinct results with the given projection
     * 
     * @param <RT> generic type of the Iteratpr
     * @param projection
     * @return an Iterator over the projection
     */
    <RT> Iterator<RT> iterateDistinct(Expr<RT> projection);

    /**
     * list the results with the given projection
     * 
     * @param first
     * @param second
     * @param rest rest
     * @return a List over the projection
     */
    List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest);
    
    /**
     * list the distinct results with the given projection
     * 
     * @param first
     * @param second
     * @param rest rest
     * @return a List over the projection
     */
    List<Object[]> listDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest);

    /**
     * list the results with the given projection
     * 
     * @param <RT> generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> List<RT> list(Expr<RT> projection);
    
    /**
     * list the distinct results with the given projection
     * 
     * @param <RT> generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> List<RT> listDistinct(Expr<RT> projection);

    /**
     * return a unique result for the given projection
     * 
     * @param <RT> return type
     * @param projection
     * @return the result or null for an empty result
     */
    <RT> RT uniqueResult(Expr<RT> projection);


}