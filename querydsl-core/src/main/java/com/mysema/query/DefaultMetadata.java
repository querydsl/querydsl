package com.mysema.query;

import java.util.List;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * DefaultMetadata provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DefaultMetadata<JoinMeta> implements QueryMetadata<JoinMeta>{

    private List<? extends Expr<?>> groupBy;
    
    private EBoolean having;

    private List<JoinExpression<JoinMeta>> joins;

    private List<OrderSpecifier<?>> orderBy;

    private List<? extends Expr<?>> select;

    private EBoolean where;

    public List<? extends Expr<?>> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<? extends Expr<?>> groupBy) {
        this.groupBy = groupBy;
    }

    public EBoolean getHaving() {
        return having;
    }

    public void setHaving(EBoolean having) {
        this.having = having;
    }

    public List<JoinExpression<JoinMeta>> getJoins() {
        return joins;
    }

    public void setJoins(List<JoinExpression<JoinMeta>> joins) {
        this.joins = joins;
    }

    public List<OrderSpecifier<?>> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(List<OrderSpecifier<?>> orderBy) {
        this.orderBy = orderBy;
    }

    public List<? extends Expr<?>> getSelect() {
        return select;
    }

    public void setSelect(List<? extends Expr<?>> select) {
        this.select = select;
    }

    public EBoolean getWhere() {
        return where;
    }

    public void setWhere(EBoolean where) {
        this.where = where;
    }
    
    
    
}
