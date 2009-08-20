/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import javax.persistence.EntityManager;

import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.HQLQuery;

/**
 * 
 * 
 * @author tiwe
 *
 */
public class JPAQueryImpl extends AbstractJPAQuery<JPAQueryImpl> implements HQLQuery{

    public JPAQueryImpl(EntityManager em, HQLTemplates patterns) {
        super(em, patterns);
    }

}
