/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql;

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
    public JDOQLQueryImpl clone(PersistenceManager persistenceManager){
        return new JDOQLQueryImpl(persistenceManager, getTemplates(), getMetadata().clone(), isDetach());
    }

}
