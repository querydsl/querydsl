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

import com.querydsl.core.QueryMetadata;

/**
 * SQLSubQuery is a subquery implementation for SQL queries
 *
 * @author tiwe
 *
 */
public class SQLSubQuery extends AbstractSQLSubQuery<SQLSubQuery> {

    public SQLSubQuery() {
        super();
    }
    
    public SQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }

    public SQLSubQuery(Configuration configuration, QueryMetadata metadata) {
        super(metadata);
    }

    @Override
    public SQLSubQuery clone() {
        SQLSubQuery subQuery = new SQLSubQuery(configuration, getMetadata());
        return subQuery;
    }

}
