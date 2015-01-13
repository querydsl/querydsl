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
package com.querydsl.sql;

import java.util.List;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.dml.SQLMergeBatch;
import com.querydsl.sql.dml.SQLUpdateBatch;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;

/**
 * Listener interface for SQL queries and clauses
 *
 * @author tiwe
 *
 */
public interface SQLListener {

    /**
     * Notify about a querydsl
     *
     * @param md metadata of the querydsl
     */
    void notifyQuery(QueryMetadata md);

    /**
     * Notify about a deletion
     *
     * @param entity table to be deleted from
     * @param md metadata of deletion
     */
    void notifyDelete(RelationalPath<?> entity, QueryMetadata md);

    /**
     * Notify about a batch deletion
     *
     * @param entity table to be deleted from
     * @param batches metadata of batches
     */
    void notifyDeletes(RelationalPath<?> entity, List<QueryMetadata> batches);

    /**
     * Notify about a merge
     *
     * @param entity table to be merged
     * @param md metadata of merge
     * @param keys key columns
     * @param columns columns to be updated/inserted
     * @param values values
     * @param subQuery optional sub querydsl
     */
    void notifyMerge(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> keys,
            List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery);

    /**
     * Notify about a batch merge
     *
     * @param entity table to be merged
     * @param md metadata of merge
     * @param batches metadata of batches
     */
    void notifyMerges(RelationalPath<?> entity, QueryMetadata md, List<SQLMergeBatch> batches);

    /**
     * Notify about an insertion
     *
     * @param entity table to be inserted into
     * @param md metadata of insertion
     * @param columns columns to be inserted into
     * @param values values to be inserted into
     * @param subQuery optional sub querydsl
     */
    void notifyInsert(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> columns,
            List<Expression<?>> values, SubQueryExpression<?> subQuery);

    /**
     * Notify about a batch insertion
     *
     * @param entity table to be inserted into
     * @param md metadata of insertion
     * @param batches metadata of batches
     */
    void notifyInserts(RelationalPath<?> entity, QueryMetadata md, List<SQLInsertBatch> batches);

    /**
     * Notify about an update operation
     *
     * @param entity table to be updated
     * @param md metadata of update
     * @param updates metadata of batches
     */
    void notifyUpdate(RelationalPath<?> entity, QueryMetadata md,
            List<Pair<Path<?>, Expression<?>>> updates);

    /**
     * Notify about a batch update
     *
     * @param entity table to be updated
     * @param batches metadata of batches
     */
    void notifyUpdates(RelationalPath<?> entity, List<SQLUpdateBatch> batches);

}