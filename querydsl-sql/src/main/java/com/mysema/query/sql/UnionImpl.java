package com.mysema.query.sql;

import java.util.List;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;

/**
 * @author tiwe
 *
 * @param <Q>
 * @param <RT>
 */
public class UnionImpl<Q extends Query & Projectable, RT> implements Union<RT> {
    
    private final Q query;
    
    private final Expression<?>[] projection;
    
    public UnionImpl(Q query, List<? extends Expression<?>> projection) {
        this.query = query;
        this.projection = projection.toArray(new Expression[projection.size()]);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<RT> list() {
        if (projection.length == 1) {
            return (List<RT>) query.list(projection[0]);
        } else {
            return (List<RT>) query.list(projection);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public CloseableIterator<RT> iterate() {
        if (projection.length == 1) {
            return (CloseableIterator<RT>) query.iterate(projection[0]);
        } else {
            return (CloseableIterator<RT>) query.iterate(projection);
        }
    }

    @Override
    public Union<RT> orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return this;
    }

    @Override
    public String toString() {
        return query.toString();
    }

}
