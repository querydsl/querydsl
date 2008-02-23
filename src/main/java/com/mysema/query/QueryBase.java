package com.mysema.query;

import java.util.ArrayList;
import java.util.List;

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
@SuppressWarnings("unchecked")
public class QueryBase<A extends QueryBase<A>> implements ExtQuery<A> {
    public enum JoinType{
        DEFAULT,IJ,LJ,J
    }
    
    public class JoinExpression{
        public final JoinType type;
        public final EntityExpr<?> target;
        JoinExpression(JoinType type, EntityExpr<?> target){
            this.type = type;
            this.target = target;
        }
        public BooleanExpr[] conditions;
    }
    
    protected List<JoinExpression> joins = new ArrayList<JoinExpression>();
    protected Expr<?>[] groupBy;
    protected BooleanExpr[] having;
    protected OrderSpecifier<?>[] orderBy;
    protected Expr<?>[] select;
    protected BooleanExpr[] where;
    protected BooleanExpr[] with;
    
    protected void clear(){
        joins.clear();
        groupBy = null;
        having = null;
        orderBy = null;
        select = null;
        where = null;
    }
    
    public A from(EntityExpr<?>... objects) {
        // TODO
        return (A) this;
    }

    public A groupBy(Expr<?>... objects) {
        groupBy = objects;
        return (A) this;
    }

    public A having(BooleanExpr... objects) {
        having = objects;
        return (A) this;
    }

    public A innerJoin(EntityExpr<?> object) {
//        innerJoin = objects;
        return (A) this;
    }
    
    public A join(EntityExpr<?> object) {
//        join = objects;
        return (A) this;
    }

    public A leftJoin(EntityExpr<?> object) {
//        leftJoin = objects;
        return (A) this;
    }

    public A orderBy(OrderSpecifier<?>... objects) {
        orderBy = objects;
        return (A) this;
    }

    public A select(Expr<?>... objects) {
        select = objects;
        return (A) this;
    }
    
    public A where(BooleanExpr... objects) {
        where = objects;
        return (A) this;
    }
    
    public A with(BooleanExpr... objects) {
        with = objects;
        return (A) this;
    }

}
