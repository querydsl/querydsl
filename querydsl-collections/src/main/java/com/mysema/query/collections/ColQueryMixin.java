/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.CollectionAnyVisitor;
import com.mysema.query.support.Context;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.template.BooleanTemplate;

/**
 * ColQueryMixin extends QueryMixin 
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class ColQueryMixin<T> extends QueryMixin<T> {
    
    public ColQueryMixin() {}

    public ColQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }

    public ColQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }

    @Override
    protected Predicate[] normalize(Predicate[] conditions, boolean where) {
        for (int i = 0; i < conditions.length; i++){
            conditions[i] = normalize(conditions[i], where);
        }
        return conditions;
    }

    @SuppressWarnings("unchecked")
    private Predicate normalize(Predicate predicate, boolean where) {
        if (predicate instanceof BooleanBuilder && ((BooleanBuilder)predicate).getValue() == null){
            return predicate;
        }else{
            Context context = new Context();
            Predicate transformed = (Predicate) predicate.accept(CollectionAnyVisitor.DEFAULT, context);
            for (int i = 0; i < context.paths.size(); i++){
                innerJoin(
                    (CollectionExpression)context.paths.get(i).getMetadata().getParent(), 
                    (Path)context.replacements.get(i));
                on(BooleanTemplate.create("any"));
            }
            return transformed;    
        }        
    }
}
