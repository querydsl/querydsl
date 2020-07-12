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
package com.querydsl.r2dbc.dml;

import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.R2DBCConnectionProvider;
import com.querydsl.r2dbc.R2DBCQuery;
import com.querydsl.r2dbc.SQLTemplates;
import com.querydsl.sql.RelationalPath;
import io.r2dbc.spi.Connection;

/**
 * SQLInsertClause defines an INSERT INTO clause
 * If you need to subtype this, use {@link AbstractR2DBCInsertClause} instead.
 *
 * @author mc_fish
 */
public class R2DBCInsertClause extends AbstractR2DBCInsertClause<R2DBCInsertClause> {
    public R2DBCInsertClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }

    public R2DBCInsertClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity, R2DBCQuery<?> subQuery) {
        this(connection, new Configuration(templates), entity, subQuery);
    }

    public R2DBCInsertClause(Connection connection, Configuration configuration, RelationalPath<?> entity, R2DBCQuery<?> subQuery) {
        super(connection, configuration, entity, subQuery);
    }

    public R2DBCInsertClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(connection, configuration, entity);
    }

    public R2DBCInsertClause(R2DBCConnectionProvider connection, Configuration configuration, RelationalPath<?> entity, R2DBCQuery<?> subQuery) {
        super(connection, configuration, entity, subQuery);
    }

    public R2DBCInsertClause(R2DBCConnectionProvider connection, Configuration configuration, RelationalPath<?> entity) {
        super(connection, configuration, entity);
    }

}
