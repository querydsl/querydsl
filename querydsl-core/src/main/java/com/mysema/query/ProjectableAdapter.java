package com.mysema.query;

import java.util.Iterator;
import java.util.List;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.util.Assert;

/**
 * ProjectableAdapter provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class ProjectableAdapter implements Projectable{
    
    private Projectable projectable;
    
    public ProjectableAdapter(){}
    
    public ProjectableAdapter(Projectable projectable){
        this.projectable = Assert.notNull(projectable);
    }

    public long count() {
        return projectable.count();
    }

    public Iterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.iterate(first, second, rest);
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        return projectable.iterate(projection);
    }

    public List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.list(first, second, rest);
    }

    public <RT> List<RT> list(Expr<RT> projection) {
        return projectable.list(projection);
    }

    public <RT> RT uniqueResult(Expr<RT> expr) {
        return projectable.uniqueResult(expr);
    }

    public void setProjectable(Projectable projectable) {
        this.projectable = Assert.notNull(projectable);
    }
    
}
