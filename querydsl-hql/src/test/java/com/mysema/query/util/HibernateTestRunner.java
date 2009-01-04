/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.util;

import java.lang.reflect.Method;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.RunNotifier;

import com.mysema.query.hql.HqlDomain;
import com.mysema.query.hql.HqlIntegrationTest;

/**
 * HibernateTestRunner provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HibernateTestRunner extends JUnit4ClassRunner{
    
    private Session session;
    
    private SessionFactory sessionFactory;
    
    public HibernateTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }
    
    protected Object createTest() throws Exception {
        Object o = getTestClass().getConstructor().newInstance();
        o.getClass().getMethod("setSession", Session.class).invoke(o, session);        
        return o;
    }
    
    protected void invokeTestMethod(Method method, RunNotifier notifier) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        try{
            super.invokeTestMethod(method, notifier);    
        }finally{
            session.getTransaction().rollback();
        }        
    }
    
    public void run(final RunNotifier notifier) {
        try{
            AnnotationConfiguration cfg = new AnnotationConfiguration();
            
            // TODO : make this configurable
            for (Class<?> cl : HqlDomain.class.getDeclaredClasses()){
                cfg.addAnnotatedClass(cl);
            }
            
            Hibernate config = getTestClass().getJavaClass().getAnnotation(Hibernate.class);            
            cfg.setNamingStrategy(config.namingStrategy().newInstance());
            Properties props = new Properties();
            // TODO : null check and error message
            props.load(HqlIntegrationTest.class.getResourceAsStream(config.properties()));
            cfg.setProperties(props);
            sessionFactory = cfg.buildSessionFactory();    
            super.run(notifier);            
        } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                throw new RuntimeException(error, e);
        }finally{
            if (sessionFactory != null) sessionFactory.close();
        }
           
    }
    
}
