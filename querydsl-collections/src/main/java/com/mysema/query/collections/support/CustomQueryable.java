/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.support;

import java.util.List;
import java.util.Map;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.ProjectableAdapter;
import com.mysema.query.QueryMetadata;
import com.mysema.query.collections.ColQuery;
import com.mysema.query.collections.IteratorSource;
import com.mysema.query.collections.QueryIndexSupport;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.util.Assert;

/**
 * CustomQueryable a ColQuery like interface for querying on custom IteratorSource sources
 *
 * @author tiwe
 * @version $Id$
 */
// TODO : find a better name for this
public class CustomQueryable<SubType extends CustomQueryable<SubType>> extends ProjectableAdapter{
    
    private final ColQuery innerQuery;

    @SuppressWarnings("unchecked")
    private final SubType _this = (SubType)this;
    
    public CustomQueryable(IteratorSource iteratorSource){
        this(iteratorSource, new DefaultQueryMetadata<Object>());
    }
    
    public CustomQueryable(final IteratorSource iteratorSource, QueryMetadata<Object> metadata){
        Assert.notNull(iteratorSource);
        this.innerQuery = new ColQuery(metadata){
            @Override
            protected QueryIndexSupport createIndexSupport(
                    Map<Expr<?>, 
                    Iterable<?>> exprToIt, 
                    JavaOps ops, 
                    List<Expr<?>> sources){
                return new DefaultIndexSupport(iteratorSource, ops, sources);
            }    
        };
        setProjectable(innerQuery);
    }
    
    
    protected ColQuery getInnerQuery(){
        return innerQuery;
    }
    
    
    public SubType from(Expr<?>... o) {
        innerQuery.from(o);
        return _this;
    }
    
    public SubType orderBy(OrderSpecifier<?>... o) {
        innerQuery.orderBy(o);
        return _this;
    }
     
    public SubType where(Expr.EBoolean... o) {
        innerQuery.where(o);
        return _this;
    }
    
}
