/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import javax.persistence.EntityManager;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.hql.HQLQuery;
import com.mysema.query.hql.HQLTemplates;

/**
 * JPAQuery is the default implementation of the HQLQuery interface for JPA
 * 
 * @author tiwe
 *
 */
public class JPAQuery extends AbstractJPAQuery<JPAQuery> implements HQLQuery{

    public JPAQuery(EntityManager entityManager, HQLTemplates patterns) {
        super(new DefaultQueryMetadata(), entityManager, patterns);
    }

    public JPAQuery(EntityManager entityManager) {
        super(new DefaultQueryMetadata(), entityManager, HQLTemplates.DEFAULT);
    }
}
