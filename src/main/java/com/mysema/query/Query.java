package com.mysema.query;

import com.mysema.query.grammar.Types.*;

/**
 * Query provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<?>>{
    A select(Expr<?>... objects);
    A from(EntityPathExpr<?>... objects);    
    A where(Expr<Boolean>... objects);
    A groupBy(Expr<?>... objects);
    A orderBy(OrderSpecifier<?>... objects);
    
    // TODO : move back to ExtQuery when querydsl has stabilized
    A innerJoin(EntityPathExpr<?>... objects);
    A leftJoin(EntityPathExpr<?>... objects); 
    A with(Expr<Boolean>... objects);
}