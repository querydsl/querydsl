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
package com.querydsl.collections;

import java.util.Collection;

import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;

/**
 * CollDeleteClause is an implementation of the {@link DeleteClause} interface for the Querydsl 
 * Collections module
 *
 * @author tiwe
 *
 * @param <T>
 */
public class CollDeleteClause<T> implements DeleteClause<CollDeleteClause<T>> {

    private final Collection<? extends T> col;

    private final Path<T> expr;

    private final CollQuery query;

    public CollDeleteClause(QueryEngine qe, Path<T> expr, Collection<? extends T> col) {
        this.query = new CollQuery(qe).from(expr, col);
        this.expr = expr;
        this.col = col;
    }

    public CollDeleteClause(Path<T> expr, Collection<? extends T> col) {
        this(DefaultQueryEngine.getDefault(), expr, col);
    }

    @Override
    public long execute() {
        int rv = 0;
        for (T match : query.list(expr)) {
            col.remove(match);
            rv++;
        }
        return rv;
    }
    
    @Override
    public CollDeleteClause<T> where(Predicate... o) {
        query.where(o);
        return this;
    }

    @Override
    public String toString() {
        return "delete " + query.toString();
    }
}
