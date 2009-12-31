/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import org.hibernate.Query;

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

}
