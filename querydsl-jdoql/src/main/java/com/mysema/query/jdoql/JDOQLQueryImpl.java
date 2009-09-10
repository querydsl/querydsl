/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import javax.jdo.PersistenceManager;

import com.mysema.query.DefaultQueryMetadata;


/**
 * Default implementation of the JDOQLQuery interface
 * 
 * @author tiwe
 * 
 * @param <A>
 */
public class JDOQLQueryImpl extends AbstractJDOQLQuery<JDOQLQueryImpl> implements JDOQLQuery{

    private static final JDOQLTemplates DEFAULT_TEMPLATES = new JDOQLTemplates();
    
    public JDOQLQueryImpl(PersistenceManager pm, JDOQLTemplates templates) {
        super(new DefaultQueryMetadata(), pm, templates);
    }

    public JDOQLQueryImpl(PersistenceManager pm) {
        super(new DefaultQueryMetadata(), pm, DEFAULT_TEMPLATES);
    }
}
