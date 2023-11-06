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

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.types.Null;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code SQLMergeClause} defines a case action for MERGE INTO clause with USING
 *
 * @author borgiiri
 */
public class SQLMergeUsingCase {

    private final SQLMergeUsingClause parentClause;
    private final Boolean matched;
    private final List<Predicate> matchAnds = new ArrayList<>();
    private Map<Path<?>, Expression<?>> updates = new LinkedHashMap<>();
    private MergeOperation mergeOperation;

    public enum MergeOperation { UPDATE, INSERT, DELETE }

    public SQLMergeUsingCase(SQLMergeUsingClause parentClause, Boolean matched) {
        this.parentClause = parentClause;
        this.matched = matched;
    }

    public SQLMergeUsingCase and(Predicate predicate) {
        matchAnds.add(predicate);
        return this;
    }

    public SQLMergeUsingClause thenInsert(List<? extends Path<?>> paths, List<?> values) {
        mergeOperation = MergeOperation.INSERT;
        set(paths, values);
        return parentClause.addWhen(this);
    }

    public SQLMergeUsingClause thenUpdate(List<? extends Path<?>> paths, List<?> values) {
        mergeOperation = MergeOperation.UPDATE;
        set(paths, values);
        return parentClause.addWhen(this);
    }

    public SQLMergeUsingClause thenDelete() {
        mergeOperation = MergeOperation.DELETE;
        return parentClause.addWhen(this);
    }

    private void set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++) {
            if (values.get(i) instanceof Expression) {
                updates.put(paths.get(i), (Expression<?>) values.get(i));
            } else if (values.get(i) != null) {
                updates.put(paths.get(i), ConstantImpl.create(values.get(i)));
            } else {
                updates.put(paths.get(i), Null.CONSTANT);
            }
        }
    }

    public Boolean getMatched() {
        return matched;
    }

    public List<Predicate> getMatchAnds() {
        return matchAnds;
    }

    public MergeOperation getMergeOperation() {
        return mergeOperation;
    }

    public Map<Path<?>, Expression<?>> getUpdates() {
        return updates;
    }
}