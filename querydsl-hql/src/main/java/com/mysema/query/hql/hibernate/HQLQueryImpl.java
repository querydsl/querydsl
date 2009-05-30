/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import org.hibernate.Session;

import com.mysema.query.hql.HQLPatterns;
import com.mysema.query.hql.HQLQuery;


/**
 * 
 * @author tiwe
 *
 */
public class HQLQueryImpl extends AbstractHQLQuery<HQLQueryImpl> implements HQLQuery{

    public HQLQueryImpl(Session session) {
        super(session);
    }

    public HQLQueryImpl(Session session, HQLPatterns patterns) {
        super(session, patterns);
    }

}
