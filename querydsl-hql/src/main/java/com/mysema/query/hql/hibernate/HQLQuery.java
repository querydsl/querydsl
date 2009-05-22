/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import org.hibernate.Session;

import com.mysema.query.hql.HQLOps;


/**
 * HqlQuery provides a fluent statically typed interface for creating HQL queries.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HQLQuery extends AbstractHQLQuery<HQLQuery>{

    public HQLQuery(Session session) {
        super(session);
    }
    
    public HQLQuery(Session session, HQLOps ops) {
        super(session,ops);
    }

    



}
