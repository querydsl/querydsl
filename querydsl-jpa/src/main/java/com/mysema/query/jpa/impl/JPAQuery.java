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

import javax.persistence.EntityManager;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.jpa.JPQLTemplates;

/**
 * JPAQuery is the default implementation of the JPQLQuery interface for JPA
 *
 * @author tiwe
 *
 */
public final class JPAQuery extends AbstractJPAQuery<JPAQuery> {

    /**
     * Creates a new detached query
     * The query can be attached via the clone method
     */
    public JPAQuery() {
        super(null, JPQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new EntityManager bound query
     *
     * @param em
     */
    public JPAQuery(EntityManager em) {
        super(em, JPAProvider.getTemplates(em), new DefaultQueryMetadata());
    }

    /**
     * Creates a new EntityManager bound query
     *
     * @param em
     */
    public JPAQuery(EntityManager em, QueryMetadata metadata) {
        super(em, JPAProvider.getTemplates(em), metadata);
    }

    /**
     * Creates a new query
     *
     * @param em
     * @param templates
     */
    public JPAQuery(EntityManager em, JPQLTemplates templates) {
        super(em, templates, new DefaultQueryMetadata());
    }

    /**
     * Creates a new query
     *
     * @param em
     * @param templates
     * @param metadata
     */
    public JPAQuery(EntityManager em, JPQLTemplates templates, QueryMetadata metadata) {
        super(em, templates, metadata);
    }

    public JPAQuery clone(EntityManager entityManager) {
        JPAQuery q = new JPAQuery(entityManager, JPAProvider.getTemplates(entityManager), getMetadata().clone());
        q.clone(this);
        return q;
    }

}
