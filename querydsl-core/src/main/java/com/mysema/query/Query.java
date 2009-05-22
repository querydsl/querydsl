/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * Query provides a query interface of the fluent query DSL.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Query<SubType extends Query<SubType>>{
    /**
     * use the given query sources for the query
     * 
     * @param sources
     * @return the Query itself
     */
    SubType from(Expr<?>... sources);
    
    /**
     * inner join the given source
     * 
     * @param source
     * @return the Query itself
     */
    SubType innerJoin(Expr<?> source);
    
    /**
     * join the given source
     * 
     * @param source
     * @return the Query itself
     */
    SubType join(Expr<?> source);
    
    /**
     * full join the given source
     * 
     * @param source
     * @return the Query itself
     */
    SubType fullJoin(Expr<?> source);
    
    /**
     * left join the given source
     * 
     * @param source
     * @return the Query itself
     */
    SubType leftJoin(Expr<?> o);

    /**
     * set the join condition for that last supplied join source
     * 
     * @param o
     * @return the Query itself
     */
    SubType on(EBoolean o);    
    
    /**
     * set group by aggregation parameters
     * 
     * @param o
     * @return the Query itself
     */
    SubType groupBy(Expr<?>... o);
    
    /**
     * set the constraints of the group by aggreation
     * 
     * @param o
     * @return the Query itself
     */
    SubType having(EBoolean... o);
    
    /**
     * set the order constraints of the query
     * 
     * @param o
     * @return the Query itself
     */
    SubType orderBy(OrderSpecifier<?>... o);
}