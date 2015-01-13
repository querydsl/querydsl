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
import java.io.Closeable;

import com.querydsl.core.Projectable;

/**
 * Query interface for JDOQL queries
 *
 * @author tiwe
 *
 */
public interface JDOQLQuery extends JDOCommonQuery<JDOQLQuery>, Projectable, Closeable {

    /**
     * Clone the state of the querydsl for the given PersistenceManager
     *
     * @param persistenceManager
     * @return
     */
    JDOQLQuery clone(PersistenceManager persistenceManager);

    /**
     * Add the fetch group to the set of active fetch groups.
     * 
     * @param fetchGroupName
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
     * Close the querydsl and related resources
     */
    @Override
    void close();

}
