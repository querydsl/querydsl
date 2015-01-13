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
package com.querydsl.sql;

import javax.annotation.Nullable;
import java.util.List;

import com.google.common.collect.Lists;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.dml.SQLMergeBatch;
import com.querydsl.sql.dml.SQLUpdateBatch;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;

/**
 * SQLListeners is an SQLListener implementation which dispatches the
 * notifications to a list of SQLListener instances
 *
 * @author tiwe
 */
public class SQLListeners implements SQLDetailedListener {

    @Nullable
    private final SQLDetailedListener parent;

    private final List<SQLDetailedListener> listeners = Lists.newArrayList();

    public SQLListeners(SQLListener parent) {
        this.parent = new SQLListenerAdapter(parent);
    }

    public SQLListeners() {
        this.parent = null;
    }

    public void add(SQLListener listener) {
        listeners.add(new SQLListenerAdapter(listener));
    }

    @Override
    public void notifyQuery(QueryMetadata md) {
        if (parent != null) {
            parent.notifyQuery(md);
        }
        for (SQLListener listener : listeners) {
            listener.notifyQuery(md);
        }
    }

    @Override
    public void notifyDelete(RelationalPath<?> entity, QueryMetadata md) {
        if (parent != null) {
            parent.notifyDelete(entity, md);
        }
        for (SQLListener listener : listeners) {
            listener.notifyDelete(entity, md);
        }
    }

    @Override
    public void notifyDeletes(RelationalPath<?> entity, List<QueryMetadata> batches) {
        if (parent != null) {
            parent.notifyDeletes(entity, batches);
        }
        for (SQLListener listener : listeners) {
            listener.notifyDeletes(entity, batches);
        }
    }

    @Override
    public void notifyMerge(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> keys,
                            List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {
        if (parent != null) {
            parent.notifyMerge(entity, md, keys, columns, values, subQuery);
        }
        for (SQLListener listener : listeners) {
            listener.notifyMerge(entity, md, keys, columns, values, subQuery);
        }
    }

    @Override
    public void notifyMerges(RelationalPath<?> entity, QueryMetadata md, List<SQLMergeBatch> batches) {
        if (parent != null) {
            parent.notifyMerges(entity, md, batches);
        }
        for (SQLListener listener : listeners) {
            listener.notifyMerges(entity, md, batches);
        }
    }

    @Override
    public void notifyInsert(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> columns,
                             List<Expression<?>> values, SubQueryExpression<?> subQuery) {
        if (parent != null) {
            parent.notifyInsert(entity, md, columns, values, subQuery);
        }
        for (SQLListener listener : listeners) {
            listener.notifyInsert(entity, md, columns, values, subQuery);
        }
    }

    @Override
    public void notifyInserts(RelationalPath<?> entity, QueryMetadata md,
                              List<SQLInsertBatch> batches) {
        if (parent != null) {
            parent.notifyInserts(entity, md, batches);
        }
        for (SQLListener listener : listeners) {
            listener.notifyInserts(entity, md, batches);
        }
    }

    @Override
    public void notifyUpdate(RelationalPath<?> entity, QueryMetadata md,
                             List<Pair<Path<?>, Expression<?>>> updates) {
        if (parent != null) {
            parent.notifyUpdate(entity, md, updates);
        }
        for (SQLListener listener : listeners) {
            listener.notifyUpdate(entity, md, updates);
        }
    }

    @Override
    public void notifyUpdates(RelationalPath<?> entity, List<SQLUpdateBatch> batches) {
        if (parent != null) {
            parent.notifyUpdates(entity, batches);
        }
        for (SQLListener listener : listeners) {
            listener.notifyUpdates(entity, batches);
        }
    }


    @Override
    public void start(final SQLListenerContext context) {
        if (parent != null) {
            parent.start(context);
        }
        for (SQLDetailedListener listener : listeners) {
            listener.start(context);
        }
    }

    @Override
    public void preRender(final SQLListenerContext context) {
        if (parent != null) {
            parent.preRender(context);
        }
        for (SQLDetailedListener listener : listeners) {
            listener.preRender(context);
        }
    }

    @Override
    public void rendered(final SQLListenerContext context) {
        if (parent != null) {
            parent.rendered(context);
        }
        for (SQLDetailedListener listener : listeners) {
            listener.rendered(context);
        }
    }

    @Override
    public void prePrepare(final SQLListenerContext context) {
        if (parent != null) {
            parent.prePrepare(context);
        }
        for (SQLDetailedListener listener : listeners) {
            listener.prePrepare(context);
        }
    }

    @Override
    public void prepared(final SQLListenerContext context) {
        if (parent != null) {
            parent.prepared(context);
        }
        for (SQLDetailedListener listener : listeners) {
            listener.prepared(context);
        }
    }

    @Override
    public void preExecute(final SQLListenerContext context) {
        if (parent != null) {
            parent.preExecute(context);
        }
        for (SQLDetailedListener listener : listeners) {
            listener.preExecute(context);
        }
    }

    @Override
    public void executed(final SQLListenerContext context) {
        if (parent != null) {
            parent.executed(context);
        }
        for (SQLDetailedListener listener : listeners) {
            listener.executed(context);
        }
    }

    @Override
    public void end(final SQLListenerContext context) {
        if (parent != null) {
            parent.end(context);
        }
        for (SQLDetailedListener listener : listeners) {
            listener.end(context);
        }
    }

    @Override
    public void exception(final SQLListenerContext context) {
        if (parent != null) {
            parent.exception(context);
        }
        for (SQLDetailedListener listener : listeners) {
            listener.exception(context);
        }
    }
}
