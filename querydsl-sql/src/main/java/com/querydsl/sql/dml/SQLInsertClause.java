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
package com.querydsl.sql.dml;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

import javax.inject.Provider;
import java.sql.Connection;

/**
 * SQLInsertClause defines an INSERT INTO clause
 *
 * @author tiwe
 *
 */
public class SQLInsertClause extends AbstractSQLInsertClause<SQLInsertClause> {
    public SQLInsertClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }

    public SQLInsertClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity, SQLQuery<?> subQuery) {
        this(connection, new Configuration(templates), entity, subQuery);
    }

    public SQLInsertClause(Connection connection, Configuration configuration, RelationalPath<?> entity, SQLQuery<?> subQuery) {
        super(connection, configuration, entity, subQuery);
    }

    public SQLInsertClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(connection, configuration, entity);
    }

    public SQLInsertClause(Provider<Connection> connection, Configuration configuration, RelationalPath<?> entity, SQLQuery<?> subQuery) {
        super(connection, configuration, entity, subQuery);
    }

    public SQLInsertClause(Provider<Connection> connection, Configuration configuration, RelationalPath<?> entity) {
        super(connection, configuration, entity);
    }
}
