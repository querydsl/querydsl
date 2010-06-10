/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * Abstraction for different Hibernate Session signatures
 * 
 * @author tiwe
 *
 */
public interface SessionHolder {

    /**
     * @param queryString
     * @return
     */
    Query createQuery(String queryString);
    
    /**
     * @param queryString
     * @return
     */
    SQLQuery createSQLQuery(String queryString);

}
