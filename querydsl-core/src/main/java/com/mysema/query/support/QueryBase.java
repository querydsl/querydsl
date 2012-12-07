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
package com.mysema.query.support;

import com.mysema.query.QueryModifiers;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Predicate;

/**
 * QueryBase provides a stub for Query implementations
 *
 * @author tiwe
 */
public abstract class QueryBase<Q extends QueryBase<Q>> {

    protected final QueryMixin<Q> queryMixin;

    public QueryBase(QueryMixin<Q> queryMixin) {
        this.queryMixin = queryMixin;
    }

    public Q distinct() {
        return queryMixin.distinct();
    }
    
    public Q groupBy(Expression<?> e) {
        return queryMixin.groupBy(e);
    }
    
    public Q groupBy(Expression<?>... o) {
        return queryMixin.groupBy(o);
    }
    
    public Q having(Predicate e) {
        return queryMixin.having(e);
    }

    public Q having(Predicate... o) {
        return queryMixin.having(o);
    }
    
    public Q orderBy(OrderSpecifier<?> o) {
        return queryMixin.orderBy(o);
    }

    public Q orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }
    
    public Q where(Predicate o) {
        return queryMixin.where(o);
    }

    public Q where(Predicate... o) {
        return queryMixin.where(o);
    }

    public Q limit(long limit) {
        return queryMixin.limit(limit);
    }

    public Q offset(long offset) {
        return queryMixin.offset(offset);
    }

    public Q restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    public <P> Q set(ParamExpression<P> param, P value) {
        return queryMixin.set(param, value);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof QueryBase) {
            QueryBase q = (QueryBase)o;
            return q.queryMixin.equals(queryMixin);
        } else {
            return false;
        }
    }
  
    @Override
    public int hashCode() {
        return queryMixin.hashCode();
    }
        
    @Override
    public String toString() {
        return queryMixin.toString();
    }

}
