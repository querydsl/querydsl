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
package com.querydsl.jdo.sql;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.sql.SQLTemplates;

import javax.annotation.Nullable;
import javax.jdo.PersistenceManager;

/**
 * JDOSQLQuery is an SQLQuery implementation that uses JDO's SQL querydsl functionality
 * to execute queries
 *
 * @author tiwe
 *
 */
public final class JDOSQLQuery extends AbstractSQLQuery<JDOSQLQuery> {

    public JDOSQLQuery(@Nullable PersistenceManager persistenceManager, SQLTemplates templates) {
        this(persistenceManager, new Configuration(templates), new DefaultQueryMetadata().noValidate(), false);
    }

    public JDOSQLQuery(@Nullable PersistenceManager persistenceManager, Configuration configuration) {
        this(persistenceManager, configuration, new DefaultQueryMetadata().noValidate(), false);
    }

    public JDOSQLQuery(
            @Nullable PersistenceManager persistenceManager,
            Configuration configuration,
            QueryMetadata metadata, boolean detach) {
        super(metadata, configuration, persistenceManager, detach);
    }

    @Override
    public JDOSQLQuery clone() {
        JDOSQLQuery query = new JDOSQLQuery(persistenceManager, configuration, getMetadata().clone(), detach);
        query.clone(this);
        return query;
    }

    @Override
    protected SQLSerializer createSerializer() {
        return new SQLSerializer(configuration);
    }
}
