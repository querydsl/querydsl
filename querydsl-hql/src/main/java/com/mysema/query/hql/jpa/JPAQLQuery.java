/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import javax.persistence.EntityManager;

import com.mysema.query.hql.HQLOps;


/**
 * JpaqlQuery provides a fluent statically typed interface for creating JPAQL queries.
 *
 * @author tiwe
 * @version $Id$
 */
public class JPAQLQuery extends AbstractJPAQLQuery<JPAQLQuery>{

    public JPAQLQuery(EntityManager em) {
        super(em);
    }
    
    public JPAQLQuery(EntityManager em, HQLOps ops) {
        super(em, ops);
    }


    

}
