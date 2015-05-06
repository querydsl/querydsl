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
package com.mysema.query.sql.postgres;

import java.sql.Connection;

import javax.inject.Provider;

import com.mysema.query.sql.AbstractSQLQueryFactory;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.PostgresTemplates;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.SQLTemplates;

/**
 * PostgreSQL specific implementation of SQLQueryFactory
 *
 * @author tiwe
 *
 */
public class PostgresQueryFactory extends AbstractSQLQueryFactory<PostgresQuery, SQLSubQuery> {

    public PostgresQueryFactory(Configuration configuration, Provider<Connection> connection) {
        super(configuration, connection);
    }

    public PostgresQueryFactory(Provider<Connection> connection) {
        this(new Configuration(new PostgresTemplates()), connection);
    }

    public PostgresQueryFactory(SQLTemplates templates, Provider<Connection> connection) {
        this(new Configuration(templates), connection);
    }

    public PostgresQuery query() {
        return new PostgresQuery(connection.get(), configuration);
    }


}
