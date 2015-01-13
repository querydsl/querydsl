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
package com.querydsl.sql;

import java.util.List;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.Projectable;
import com.querydsl.core.Query;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

/**
 * Default implementation of the Union interface
 * 
 * @author tiwe
 *
 * @param <Q>
 * @param <RT>
 */
public class UnionImpl<Q extends Query & Projectable, RT>  implements Union<RT> {
    
    private final Q query;
    
    private final Expression<?>[] projection;
        
    public UnionImpl(Q query, List<? extends Expression<?>> projection) {
        this.query = query;
        this.projection = projection.toArray(new Expression[projection.size()]);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<RT> list() {
        if (projection.length == 1) {
            return (List<RT>) query.list(projection[0]);
        } else {
            return (List<RT>) query.list(projection);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public CloseableIterator<RT> iterate() {
        if (projection.length == 1) {
            return (CloseableIterator<RT>) query.iterate(projection[0]);
        } else {
            return (CloseableIterator<RT>) query.iterate(projection);
        }
    }

    @Override
    public Union<RT> groupBy(Expression<?>... o) {
        query.groupBy(o);
        return this;
    }

    @Override
    public Union<RT> having(Predicate... o) {
        query.having(o);
        return this;
    }

    
    @SuppressWarnings("unchecked")
    @Override
    public Union<RT> orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return this;
    }

    @Override
    public String toString() {
        return query.toString();
    }

}
