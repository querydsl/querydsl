/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.BeanMap;

import com.mysema.query.dml.UpdateClause;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;

/**
 * ColUpdateClause is an implementation of the UpdateClause interface for Querydsl Collections
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class ColUpdateClause<T> implements UpdateClause<ColUpdateClause<T>>{

    private final Path<T> expr;
    
    private final Map<Path<?>,Object> paths = new HashMap<Path<?>,Object>();
    
    private final ColQuery query;
    
    public ColUpdateClause(QueryEngine qe, Path<T> expr, Iterable<? extends T> col){
        this.query = new ColQueryImpl(qe).from(expr, col);
        this.expr = expr;
    }
    
    public ColUpdateClause(Path<T> expr, Iterable<? extends T> col){
        this(QueryEngine.DEFAULT, expr, col);
    }
    
    @Override
    public long execute() {
        int rv = 0;
        for (T match : query.list(expr.asExpr())){
            BeanMap beanMap = new BeanMap(match);
            for (Map.Entry<Path<?>,Object> entry : paths.entrySet()){
                // TODO : support deep updates as well
                String propertyName = entry.getKey().getMetadata().getExpression().toString();
                beanMap.put(propertyName, entry.getValue());
            }
            rv++;
        }
        return rv;
    }

    @Override
    public <U> ColUpdateClause<T> set(Path<U> path, U value) {
        paths.put(path, value);
        return this;
    }

    @Override
    public ColUpdateClause<T> set(List<? extends Path<?>> p, List<?> v) {
        for (int i = 0; i < p.size(); i++){
            paths.put(p.get(i), v.get(i));
        }
        return this;
    }
    
    @Override
    public ColUpdateClause<T> where(EBoolean... o) {
        query.where(o);
        return this;
    }


}
