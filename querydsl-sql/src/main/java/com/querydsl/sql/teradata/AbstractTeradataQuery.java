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
package com.querydsl.sql.teradata;

import java.sql.Connection;

import javax.inject.Provider;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.TeradataTemplates;

/**
 * {@code AbstractTeradataQuery} provides Teradata related extensions to SQLQuery
 *
 * @param <T> result type
 * @param <C> the concrete subtype.
 *
 * @author tiwe
 */
public abstract class AbstractTeradataQuery<T, C extends AbstractTeradataQuery<T, C>> extends AbstractSQLQuery<T, C> {
    public AbstractTeradataQuery(Connection conn) {
        this(conn, new Configuration(TeradataTemplates.DEFAULT), new DefaultQueryMetadata());
    }

    public AbstractTeradataQuery(Connection conn, SQLTemplates templates) {
        this(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    public AbstractTeradataQuery(Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    public AbstractTeradataQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public AbstractTeradataQuery(Provider<Connection> connProvider, Configuration configuration, QueryMetadata metadata) {
        super(connProvider, configuration, metadata);
    }

    public AbstractTeradataQuery(Provider<Connection> connProvider, Configuration configuration) {
        super(connProvider, configuration);
    }

}
