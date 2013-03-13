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
package com.mysema.query.jpa.impl;

import javax.inject.Provider;
import javax.persistence.EntityManager;

import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.JPQLQueryFactory;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.types.EntityPath;

/**
 * Factory class for query and DML clause creation
 *
 * @author tiwe
 *
 */
public class JPAQueryFactory implements JPQLQueryFactory  {

    private final JPQLTemplates templates;

    private final Provider<EntityManager> entityManager;

    public JPAQueryFactory(Provider<EntityManager> entityManager) {
        this(JPQLTemplates.DEFAULT, entityManager);
    }

    public JPAQueryFactory(JPQLTemplates templates, Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
        this.templates = templates;
    }

    @Override
    public JPADeleteClause delete(EntityPath<?> path) {
        return new JPADeleteClause(entityManager.get(), path, templates);
    }

    @Override
    public JPAQuery from(EntityPath<?> from) {
        return query().from(from);
    }

    @Override
    public JPAUpdateClause update(EntityPath<?> path) {
        return new JPAUpdateClause(entityManager.get(), path, templates);
    }

    @Override
    public JPAQuery query() {
        return new JPAQuery(entityManager.get(), templates);
    }

    @Override
    public JPASubQuery subQuery() {
        return new JPASubQuery();
    }

}
