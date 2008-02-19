package com.mysema.query;

import com.mysema.query.QueryDsl.BooleanExpr;
import com.mysema.query.QueryDsl.DomainType;
import com.mysema.query.QueryDsl.Expr;
import com.mysema.query.QueryDsl.OrderSpecifier;

/**
 * Query provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<?>>{
    A select(Expr... objects);
    A from(DomainType<?>... objects);
    A leftJoin(DomainType<?>... objects);
    A with(BooleanExpr... objects);
    A where(BooleanExpr... objects);
    A groupBy(Expr... objects);
    A orderBy(OrderSpecifier... objects);                        
}