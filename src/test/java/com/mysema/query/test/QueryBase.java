package com.mysema.query.test;

import com.mysema.query.Query;
import com.mysema.query.grammar.GrammarTypes.BooleanExpr;
import com.mysema.query.grammar.GrammarTypes.DomainType;
import com.mysema.query.grammar.GrammarTypes.Expr;
import com.mysema.query.grammar.GrammarTypes.OrderSpecifier;

/**
 * QueryBase provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryBase implements Query<QueryBase> {

    public QueryBase from(DomainType<?>... objects) {
        return this;
    }

    public QueryBase groupBy(Expr... objects) {
        return this;
    }

    public QueryBase orderBy(OrderSpecifier... objects) {
        return this;
    }

    public QueryBase select(Expr... objects) {
        return this;
    }

    public QueryBase where(BooleanExpr... objects) {
        return this;
    }

    public QueryBase leftJoin(DomainType<?>... objects) {
        return this;
    }

    public QueryBase with(BooleanExpr... objects) {
        return this;
    }

    public QueryBase innerJoin(DomainType<?>... objects) {
        return this;
    }

}
