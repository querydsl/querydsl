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
package com.querydsl.sql.teradata;

import java.sql.Connection;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLOps;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.TeradataTemplates;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.PredicateOperation;

/**
 * TeradataQuery provides Teradata related extensions to SQLQuery
 *
 * @author tiwe
 *
 */
public class TeradataQuery extends AbstractSQLQuery<TeradataQuery> {

    public TeradataQuery(Connection conn) {
        this(conn, new Configuration(new TeradataTemplates()), new DefaultQueryMetadata());
    }

    public TeradataQuery(Connection conn, SQLTemplates templates) {
        this(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    public TeradataQuery(Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    public TeradataQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    /**
     * Adds a qualify expression
     *
     * @param predicate
     * @return
     */
    public TeradataQuery qualify(Predicate predicate) {
        predicate = PredicateOperation.create(SQLOps.QUALIFY, predicate);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.BEFORE_ORDER, predicate));
    }

    @Override
    public TeradataQuery clone(Connection conn) {
        TeradataQuery q = new TeradataQuery(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

}
