/*
 * Copyright 2013, Mysema Ltd
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
public interface SQLListener {

    /**
     * @param md
     */
    void notifyQuery(QueryMetadata md);

    /**
     * @param md
     * @param entity
     */
    void notifyDelete(QueryMetadata md, RelationalPath<?> entity);

    /**
     * @param md
     * @param entity
     * @param batches
     */
    void notifyDeletes(QueryMetadata md, RelationalPath<?> entity,
            List<QueryMetadata> batches);

    /**
     * @param md
     * @param entity
     * @param keys
     * @param columns
     * @param values
     * @param subQuery
     */
    void notifyMerge(QueryMetadata md, RelationalPath<?> entity, List<Path<?>> keys,
            List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery);

    /**
     * @param md
     * @param entity
     * @param batches
     */
    void notifyMerges(QueryMetadata md, RelationalPath<?> entity, List<SQLMergeBatch> batches);

    /**
     * @param md
     * @param entity
     * @param columns
     * @param values
     * @param subQuery
     */
    void notifyInsert(QueryMetadata md, RelationalPath<?> entity, List<Path<?>> columns,
            List<Expression<?>> values, SubQueryExpression<?> subQuery);

    /**
     * @param md
     * @param entity
     * @param batches
     */
    void notifyInserts(QueryMetadata md, RelationalPath<?> entity, List<SQLInsertBatch> batches);

    /**
     * @param md
     * @param entity
     * @param updates
     */
    void notifyUpdate(QueryMetadata md, RelationalPath<?> entity,
            List<Pair<Path<?>, Expression<?>>> updates);

    /**
     * @param md
     * @param entity
     * @param batches
     */
    void notifyUpdates(QueryMetadata md, RelationalPath<?> entity, List<SQLUpdateBatch> batches);

}