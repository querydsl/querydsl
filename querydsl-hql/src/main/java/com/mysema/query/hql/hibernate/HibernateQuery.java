/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import org.hibernate.Session;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.hql.HQLQuery;
import com.mysema.query.hql.HQLTemplates;


/**
 * 
 * @author tiwe
 *
 */
public class HibernateQuery extends AbstractHibernateQuery<HibernateQuery> implements HQLQuery{

    public HibernateQuery(Session session, HQLTemplates patterns) {
        super(new DefaultQueryMetadata(), session, patterns);
    }

}
