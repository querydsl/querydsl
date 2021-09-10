/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.jpa;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.core.types.Expression;

class JPASubQuery<T> extends JPAQueryBase<T, JPASubQuery<T>> {

    JPASubQuery() {
        super(new DefaultQueryMetadata(), JPQLTemplates.DEFAULT);
    }

    JPASubQuery(QueryMetadata metadata) {
        super(metadata, JPQLTemplates.DEFAULT);
    }

    @Override
    protected JPQLSerializer createSerializer() {
        return new JPQLSerializer(getTemplates(), null);
    }

    @Override
    protected void reset() {
        // do nothing
    }

    @Override
    public JPASubQuery<T> clone() {
        return new JPASubQuery<T>(getMetadata().clone());
    }

    @Override
    public <U> JPASubQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
        JPASubQuery<U> newType = (JPASubQuery<U>) this;
        return newType;
    }

    @Override
    public JPASubQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
        JPASubQuery<Tuple> newType = (JPASubQuery<Tuple>) this;
        return newType;
    }

    @Override
    public T fetchOne() throws NonUniqueResultException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CloseableIterator<T> iterate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public QueryResults<T> fetchResults() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long fetchCount() {
        throw new UnsupportedOperationException();
    }

}
