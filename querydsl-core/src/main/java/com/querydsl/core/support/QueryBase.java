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
package com.querydsl.core.support;

import javax.annotation.Nonnegative;

import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Predicate;

/**
 * QueryBase provides a stub for Query implementations
 *
 * @author tiwe
 */
public abstract class QueryBase<Q extends QueryBase<Q>> {

    public static final String MDC_QUERY = "querydsl.querydsl";

    public static final String MDC_PARAMETERS = "querydsl.parameters";

    protected final QueryMixin<Q> queryMixin;

    public QueryBase(QueryMixin<Q> queryMixin) {
        this.queryMixin = queryMixin;
    }

    /**
     * Set the Query to return distinct results
     * 
     * @return
     */
    public Q distinct() {
        return queryMixin.distinct();
    }
    
    /**
     * Add a single grouping expression
     *
     * @param e
     * @return
     */
    public Q groupBy(Expression<?> e) {
        return queryMixin.groupBy(e);
    }
    
    /**
     * Add grouping/aggregation expressions
     *
     * @param o
     * @return
     */
    public Q groupBy(Expression<?>... o) {
        return queryMixin.groupBy(o);
    }
    
    /**
     * Add a single filter for aggregation
     *
     * @param e
     * @return
     */
    public Q having(Predicate e) {
        return queryMixin.having(e);
    }

    /**
     * Add filters for aggregation
     *
     * @param o
     * @return
     */
    public Q having(Predicate... o) {
        return queryMixin.having(o);
    }
    
    /**
     * Add a single order expression
     * 
     * @param o
     * @return
     */
    public Q orderBy(OrderSpecifier<?> o) {
        return queryMixin.orderBy(o);
    }

    /**
     * Add order expressions
     *
     * @param o
     * @return
     */
    public Q orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }
    
    /**
     * Add the given filter condition
     * 
     * <p>Skips null arguments</p>
     *
     * @param o filter conditions to be added
     * @return
     */
    public Q where(Predicate o) {
        return queryMixin.where(o);
    }

    /**
     * Add the given filter conditions
     * 
     * <p>Skips null arguments</p>
     *
     * @param o filter conditions to be added
     * @return
     */
    public Q where(Predicate... o) {
        return queryMixin.where(o);
    }

    /**
     * Defines the limit / max results for the querydsl results
     *
     * @param limit
     * @return
     */
    public Q limit(@Nonnegative long limit) {
        return queryMixin.limit(limit);
    }

    /**
     * Defines the offset for the querydsl results
     *
     * @param offset
     * @return
     */
    public Q offset(long offset) {
        return queryMixin.offset(offset);
    }

    /**
     * Defines both limit and offset of the querydsl results
     *
     * @param modifiers
     * @return
     */
    public Q restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    /**
     * Set the given parameter to the given value
     *
     * @param <P>
     * @param param
     * @param value
     * @return
     */
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
