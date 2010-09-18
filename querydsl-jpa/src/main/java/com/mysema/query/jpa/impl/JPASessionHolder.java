/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.impl;

import javax.persistence.Query;

/**
 * @author tiwe
 *
 */
public interface JPASessionHolder {

    /**
     * Create a JPQL query for the given query string
     * 
     * @param queryString
     * @return
     */
    Query createQuery(String queryString);

    /**
     * Create an SQL query for the given query string
     * 
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
