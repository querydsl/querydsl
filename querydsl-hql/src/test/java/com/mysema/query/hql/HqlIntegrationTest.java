/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.mysema.query.util.CustomNamingStrategy;
import com.mysema.query.util.Hibernate;
import com.mysema.query.util.HibernateTestRunner;


/**
 * HibernatePersistenceTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
@RunWith(HibernateTestRunner.class)
@Hibernate(namingStrategy=CustomNamingStrategy.class, properties="default.properties")
public class HqlIntegrationTest extends HqlParserTest{
    
    private Session session;
        
    @Override
    protected void parse() throws RecognitionException, TokenStreamException{
        System.out.println("query : " + toString().replace('\n', ' '));

        // create Query and execute it
        Query query = session.createQuery(toString());
        HqlQuery.setConstants(query, getConstants());
        try{
            query.list();    
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }        
    }
    
    @Test
    public void testGroupBy() throws Exception {
        // do nothing
    }

    @Test
    public void testOrderBy() throws Exception {
        // do nothing
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
}
