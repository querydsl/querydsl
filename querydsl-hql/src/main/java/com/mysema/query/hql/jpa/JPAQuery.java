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
 * 
 * 
 * @author tiwe
 *
 */
public class JPAQuery extends AbstractJPAQuery<JPAQuery> implements HQLQuery{

    private static final HQLTemplates DEFAULT_TEMPLATES = new HQLTemplates();
    
    public JPAQuery(EntityManager em, HQLTemplates patterns) {
        super(new DefaultQueryMetadata(), em, patterns);
    }

    public JPAQuery(EntityManager em) {
        super(new DefaultQueryMetadata(), em, DEFAULT_TEMPLATES);
    }
}
