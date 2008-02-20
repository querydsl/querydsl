package com.mysema.query.test;

import com.mysema.query.*;
import com.mysema.query.grammar.Types.*;
/**
 * QueryBase provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryBase implements ExtQuery<QueryBase> {
    
    public QueryBase from(EntityPathExpr<?>... objects) {
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

    public QueryBase where(Expr<Boolean>... objects) {
        
        return this;
    }

    public QueryBase innerJoin(EntityPathExpr<?>... objects) {
        return this;
    }

    public QueryBase leftJoin(EntityPathExpr<?>... objects) {
        return this;
    }

    public QueryBase with(Expr<Boolean>... objects) {
        return this;
    }

   

}
