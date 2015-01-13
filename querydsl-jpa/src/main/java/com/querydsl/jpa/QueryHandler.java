/*
 * Copyright 2013, Mysema Ltd
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

import javax.annotation.Nullable;
import javax.persistence.Query;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.FactoryExpression;

/**
 * QueryHandler provides injection of provider specific functionality into the querydsl logic
 *
 * @author tiwe
 *
 */
public interface QueryHandler {

    /**
     * @return
     */
    boolean createNativeQueryTyped();

    /**
     * Iterate the results with the optional projection
     *
     * @param query
     * @return
     */
    <T> CloseableIterator<T> iterate(Query query, @Nullable FactoryExpression<?> projection);

    /**
     * Add the given scalar to the given native querydsl
     *
     * @param query
     * @param alias
     * @param type
     */
    void addScalar(Query query, String alias, Class<?> type);

    /**
     * Add the given entity to the given native querydsl
     *
     * @param query
     * @param alias
     * @param type
     */
    void addEntity(Query query, String alias, Class<?> type);

    /**
     * Transform the results of the given querydsl using the given factory expression
     *
     * @param query
     * @param projection
     * @return
     */
    boolean transform(Query query, FactoryExpression<?> projection);

    /**
     * @return
     */
    boolean wrapEntityProjections();

}
