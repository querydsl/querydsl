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
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.dml.SQLMergeBatch;
import com.querydsl.sql.dml.SQLMergeUsingCase;
import com.querydsl.sql.dml.SQLUpdateBatch;

/**
 * A simple adapter class that knows if the underlying listener is a simple or detailed SQL listener
 */
class SQLListenerAdapter implements SQLDetailedListener {

    private final SQLListener sqlListener;
    private final SQLDetailedListener detailedListener;

    SQLListenerAdapter(final SQLListener sqlListener) {
        this.detailedListener = sqlListener instanceof SQLDetailedListener ? (SQLDetailedListener) sqlListener : null;
        this.sqlListener = sqlListener;
    }

    public SQLListener getSqlListener() {
        return sqlListener;
    }

    @Override
    public void start(final SQLListenerContext context) {
        if (detailedListener != null) {
            detailedListener.start(context);
        }
    }

    @Override
    public void preRender(final SQLListenerContext context) {
        if (detailedListener != null) {
            detailedListener.preRender(context);
        }
    }

    @Override
    public void rendered(final SQLListenerContext context) {
        if (detailedListener != null) {
            detailedListener.rendered(context);
        }
    }

    @Override
    public void prePrepare(final SQLListenerContext context) {
        if (detailedListener != null) {
            detailedListener.prePrepare(context);
        }
    }

    @Override
    public void prepared(final SQLListenerContext context) {
        if (detailedListener != null) {
            detailedListener.prepared(context);
        }
    }

    @Override
    public void preExecute(final SQLListenerContext context) {
        if (detailedListener != null) {
            detailedListener.preExecute(context);
        }
    }

    @Override
    public void executed(final SQLListenerContext context) {
        if (detailedListener != null) {
            detailedListener.executed(context);
        }
    }

    @Override
    public void end(final SQLListenerContext context) {
        if (detailedListener != null) {
            detailedListener.end(context);
        }
    }

    @Override
    public void exception(final SQLListenerContext context) {
        if (detailedListener != null) {
            detailedListener.exception(context);
        }
    }

    @Override
    public void notifyQuery(final QueryMetadata md) {
        sqlListener.notifyQuery(md);
    }

    @Override
    public void notifyDelete(final RelationalPath<?> entity, final QueryMetadata md) {
        sqlListener.notifyDelete(entity, md);
    }

    @Override
    public void notifyDeletes(final RelationalPath<?> entity, final List<QueryMetadata> batches) {
        sqlListener.notifyDeletes(entity, batches);
    }

    @Override
    public void notifyMerge(final RelationalPath<?> entity, final QueryMetadata md, final List<Path<?>> keys, final List<Path<?>> columns, final List<Expression<?>> values, final SubQueryExpression<?> subQuery) {
        sqlListener.notifyMerge(entity, md, keys, columns, values, subQuery);
    }

    @Override
    public void notifyMerges(final RelationalPath<?> entity, final QueryMetadata md, final List<SQLMergeBatch> batches) {
        sqlListener.notifyMerges(entity, md, batches);
    }

    @Override
    public void notifyMergeUsing(RelationalPath<?> entity, QueryMetadata md, SimpleExpression<?> usingExpression, Predicate usingOn, List<SQLMergeUsingCase> whens) {
        sqlListener.notifyMergeUsing(entity, md, usingExpression, usingOn, whens);
    }

    @Override
    public void notifyInsert(final RelationalPath<?> entity, final QueryMetadata md, final List<Path<?>> columns, final List<Expression<?>> values, final SubQueryExpression<?> subQuery) {
        sqlListener.notifyInsert(entity, md, columns, values, subQuery);
    }

    @Override
    public void notifyInserts(final RelationalPath<?> entity, final QueryMetadata md, final List<SQLInsertBatch> batches) {
        sqlListener.notifyInserts(entity, md, batches);
    }

    @Override
    public void notifyUpdate(final RelationalPath<?> entity, final QueryMetadata md, final Map<Path<?>, Expression<?>> updates) {
        sqlListener.notifyUpdate(entity, md, updates);
    }

    @Override
    public void notifyUpdates(final RelationalPath<?> entity, final List<SQLUpdateBatch> batches) {
        sqlListener.notifyUpdates(entity, batches);
    }
}
