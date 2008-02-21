package com.mysema.query.test;

import com.mysema.query.ExtQuery;
import com.mysema.query.grammar.Types.BooleanExpr;
import com.mysema.query.grammar.Types.EntityExpr;
import com.mysema.query.grammar.Types.Expr;
import com.mysema.query.grammar.Types.OrderSpecifier;
/**
 * QueryBase provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryBase implements ExtQuery<QueryBase> {
    
    public QueryBase from(EntityExpr<?>... objects) {
       return this;
    }

    public QueryBase groupBy(Expr<?>... objects) {
        return this;
    }

    public QueryBase orderBy(OrderSpecifier<?>... objects) {
        return this;
    }

    public QueryBase select(Expr<?>... objects) {
        return this;
    }

    public QueryBase where(BooleanExpr... objects) {        
        return this;
    }

    public QueryBase innerJoin(EntityExpr<?>... objects) {
        return this;
    }

    public QueryBase leftJoin(EntityExpr<?>... objects) {
        return this;
    }

    public QueryBase with(BooleanExpr... objects) {
        return this;
    }   

}
