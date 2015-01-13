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
package com.querydsl.jpa.sql;

import javax.persistence.EntityManager;

import com.querydsl.core.QueryMetadata;
import com.querydsl.jpa.QueryHandler;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;

/**
 * JPASQLQuery is an SQLQuery implementation that uses JPA Native SQL functionality
 * to execute queries
 *
 * @author tiwe
 *
 */
public class JPASQLQuery extends AbstractJPASQLQuery<JPASQLQuery> {

    public JPASQLQuery(EntityManager entityManager, SQLTemplates sqlTemplates) {
        super(entityManager, new Configuration(sqlTemplates));
    }

    public JPASQLQuery(EntityManager entityManager, Configuration conf) {
        super(entityManager, conf);
    }

    public JPASQLQuery(EntityManager entityManager, Configuration conf, QueryHandler queryHandler) {
        super(entityManager, conf, queryHandler);
    }

    public JPASQLQuery(EntityManager entityManager, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(entityManager, new Configuration(sqlTemplates), metadata);
    }

    public JPASQLQuery(EntityManager entityManager, Configuration conf, QueryMetadata metadata) {
        super(entityManager, conf, metadata);
    }

    public JPASQLQuery(EntityManager entityManager, Configuration conf, QueryHandler queryHandler, QueryMetadata metadata) {
        super(entityManager, conf, queryHandler, metadata);
    }

    @Override
    public JPASQLQuery clone(EntityManager entityManager) {
        JPASQLQuery q = new JPASQLQuery(entityManager, configuration, queryHandler, getMetadata().clone());
        q.clone(this);
        return q;
    }

}
