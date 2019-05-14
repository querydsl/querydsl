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
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.types.FactoryExpression;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.Query;
import java.util.Iterator;

/**
 * {@code HibernateHandler} is the {@link QueryHandler} implementation for Hibernate
 *
 * @author tiwe
 */
class HibernateHandler implements QueryHandler {

    @Override
    public void addEntity(Query query, String alias, Class<?> type) {
        if (query instanceof NativeQuery) {
            ((NativeQuery) query).addEntity(alias, type);
        }
    }


    @Override
    public void addScalar(Query query, String alias, Class<?> type) {
        if (query instanceof NativeQuery) {
            ((NativeQuery) query).addScalar(alias);
        }
    }

    @Override
    public boolean transform(Query query, FactoryExpression<?> projection) {
        ResultTransformer transformer = new FactoryExpressionTransformer(projection);

        if (query instanceof org.hibernate.query.Query) {
            ((org.hibernate.query.Query) query).setResultTransformer(transformer);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean wrapEntityProjections() {
        return true;
    }

    @Override
    public boolean createNativeQueryTyped() {
        return false;
    }

    @Override
    public <T> CloseableIterator<T> iterate(Query query, FactoryExpression<?> projection) {
        if (query instanceof NativeQuery) {
            NativeQuery nativeQuery = (NativeQuery) query;
            ScrollableResults results = nativeQuery.scroll(ScrollMode.FORWARD_ONLY);
            CloseableIterator<T> iterator = new ScrollableResultsIterator<T>(results);
            if (projection != null) {
                iterator = new TransformingIterator<T>(iterator, projection);
            }
            return iterator;
        } else {
            Iterator<T> iterator = query.getResultList().iterator();
            if (projection != null) {
                return new TransformingIterator<T>(iterator, projection);
            } else {
                return new IteratorAdapter<T>(iterator);
            }
        }
    }
}
