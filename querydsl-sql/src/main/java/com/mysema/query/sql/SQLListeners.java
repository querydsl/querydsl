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
package com.mysema.query.sql;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mysema.commons.lang.Pair;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.dml.SQLInsertBatch;
import com.mysema.query.sql.dml.SQLMergeBatch;
import com.mysema.query.sql.dml.SQLUpdateBatch;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;

/**
 * @author tiwe
 *
 */
public class SQLListeners implements SQLListener {

    @Nullable
    private final SQLListener parent;

    private final List<SQLListener> listeners = Lists.newArrayList();

    public SQLListeners(SQLListener parent) {
        this.parent = parent;
    }

    public SQLListeners() {
        this.parent = null;
    }

    public void add(SQLListener listener) {
        listeners.add(listener);
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

}
