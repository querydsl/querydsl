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
package com.querydsl.jdo;

import javax.jdo.PersistenceManager;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;

/**
 * JDOQuery is the default implementation of the JDOQLQuery interface
 *
 * @author tiwe
 *
 */
public class JDOQuery<T> extends AbstractJDOQuery<T, JDOQuery<T>> {

    /**
     * Create a detached JDOQuery instance
     * The query can be attached via the clone method
     *
     */
    public JDOQuery() {
        super(null, JDOQLTemplates.DEFAULT, new DefaultQueryMetadata(), false);
    }

    /**
     * Create a new JDOQuery instance
     *
     * @param persistenceManager PersistenceManager instance to use
     * @param templates JDOQLTemplates to use
     * @param detach detached results or not
     */
    public JDOQuery(PersistenceManager persistenceManager, JDOQLTemplates templates, boolean detach) {
        super(persistenceManager, templates, new DefaultQueryMetadata(), detach);
    }

    /**
     * Create a new JDOQuery instance
     *
     * @param persistenceManager PersistenceManager instance to use
     * @param detach detached results or not
     */
    public JDOQuery(PersistenceManager persistenceManager, boolean detach) {
        super(persistenceManager, JDOQLTemplates.DEFAULT, new DefaultQueryMetadata(), detach);
    }

    /**
     * Create a new JDOQuery instance
     *
     * @param persistenceManager PersistenceManager instance to use
     */
    public JDOQuery(PersistenceManager persistenceManager) {
        super(persistenceManager, JDOQLTemplates.DEFAULT, new DefaultQueryMetadata(), false);
    }

    /**
     * Create a new JDOQuery instance
     * 
     * @param persistenceManager
     * @param templates
     * @param metadata
     * @param detach
     */
    protected JDOQuery(PersistenceManager persistenceManager, JDOQLTemplates templates, 
            QueryMetadata metadata, boolean detach) {
        super(persistenceManager, templates, metadata, detach);
    }

    /**
     * Clone the state of this query to a new JDOQuery instance with the given PersistenceManager
     *
     * @param persistenceManager
     * @return
     */
    public JDOQuery<T> clone(PersistenceManager persistenceManager) {
        JDOQuery<T> query = new JDOQuery<T>(persistenceManager, getTemplates(),
                getMetadata().clone(), isDetach());
        query.fetchGroups.addAll(fetchGroups);
        query.maxFetchDepth = maxFetchDepth;
        return query;
    }

    @Override
    public <U> JDOQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        return (JDOQuery<U>) this;
    }

    @Override
    public JDOQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        return (JDOQuery<Tuple>) this;
    }

}
