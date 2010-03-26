/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.search;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractQueryTest {
    
    private static SessionFactory sessionFactory;
    
    @BeforeClass
    public static void setUpClass() throws IOException{
        FileUtils.deleteDirectory(new File("target/derbydb"));
        FileUtils.deleteDirectory(new File("target/lucene"));
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(Resume.class);
        Properties props = new Properties();
        InputStream is = SearchQueryTest.class.getResourceAsStream("/derby.properties");  
        props.load(is);
        cfg.setProperties(props);
        sessionFactory = cfg.buildSessionFactory();
    }
    
    @AfterClass
    public static void tearDownClass(){
        sessionFactory.close();
    }
    
    private Session session;
    
    protected Session getSession(){
        return session;
    }
    
    @Before
    public void setUp(){        
        session = sessionFactory.openSession();
        session.beginTransaction();
    }
    
    @After
    public void tearDown() throws HibernateException, SQLException{
        if (!session.getTransaction().wasRolledBack()){
            session.getTransaction().commit();    
        }        
        session.close();
    }

}
