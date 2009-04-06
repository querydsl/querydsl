package com.mysema.query;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.query.grammar.types.Expr;

/**
 * QueryBaseWithProjection extends the QueryBase interface to provide basic implementations of the methods
 * of the Projectable itnerface
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class QueryBaseWithProjection<JoinMeta,SubType 
    extends QueryBaseWithProjection<JoinMeta,SubType>> extends QueryBase<JoinMeta,SubType> implements Projectable{

    public QueryBaseWithProjection(){}
    
    public QueryBaseWithProjection(QueryMetadata<JoinMeta> metadata) {
        super(metadata);
    }

    protected <A> A[] asArray(A[] target, A first, A second, A... rest) {
        target[0] = first;
        target[1] = second;
        System.arraycopy(rest, 0, target, 2, rest.length);
        return target;
    }
    
    @SuppressWarnings("unchecked")
    public Iterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        Expr<?>[] full = asArray(new Expr[rest.length + 2], first, second, rest);
        return iterate(new Expr.EArrayConstructor(Object.class, full));
    }
    
    public List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return IteratorUtils.toList(iterate(first, second, rest));
    }

    public <RT> List<RT> list(Expr<RT> projection) {
        return IteratorUtils.toList(iterate(projection));
    }

    public <RT> RT uniqueResult(Expr<RT> expr) {
        Iterator<RT> it = iterate(expr);
        return it.hasNext() ? it.next() : null;
    }
}
