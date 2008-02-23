package com.mysema.query;

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
    
    protected EntityExpr<?>[] from;
    protected Expr<?>[] groupBy;
    protected BooleanExpr[] having;
//    protected EntityExpr<?>[] innerJoin;
//    protected EntityExpr<?>[] join;
//    protected EntityExpr<?>[] leftJoin;
    protected OrderSpecifier<?>[] orderBy;
    protected Expr<?>[] select;
    protected BooleanExpr[] where;
    protected BooleanExpr[] with;
    
    protected void clear(){
        from = null;
        groupBy = null;
        having = null;
//        innerJoin = null;
//        join = null;
//        leftJoin = null;
        orderBy = null;
        select = null;
        where = null;
        with = null;
    }
    
    public QueryBase from(EntityExpr<?>... objects) {
        from = objects;
        return this;
    }

    public QueryBase groupBy(Expr<?>... objects) {
        groupBy = objects;
        return this;
    }

    public QueryBase having(BooleanExpr... objects) {
        having = objects;
        return this;
    }

    public QueryBase innerJoin(EntityExpr<?> object) {
//        innerJoin = objects;
        return this;
    }
    
    public QueryBase join(EntityExpr<?> object) {
//        join = objects;
        return this;
    }

    public QueryBase leftJoin(EntityExpr<?> object) {
//        leftJoin = objects;
        return this;
    }

    public QueryBase orderBy(OrderSpecifier<?>... objects) {
        orderBy = objects;
        return this;
    }

    public QueryBase select(Expr<?>... objects) {
        select = objects;
        return this;
    }
    
    public QueryBase where(BooleanExpr... objects) {
        where = objects;
        return this;
    }
    
    public QueryBase with(BooleanExpr... objects) {
        with = objects;
        return this;
    }

}
