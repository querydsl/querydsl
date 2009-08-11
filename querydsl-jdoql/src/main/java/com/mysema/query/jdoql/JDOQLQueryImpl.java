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

    public JDOQLQueryImpl() {
    }
    
    public JDOQLQueryImpl(PersistenceManager pm) {
        super(pm);
    }

    public JDOQLQueryImpl(PersistenceManager pm, JDOQLPatterns patterns) {
        super(pm);
    }

}
