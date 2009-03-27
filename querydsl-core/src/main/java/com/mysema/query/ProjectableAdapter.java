package com.mysema.query;

import java.util.Iterator;
import java.util.List;

import com.mysema.query.grammar.types.Expr;

/**
 * ProjectableAdapter provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class ProjectableAdapter implements Projectable{
    
    private Projectable projectable;
    
    public ProjectableAdapter(Projectable projectable){
        this.projectable = projectable;
    }

    public long count() {
        return projectable.count();
    }

    public Iterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        return projectable.iterate(e1, e2, rest);
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        return projectable.iterate(projection);
    }

    public List<Object[]> list(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        return projectable.list(e1, e2, rest);
    }

    public <RT> List<RT> list(Expr<RT> projection) {
        return projectable.list(projection);
    }

    public <RT> RT uniqueResult(Expr<RT> expr) {
        return projectable.uniqueResult(expr);
    }

}
