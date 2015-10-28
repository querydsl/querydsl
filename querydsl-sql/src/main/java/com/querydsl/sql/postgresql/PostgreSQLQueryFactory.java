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
package com.querydsl.sql.postgresql;

import java.sql.Connection;

import javax.inject.Provider;

import com.querydsl.sql.AbstractSQLQueryFactory;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLTemplates;

/**
 * PostgreSQL specific implementation of SQLQueryFactory
 *
 * @author tiwe
 *
 */
public class PostgreSQLQueryFactory extends AbstractSQLQueryFactory<PostgreSQLQuery<?>> {

    public PostgreSQLQueryFactory(Configuration configuration, Provider<Connection> connection) {
        super(configuration, connection);
    }

    public PostgreSQLQueryFactory(Provider<Connection> connection) {
        this(new Configuration(new PostgreSQLTemplates()), connection);
    }

    public PostgreSQLQueryFactory(SQLTemplates templates, Provider<Connection> connection) {
        this(new Configuration(templates), connection);
    }

    @Override
    public PostgreSQLQuery<?> query() {
        return new PostgreSQLQuery<Void>(connection, configuration);
    }


}
