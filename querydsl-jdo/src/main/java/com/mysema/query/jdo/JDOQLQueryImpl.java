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
package com.mysema.query.jdo;

import javax.jdo.PersistenceManager;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;

/**
 * Default implementation of the JDOQLQuery interface
 *
 * @author tiwe
 *
 * @param <A>
 */
public final class JDOQLQueryImpl extends AbstractJDOQLQuery<JDOQLQueryImpl> implements JDOQLQuery{

    /**
     * Create a detached JDOQLQueryImpl instance
     * The query can be attached via the clone method
     *
     * @param persistenceManager
     */
    public JDOQLQueryImpl() {
        super(null, JDOQLTemplates.DEFAULT, new DefaultQueryMetadata(), false);
    }

    /**
     * Create a new JDOQLQueryImpl instance
     *
     * @param persistenceManager PersistenceManager instance to use
     * @param templates JDOQLTemplates to use
     * @param detach detached results or not
     */
    public JDOQLQueryImpl(PersistenceManager persistenceManager, JDOQLTemplates templates, boolean detach) {
        super(persistenceManager, templates, new DefaultQueryMetadata(), detach);
    }

    /**
     * Create a new JDOQLQueryImpl instance
     *
     * @param persistenceManager PersistenceManager instance to use
     * @param detach detached results or not
     */
    public JDOQLQueryImpl(PersistenceManager persistenceManager, boolean detach) {
        super(persistenceManager, JDOQLTemplates.DEFAULT, new DefaultQueryMetadata(), detach);
    }

    /**
     * Create a new JDOQLQueryImpl instance
     *
     * @param persistenceManager PersistenceManager instance to use
     */
    public JDOQLQueryImpl(PersistenceManager persistenceManager) {
        super(persistenceManager, JDOQLTemplates.DEFAULT, new DefaultQueryMetadata(), false);
    }

    /**
     * Create a new JDOQLQueryImpl instance
     * 
     * @param persistenceManager
     * @param templates
     * @param metadata
     * @param detach
     */
    protected JDOQLQueryImpl(PersistenceManager persistenceManager, JDOQLTemplates templates, QueryMetadata metadata, boolean detach) {
        super(persistenceManager, templates, metadata, detach);
    }

    /**
     * Clone the state of this query to a new JDOQLQueryImpl instance with the given PersistenceManager
     *
     * @param persistenceManager
     * @return
     */
    public JDOQLQueryImpl clone(PersistenceManager persistenceManager) {
        JDOQLQueryImpl query = new JDOQLQueryImpl(persistenceManager, getTemplates(), getMetadata().clone(), isDetach());
        query.fetchGroups.addAll(fetchGroups);
        query.maxFetchDepth = maxFetchDepth;
        return query;
    }

}
