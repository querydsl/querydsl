/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import javax.persistence.Query;

/**
 * NoSessionHolder is a session holder for detached JPAQuery usage
 * 
 * @author tiwe
 *
 */
public class NoSessionHolder implements JPASessionHolder{

    @Override
    public Query createQuery(String queryString) {
        throw new UnsupportedOperationException("No entityManager in detached Query available");
    }

}
