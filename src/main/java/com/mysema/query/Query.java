package com.mysema.query;

import com.mysema.query.grammar.GrammarTypes.BooleanExpr;
import com.mysema.query.grammar.GrammarTypes.DomainType;
import com.mysema.query.grammar.GrammarTypes.Expr;
import com.mysema.query.grammar.GrammarTypes.OrderSpecifier;

/**
 * Query provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<?>>{
    A select(Expr... objects);
    A from(DomainType<?>... objects);
    A innerJoin(DomainType<?>... objects);
    A leftJoin(DomainType<?>... objects);
    A with(BooleanExpr... objects);
    A where(BooleanExpr... objects);
    A groupBy(Expr... objects);
    A orderBy(OrderSpecifier... objects);                        
}