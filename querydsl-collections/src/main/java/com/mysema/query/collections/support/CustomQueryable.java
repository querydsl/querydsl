package com.mysema.query.collections.support;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mysema.query.ProjectableAdapter;
import com.mysema.query.collections.ColQuery;
import com.mysema.query.collections.IteratorSource;
import com.mysema.query.collections.QueryIndexSupport;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EEntity;
import com.mysema.query.util.Assert;

/**
 * CustomQueryable a ColQuery like interface for querying on custom IteratorSource sources
 *
 * @author tiwe
 * @version $Id$
 */
public class CustomQueryable extends ProjectableAdapter{

    private IteratorSource iteratorSource;
    
    private ColQuery innerQuery;
    
    public CustomQueryable(final IteratorSource iteratorSource){
        this.iteratorSource = Assert.notNull(iteratorSource);
        this.innerQuery = new ColQuery(){
            @Override
            protected QueryIndexSupport createIndexSupport(Map<Expr<?>, Iterable<?>> exprToIt, JavaOps ops, List<Expr<?>> sources){
                return new DefaultIndexSupport(iteratorSource, ops, sources);
            }    
        };
        setProjectable(innerQuery);
    }
    
    public <A> CustomQueryable from(EEntity<A> entity){
        innerQuery.from(entity, Collections.<A>emptyList());
        return this;
    }
    
    public CustomQueryable where(Expr.EBoolean... o) {
        innerQuery.where(o);
        return this;
    }
    
    
}
