/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import javax.persistence.Query;

/**
 * @author tiwe
 *
 */
public interface JPASessionHolder {
    
    /**
     * @param queryString
     * @return
     */
    Query createQuery(String queryString);

    /**
     * @param queryString
     * @return
     */
    Query createSQLQuery(String queryString);
    
    /**
     * @param queryString
     * @param resultClass
     * @return
     */
    Query createSQLQuery(String queryString, Class<?> resultClass);
    
}
