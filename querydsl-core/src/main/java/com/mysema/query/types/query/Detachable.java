/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import com.mysema.query.types.Expr;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.ETime;

/**
 * Detachable defines methods for the construction of SubQuery instances
 * 
 * @author tiwe
 *
 */
public interface Detachable {

    /**
     * Return the count of matched rows as a sub query
     * 
     * @return
     */
    ObjectSubQuery<Long> count();
    
    /**
     * Create an exists(this) expression
     * 
     * @return
     */
    EBoolean exists();
    
    /**
     * Create a projection expression for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     *            rest
     * @return a List over the projection
     */
    ListSubQuery<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest);

    /**
     * 
     * 
     * @param args
     * @return
     */
    ListSubQuery<Object[]> list(Expr<?>[] args);
    
    /**
     * Create a projection expression for the given projection
     * 
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> ListSubQuery<RT> list(Expr<RT> projection);
    
    /**
     * Create an not exists(this) expression
     * 
     * @return
     */
    EBoolean notExists();

    /**
     * Create a projection expression for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     * @return
     */
    ObjectSubQuery<Object[]> unique(Expr<?> first, Expr<?> second, Expr<?>... rest);
    
    /**
     * Create a projection expression for the given projection
     * 
     * @param args
     * @return
     */
    ObjectSubQuery<Object[]> unique(Expr<?>[] args);

    /**
     * Create a subquery expression for the given projection
     * 
     * @param <RT>
     *            return type
     * @param projection
     * @return the result or null for an empty result
     */
    <RT> ObjectSubQuery<RT> unique(Expr<RT> projection);
    
    /**
     * Create a subquery expression for the given projection
     * 
     * @param projection
     * @return
     */
    BooleanSubQuery unique(EBoolean projection);  
    
    /**
     * Create a subquery expression for the given projection
     * 
     * @param projection
     * @return
     */
    StringSubQuery unique(EString projection);
    
    /**
     * Create a subquery expression for the given projection
     * 
     * @param <RT>
     * @param projection
     * @return
     */
    <RT extends Comparable<?>> ComparableSubQuery<RT> unique(EComparable<RT> projection);
    
    /**
     * Create a subquery expression for the given projection
     * 
     * @param <RT>
     * @param projection
     * @return
     */
    <RT extends Comparable<?>> DateSubQuery<RT> unique(EDate<RT> projection);
    
    /**
     * Create a subquery expression for the given projection
     * 
     * @param <RT>
     * @param projection
     * @return
     */
    <RT extends Comparable<?>> DateTimeSubQuery<RT> unique(EDateTime<RT> projection);
    
    /**
     * Create a subquery expression for the given projection
     * 
     * @param <RT>
     * @param projection
     * @return
     */
    <RT extends Comparable<?>> TimeSubQuery<RT> unique(ETime<RT> projection);
    
    /**
     * Create a subquery expression for the given projection
     * 
     * @param <RT>
     * @param projection
     * @return
     */
    <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(ENumber<RT> projection);
    
    

}
