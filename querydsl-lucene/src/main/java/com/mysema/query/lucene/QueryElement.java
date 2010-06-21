package com.mysema.query.lucene;

import org.apache.lucene.search.Query;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringConst;

/**
 * QueryElement wraps a lucene Query
 *
 * @author tiwe
 *
 */
public class QueryElement extends EBoolean{

    private static final long serialVersionUID = 470868107363840155L;

    private final Query query;

    private volatile EString expr;

    public QueryElement(Query query){
        this.query = query;
    }

    @Override
    public void accept(Visitor v) {
        if (expr == null){
            expr = EStringConst.create(query.toString());
        }
        expr.accept(v);
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
