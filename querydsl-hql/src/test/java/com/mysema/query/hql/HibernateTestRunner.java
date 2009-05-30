/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.RunNotifier;

/**
 * HibernateTestRunner provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HibernateTestRunner extends JUnit4ClassRunner {

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
        try {
            super.invokeTestMethod(method, notifier);
        } finally {
            session.getTransaction().rollback();
        }
    }

    public void run(final RunNotifier notifier) {
        try {
            AnnotationConfiguration cfg = new AnnotationConfiguration();

            // TODO : make this configurable
            for (Class<?> cl : Domain.class.getDeclaredClasses()) {
                cfg.addAnnotatedClass(cl);
            }

            Hibernate config = getTestClass().getJavaClass().getAnnotation(Hibernate.class);
            cfg.setNamingStrategy(config.namingStrategy().newInstance());
            Properties props = new Properties();
            InputStream is = IntegrationTest.class.getResourceAsStream(config
                    .properties());
            if (is == null)
                throw new IllegalArgumentException(
                        "No configuration available at classpath:" + config.properties());
            props.load(is);
            cfg.setProperties(props);
            sessionFactory = cfg.buildSessionFactory();
            super.run(notifier);
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        } finally {
            if (sessionFactory != null)
                sessionFactory.close();
        }

    }

}
