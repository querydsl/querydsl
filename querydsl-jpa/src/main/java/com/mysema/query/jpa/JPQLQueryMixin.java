/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.Context;
import com.mysema.query.support.ListAccessVisitor;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.TemplateExpressionImpl;
import com.mysema.query.types.path.ListPath;

/**
 * JPQLQueryMixin extends {@link QueryMixin} to support JPQL join construction
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class JPQLQueryMixin<T> extends QueryMixin<T> {
    
    private final Set<Path<?>> paths = new HashSet<Path<?>>();
    
    public static final JoinFlag FETCH = new JoinFlag("fetch ");
    
    public static final JoinFlag FETCH_ALL_PROPERTIES = new JoinFlag(" fetch all properties");
    
    public JPQLQueryMixin() {}

    public JPQLQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }

    public JPQLQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }

    public T fetch(){
        List<JoinExpression> joins = getMetadata().getJoins();
        joins.get(joins.size()-1).addFlag(FETCH);
        return getSelf();
    }

    public T fetchAll(){
        List<JoinExpression> joins = getMetadata().getJoins();
        joins.get(joins.size()-1).addFlag(FETCH_ALL_PROPERTIES);
        return getSelf();
    }

    public T with(Predicate... conditions){
        for (Predicate condition : normalize(conditions, false)){
            getMetadata().addJoinCondition(condition);
        }
        return getSelf();
    }
    
    @Override
    public <RT> Expression<RT> convert(Expression<RT> expr){
        return super.convert(Conversions.convert(expr));
    }
    
    @Override    
    protected Predicate normalize(Predicate predicate, boolean where) {
        if (predicate instanceof BooleanBuilder && ((BooleanBuilder)predicate).getValue() == null){
            return predicate;
        } else {
            // transform any usage
            predicate = (Predicate) predicate.accept(JPQLCollectionAnyVisitor.DEFAULT, new Context());
            
            // transform list access
            Context context = new Context();
            predicate = (Predicate) predicate.accept(ListAccessVisitor.DEFAULT, context);
            for (int i = 0; i < context.paths.size(); i++){
                Path<?> path = context.paths.get(i);            
                if (!paths.contains(path)){
                    addCondition(context, i, path, where);
                }
            }
            return predicate;
        }        
    }
    
    @SuppressWarnings("unchecked")
    private void addCondition(Context context, int i, Path<?> path, boolean where) {
        paths.add(path);
        EntityPath<?> alias = context.replacements.get(i);
        leftJoin((ListPath)path.getMetadata().getParent(), context.replacements.get(i));
        Expression index = TemplateExpressionImpl.create(Integer.class, "index({0})", alias);
        Predicate condition = ExpressionUtils.eq(index, path.getMetadata().getExpression()); 
        if (where){
            super.where(condition);
        } else {
            super.having(condition);
        }
    }
    
}
