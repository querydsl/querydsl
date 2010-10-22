/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo;

import java.util.HashSet;
import java.util.Set;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.CollectionAnyVisitor;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * JDOQLQueryMixin extends QueryMixin 
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class JDOQLQueryMixin<T> extends QueryMixin<T> {
    
    private final Set<Path<?>> anyPaths = new HashSet<Path<?>>();
    
    public JDOQLQueryMixin() {}

    public JDOQLQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }

    public JDOQLQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }
    
    @Override
    public T where(Predicate... o) {
        return super.where(normalize(o, true));
    }
    
    @Override
    public T having(Predicate... o) {
        return super.having(normalize(o, false));
    }

    private Predicate[] normalize(Predicate[] conditions, boolean where) {
        for (int i = 0; i < conditions.length; i++){
            conditions[i] = normalize(conditions[i], where);
        }
        return conditions;
    }

    private Predicate normalize(Predicate predicate, boolean where) {
        if (predicate instanceof BooleanBuilder && ((BooleanBuilder)predicate).getValue() == null){
            return predicate;
        }else{
            CollectionAnyVisitor.Context context = new CollectionAnyVisitor.Context();
            Predicate transformed = (Predicate) predicate.accept(JDOQLCollectionAnyVisitor.DEFAULT, context);
            for (int i = 0; i < context.anyPaths.size(); i++){
                Path<?> path = context.anyPaths.get(i);            
                if (!anyPaths.contains(path)){
                    addCondition(context, i, path, where);
                }
            }
            return transformed;    
        }        
    }

    @SuppressWarnings("unchecked")
    private void addCondition(CollectionAnyVisitor.Context context, int i, Path<?> path, boolean where) {
        anyPaths.add(path);
        EntityPath<?> alias = context.replacements.get(i);                 
        from(alias);
        Predicate condition = ExpressionUtils.in(alias, (CollectionExpression)path.getMetadata().getParent());
        if (where){
            super.where(condition);
        }else{
            super.having(condition);
        }
    }
    
}
