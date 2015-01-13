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

import com.google.common.collect.ImmutableList;
import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.Path;

/**
 * CollQueryFactory provides static convenience methods for querydsl construction
 *
 * @author tiwe
 */
public final class CollQueryFactory {

    /**
     * Create a new delete clause 
     * 
     * @param path source expression
     * @param col source collection
     * @return
     */
    public static <A> CollDeleteClause<A> delete(Path<A> path, Collection<A> col) {
        return new CollDeleteClause<A>(path, col);
    }

    /**
     * Create a new querydsl
     * 
     * @param alias source alias
     * @param col source collection
     * @return
     */
    public static <A> CollQuery from(A alias, Iterable<A> col) {
        return new CollQuery().from(Alias.$(alias), col);
    }

    /**
     * Create a new querydsl
     * 
     * @param path source expression
     * @param arr source array
     * @return
     */
    public static <A> CollQuery from(Path<A> path, A... arr) {
        return new CollQuery().from(path, ImmutableList.copyOf(arr));
    }

    /**
     * Create a new querydsl
     * 
     * @param path source expression
     * @param col source collection
     * @return
     */
    public static <A> CollQuery from(Path<A> path, Iterable<A> col) {
        return new CollQuery().from(path, col);
    }

    /**
     * Create a new update clause
     * 
     * @param path source expression
     * @param col source collection
     * @return
     */
    public static <A> CollUpdateClause<A> update(Path<A> path, Iterable<A> col) {
        return new CollUpdateClause<A>(path, col);
    }

    private CollQueryFactory() {}

}
