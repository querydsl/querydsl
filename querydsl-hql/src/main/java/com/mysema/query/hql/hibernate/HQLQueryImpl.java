/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import org.hibernate.Session;

import com.mysema.query.hql.HQLQuery;
import com.mysema.query.hql.HQLTemplates;


/**
 * 
 * @author tiwe
 *
 */
public class HQLQueryImpl extends AbstractHQLQuery<HQLQueryImpl> implements HQLQuery{

    public HQLQueryImpl(Session session, HQLTemplates patterns) {
        super(session, patterns);
    }

}
