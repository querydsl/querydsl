/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import javax.persistence.EntityManager;

import com.mysema.query.hql.HQLPatterns;

/**
 * JpaqlQuery provides a fluent statically typed interface for creating JPAQL
 * queries.
 * 
 * @author tiwe
 * @version $Id$
 */
public class JPAQLQuery extends AbstractJPAQLQuery<JPAQLQuery> {

    public JPAQLQuery(EntityManager em) {
        super(em);
    }

    public JPAQLQuery(EntityManager em, HQLPatterns ops) {
        super(em, ops);
    }

}
