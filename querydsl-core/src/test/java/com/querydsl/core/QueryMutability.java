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
package com.querydsl.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.querydsl.core.types.Expression;

public final class QueryMutability {

    private final FetchableQuery<?,?> query;

    public QueryMutability(FetchableQuery<?,?> query) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        this.query = query;
    }

    public void test(Expression<?> p1, Expression<?> p2) throws IOException {
        System.err.println("fetchCount");
        query.select(p1).fetchCount();

        System.err.println("countDistinct");
        query.select(p1).distinct().fetchCount();

        System.err.println("iterate");
        query.select(p1).iterate();
        query.select(p1, p2).iterate();

        System.err.println("iterateDistinct");
        query.select(p1).distinct().iterate();
        query.select(p1, p2).distinct().iterate();

        System.err.println("list");
        query.select(p1).fetch();
        query.select(p1, p2).fetch();

        System.err.println("distinct fetch");
        query.select(p1).distinct().fetch();
        query.select(p2).distinct().fetch();

        System.err.println("fetchResults");
        query.select(p1).fetchResults();

        System.err.println("distinct fetchResults");
        query.select(p1).distinct().fetchResults();

        System.err.println("fetchOne");
        query.select(p1).fetchOne();
        query.select(p1,p2).fetchOne();
    }

}
