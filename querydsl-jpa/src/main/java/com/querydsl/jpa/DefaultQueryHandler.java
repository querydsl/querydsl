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

import com.mysema.commons.lang.CloseableIterator;
import org.jetbrains.annotations.Nullable;
import javax.persistence.Query;

import com.querydsl.core.types.FactoryExpression;

/**
 * {@code DefaultQueryHandler} is the default implementation of the {@link QueryHandler} interface
 *
 * @author tiwe
 *
 */
public final class DefaultQueryHandler implements QueryHandler {

    public static final QueryHandler DEFAULT = new DefaultQueryHandler();

    @Override
    public void addEntity(Query query, String alias, Class<?> type) {
        // do nothing
    }

    @Override
    public void addScalar(Query query, String alias, Class<?> type) {
        // do nothing
    }

    @Override
    public boolean createNativeQueryTyped() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CloseableIterator<T> iterate(Query query, @Nullable final FactoryExpression<?> projection) {
        Iterator<T> iterator = query.getResultList().iterator();
        if (projection != null) {
            return new TransformingIterator<T>(iterator, projection);
        } else {
            return CloseableIterator.fromIterator(iterator);
        }
    }

    @Override
    public <T> Stream<T> stream(Query query, @Nullable FactoryExpression<?> projection) {
        final Stream resultStream = query.getResultStream();
        if (projection != null) {
            return resultStream.map(element -> projection.newInstance((Object[]) (element.getClass().isArray() ? element : new Object[] {element})));
        }
        return resultStream;
    }

    @Override
    public boolean transform(Query query, FactoryExpression<?> projection) {
        return false;
    }

    @Override
    public boolean wrapEntityProjections() {
        return false;
    }

    private DefaultQueryHandler() { }


}
