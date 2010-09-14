/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo;

import javax.jdo.PersistenceManager;

import com.mysema.query.Projectable;

/**
 * Query interface for JDOQL queries
 *
 * @author tiwe
 *
 */
public interface JDOQLQuery extends JDOQLCommonQuery<JDOQLQuery>, Projectable {

    /**
     * Clone the state of the query for the given PersistenceManager
     *
     * @param persistenceManager
     * @return
     */
    JDOQLQuery clone(PersistenceManager persistenceManager);

    /**
     * Add the fetch group to the set of active fetch groups.
     * 
     * @param string
     * @return
     */
    JDOQLQuery addFetchGroup(String fetchGroupName);

    /**
     * Set the maximum fetch depth when fetching. 
     * A value of 0 has no meaning and will throw a JDOUserException.
     * A value of -1 means that no limit is placed on fetching.
     * A positive integer will result in that number of references from the
     * initial object to be fetched.
     * 
     * @param maxFetchDepth
     * @return
     */
    JDOQLQuery setMaxFetchDepth(int maxFetchDepth);
    
    /**
     * 
     */
    void close();

}
