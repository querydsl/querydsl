package com.mysema.query.lucene;

import org.apache.lucene.search.Query;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.StringConstant;

/**
 * QueryElement wraps a Lucene Query
 *
 * @author tiwe
 *
 */
public class QueryElement extends BooleanExpression{

    private static final long serialVersionUID = 470868107363840155L;

    private final Query query;

    private volatile StringExpression expr;

    public QueryElement(Query query){
        this.query = query;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        if (expr == null){
            expr = StringConstant.create(query.toString());
        }
        return expr.accept(v, context);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof QueryElement && ((QueryElement)o).query.equals(query);
    }

    @Override
    public int hashCode(){
        return query.hashCode();
    }

    public Query getQuery() {
        return query;
    }

}
