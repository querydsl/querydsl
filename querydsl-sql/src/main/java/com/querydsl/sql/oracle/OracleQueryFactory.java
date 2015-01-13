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
package com.querydsl.sql.oracle;

import java.sql.Connection;

import javax.inject.Provider;

import com.querydsl.sql.AbstractSQLQueryFactory;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.SQLSubQuery;
import com.querydsl.sql.SQLTemplates;

/**
 * Oracle specific implementation of SQLQueryFactory
 *
 * @author tiwe
 *
 */
public class OracleQueryFactory extends AbstractSQLQueryFactory<OracleQuery, SQLSubQuery> {

    public OracleQueryFactory(Configuration configuration, Provider<Connection> connection) {
        super(configuration, connection);
    }

    public OracleQueryFactory(Provider<Connection> connection) {
        this(new Configuration(new OracleTemplates()), connection);
    }

    public OracleQueryFactory(SQLTemplates templates, Provider<Connection> connection) {
        this(new Configuration(templates), connection);
    }

    public OracleQuery query() {
        return new OracleQuery(connection.get(), configuration);
    }

}
