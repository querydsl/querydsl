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
import com.querydsl.r2dbc.SQLTemplates;
import com.querydsl.sql.RelationalPath;
import io.r2dbc.spi.Connection;

/**
 * {@code SQLDeleteClause} defines a DELETE clause.
 * If you need to subtype this, use {@link AbstractR2DBCDeleteClause} instead.
 *
 * @author mc_fish
 */
public class R2DBCDeleteClause extends AbstractR2DBCDeleteClause<R2DBCDeleteClause> {
    public R2DBCDeleteClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        super(connection, new Configuration(templates), entity);
    }

    public R2DBCDeleteClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(connection, configuration, entity);
    }

    public R2DBCDeleteClause(R2DBCConnectionProvider connection, Configuration configuration, RelationalPath<?> entity) {
        super(connection, configuration, entity);
    }
}
