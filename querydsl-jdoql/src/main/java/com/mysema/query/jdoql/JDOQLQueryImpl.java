/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import javax.jdo.PersistenceManager;


/**
 * Default implementation of the JDOQLQuery interface
 * 
 * @author tiwe
 * 
 * @param <A>
 */
public class JDOQLQueryImpl extends AbstractJDOQLQuery<JDOQLQueryImpl> implements JDOQLQuery{

    /**
     * Create a new JDOQLQueryImpl instance
     * 
     * @param pm PersistenceManager instance to use
     * @param templates JDOQLTemplates to use
     * @param detach detached results or not
     */
    public JDOQLQueryImpl(PersistenceManager pm, JDOQLTemplates templates, boolean detach) {
        super(pm, templates, false);
    }
    
    /**
     * Create a new JDOQLQueryImpl instance
     * 
     * @param pm PersistenceManager instance to use
     * @param detach detached results or not
     */
    public JDOQLQueryImpl(PersistenceManager pm, boolean detach) {
        super(pm, JDOQLTemplates.DEFAULT, detach);
    }
    
    /**
     * Create a new JDOQLQueryImpl instance
     * 
     * @param pm PersistenceManager instance to use
     */
    public JDOQLQueryImpl(PersistenceManager pm) {
        super(pm, JDOQLTemplates.DEFAULT, false);
    }
}
