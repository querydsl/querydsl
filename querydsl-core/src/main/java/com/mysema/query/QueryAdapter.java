package com.mysema.query;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.util.Assert;

/**
 * QueryAdapter provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryAdapter<SubType extends QueryAdapter<SubType>> implements Query<SubType>{

    private Query<?> query;
    
    private SubType _this = (SubType)this;
    
    public QueryAdapter(){}
    
    public QueryAdapter(Query<?> query){
        this.query = Assert.notNull(query);
    }
    
    public SubType from(Expr<?>... o) {
        query.from(o);
        return _this;
    }

    public SubType fullJoin(Expr<?> o) {
        query.fullJoin(o);
        return _this;
    }

    public SubType groupBy(Expr<?>... o) {
        query.groupBy(o);
        return _this;
    }

    public SubType having(EBoolean... o) {
        query.having(o);
        return _this;
    }

    public SubType innerJoin(Expr<?> o) {
        query.innerJoin(o);
        return _this;
    }

    public SubType join(Expr<?> o) {
        query.join(o);
        return _this;
    }

    public SubType leftJoin(Expr<?> o) {
        query.leftJoin(o);
        return _this;
    }

    public SubType on(EBoolean o) {
        query.on(o);
        return _this;
    }

    public SubType orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return _this;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
    
}
