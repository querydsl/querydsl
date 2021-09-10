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

import java.util.Iterator;
import java.util.stream.Stream;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.querydsl.core.CloseableIterator;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;

import com.querydsl.core.types.FactoryExpression;
import org.jetbrains.annotations.Nullable;

/**
 * {@code HibernateHandler} is the {@link QueryHandler} implementation for Hibernate
 *
 * @author tiwe
 *
 */
public class HibernateHandler implements QueryHandler {

    @Override
    public void addEntity(Query query, String alias, Class<?> type) {
        query.unwrap(NativeQuery.class).addEntity(alias, type);
    }

    @Override
    public void addScalar(Query query, String alias, Class<?> type) {
        query.unwrap(NativeQuery.class).addScalar(alias);
    }

    @Override
    public boolean createNativeQueryTyped() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CloseableIterator<T> iterate(Query query, FactoryExpression<?> projection) {
        try {
            org.hibernate.query.Query<?> unwrappedQuery = query.unwrap(org.hibernate.query.Query.class);
            ScrollableResults results = unwrappedQuery.scroll(ScrollMode.FORWARD_ONLY);
            CloseableIterator<T> iterator = new ScrollableResultsIterator<T>(results);
            if (projection != null) {
                iterator = new TransformingIterator<T>(iterator, projection);
            }
            return iterator;
        } catch (PersistenceException e) {
            Iterator<T> iterator = query.getResultList().iterator();
            if (projection != null) {
                return new TransformingIterator<T>(iterator, projection);
            } else {
                return CloseableIterator.fromIterator(iterator);
            }
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> Stream<T> stream(Query query, @Nullable FactoryExpression<?> projection) {
        final Stream resultStream = query.getResultStream();
        if (projection != null) {
            return resultStream.map(element -> projection.newInstance((Object[]) (element.getClass().isArray() ? element : new Object[] {element})));
        }
        return resultStream;
    }

    @Override
    public boolean transform(Query query, FactoryExpression<?> projection) {
        try {
            ResultTransformer transformer = new FactoryExpressionTransformer(projection);
            query.unwrap(org.hibernate.query.Query.class).setResultTransformer(transformer);
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    @Override
    public boolean wrapEntityProjections() {
        return true;
    }

}
