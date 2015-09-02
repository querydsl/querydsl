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
package com.querydsl.sql;

import java.util.List;
import java.util.Map;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.dml.SQLMergeBatch;
import com.querydsl.sql.dml.SQLUpdateBatch;

/**
 * {@code SQLBaseListener} is a base implementation of the {@link SQLDetailedListener} interface
 * with empty method implementations
 */
public class SQLBaseListener implements SQLDetailedListener {

    @Override
    public void start(SQLListenerContext context) {

    }

    @Override
    public void preRender(SQLListenerContext context) {

    }

    @Override
    public void rendered(SQLListenerContext context) {

    }

    @Override
    public void prePrepare(SQLListenerContext context) {

    }

    @Override
    public void prepared(SQLListenerContext context) {

    }

    @Override
    public void preExecute(SQLListenerContext context) {

    }

    @Override
    public void executed(SQLListenerContext context) {

    }

    @Override
    public void exception(SQLListenerContext context) {

    }

    @Override
    public void end(SQLListenerContext context) {

    }

    @Override
    public void notifyQuery(QueryMetadata md) {

    }

    @Override
    public void notifyDelete(RelationalPath<?> entity, QueryMetadata md) {

    }

    @Override
    public void notifyDeletes(RelationalPath<?> entity, List<QueryMetadata> batches) {

    }

    @Override
    public void notifyMerge(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> keys, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {

    }

    @Override
    public void notifyMerges(RelationalPath<?> entity, QueryMetadata md, List<SQLMergeBatch> batches) {

    }

    @Override
    public void notifyInsert(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {

    }

    @Override
    public void notifyInserts(RelationalPath<?> entity, QueryMetadata md, List<SQLInsertBatch> batches) {

    }

    @Override
    public void notifyUpdate(RelationalPath<?> entity, QueryMetadata md, Map<Path<?>, Expression<?>> updates) {

    }

    @Override
    public void notifyUpdates(RelationalPath<?> entity, List<SQLUpdateBatch> batches) {

    }
}
