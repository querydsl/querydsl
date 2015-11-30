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

import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.jpa.JPAExpressions.selectOne;

import java.util.List;

import org.junit.Test;

import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.hibernate.HibernateQuery;

public class JoinTest {

    public interface Entity {

        List<String> getNames();
    }

    private final Entity alias = Alias.alias(Entity.class);

    private final StringPath path = Expressions.stringPath("path");
    private final JPQLQuery<?> subQuery = selectOne();
    private final HibernateQuery<?> query = new HibernateQuery<Void>(new DummySessionHolder(), HQLTemplates.DEFAULT);

    @Test
    public void subQuery_innerJoin() {
        subQuery.from($(alias));
        subQuery.innerJoin($(alias.getNames()), path);
        // TODO : assertions
    }

    @Test
    public void subQuery_join() {
        subQuery.from($(alias));
        subQuery.join($(alias.getNames()), path);
        // TODO : assertions
    }

    @Test
    public void subQuery_leftJoin() {
        subQuery.from($(alias));
        subQuery.leftJoin($(alias.getNames()), path);
        // TODO : assertions
    }

    @Test
    public void query_innerJoin() {
        query.from($(alias));
        query.innerJoin($(alias.getNames()), path);
        // TODO : assertions
    }

    @Test
    public void query_join() {
        query.from($(alias));
        query.join($(alias.getNames()), path);
        // TODO : assertions
    }

    @Test
    public void query_leftJoin() {
        query.from($(alias));
        query.leftJoin($(alias.getNames()), path);
        // TODO : assertions
    }

}
