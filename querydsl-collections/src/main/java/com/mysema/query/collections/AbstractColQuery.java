package com.mysema.query.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Constructor;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.serialization.OperationPatterns;

/**
 * AbstractColQuery provides
 *
 * @author tiwe
 * @version $Id$
 */
public class AbstractColQuery<S extends AbstractColQuery<S>> {
    
    private static final OperationPatterns OPS_DEFAULT = new JavaOps();

    private final InnerQuery query;

    public AbstractColQuery() {
        this(OPS_DEFAULT);
    }

    public AbstractColQuery(OperationPatterns ops) {
        query = new InnerQuery(ops);
    }
    
    private <A> A[] asArray(A[] target, A first, A second, A... rest) {
        target[0] = first;
        target[1] = second;
        System.arraycopy(rest, 0, target, 2, rest.length);
        return target;
    }

    public <A> S from(Path<A> entity, A first, A... rest) {
        List<A> list = new ArrayList<A>(rest.length + 1);
        list.add(first);
        list.addAll(Arrays.asList(rest));
        return from(entity, list);
    }

    @SuppressWarnings("unchecked")
    public <A> S from(Path<A> path, Iterable<A> col) {
        query.alias(path, col).from(path);
        return (S)this;
    }
    
    @SuppressWarnings("unchecked")
    public Iterable<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        final Expr<?>[] full = asArray(new Expr[rest.length + 2], e1, e2, rest);
        boolean oneType = true;
        if (e1.getType().isAssignableFrom((e2.getType()))){
            for (Expr<?> e : rest){
                if (!e1.getType().isAssignableFrom(e.getType())){
                    oneType = false;
                }
            }
        }else{
            oneType = false;
        }
        Class<?> type = e1.getType();
        if (!oneType){
            type = Object.class;    
        }  
        return query.iterate(new Constructor.CArray(type, full));
    }    
    
    public <RT> Iterable<RT> iterate(Expr<RT> projection) {
        return query.iterate(projection);
    }
    
    public List<Object[]> list(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        ArrayList<Object[]> rv = new ArrayList<Object[]>();
        for (Object[] v : iterate(e1, e2, rest)){
            rv.add(v);
        }
        return rv;
    }
    
    public <RT> List<RT> list(Expr<RT> projection) {
        ArrayList<RT> rv = new ArrayList<RT>();
        for (RT v : iterate(projection)){
            rv.add(v);
        }
        return rv;
    }
    
    public <RT> RT uniqueResult(Expr<RT> expr) {
        Iterator<RT> it = iterate(expr).iterator();
        return it.hasNext() ? it.next() : null;
    }
        
    @SuppressWarnings("unchecked")
    public S orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return (S)this;
    }
    
    @SuppressWarnings("unchecked")
    public S where(Expr.EBoolean... o) {
        query.where(o);
        return (S)this;
    }

}
