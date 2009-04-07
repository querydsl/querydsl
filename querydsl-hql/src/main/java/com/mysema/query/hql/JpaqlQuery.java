/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import javax.persistence.EntityManager;

import com.mysema.query.grammar.HqlOps;


/**
 * JpaqlQuery provides a fluent statically typed interface for creating JPAQL queries.
 *
 * @author tiwe
 * @version $Id$
 */
public class JpaqlQuery extends AbstractJpaqlQuery<JpaqlQuery>{

    public JpaqlQuery(EntityManager em) {
        super(em);
    }
    
    public JpaqlQuery(EntityManager em, HqlOps ops) {
        super(em, ops);
    }


    

}
