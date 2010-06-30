/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql;

import java.io.Closeable;

import javax.jdo.PersistenceManager;

import com.mysema.query.Projectable;

/**
 * Query interface for JDOQL queries
 *
 * @author tiwe
 *
 */
public interface JDOQLQuery extends JDOQLCommonQuery<JDOQLQuery>, Projectable, Closeable {

    /**
     * Clone the state of the query for the given PersistenceManager
     *
     * @param persistenceManager
     * @return
     */
    JDOQLQuery clone(PersistenceManager persistenceManager);

}
