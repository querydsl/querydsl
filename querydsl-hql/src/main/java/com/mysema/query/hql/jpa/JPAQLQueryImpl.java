/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import javax.persistence.EntityManager;

import com.mysema.query.hql.HQLPatterns;
import com.mysema.query.hql.HQLQuery;

/**
 * 
 * 
 * @author tiwe
 *
 */
public class JPAQLQueryImpl extends AbstractJPAQLQuery<JPAQLQueryImpl> implements HQLQuery{

    public JPAQLQueryImpl(EntityManager em) {
        super(em, HQLPatterns.DEFAULT);
    }

    public JPAQLQueryImpl(EntityManager em, HQLPatterns patterns) {
        super(em, patterns);
    }

}
