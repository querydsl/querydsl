/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.hibernate.Session;

import com.mysema.query.grammar.HqlOps;

/**
 * HqlQuery provides a fluent statically typed interface for creating HQL queries.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HqlQuery extends AbstractHqlQuery<HqlQuery>{

    public HqlQuery(Session session) {
        super(session);
    }
    
    public HqlQuery(Session session, HqlOps ops) {
        super(session,ops);
    }

    



}
