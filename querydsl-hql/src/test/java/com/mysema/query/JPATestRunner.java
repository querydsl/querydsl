/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.RunNotifier;

public class JPATestRunner extends JUnit4ClassRunner {

    private EntityManager entityManager;

    private EntityManagerFactory entityManagerFactory;

    public JPATestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    protected Object createTest() throws Exception {
        Object o = getTestClass().getConstructor().newInstance();
        o.getClass().getMethod("setEntityManager", EntityManager.class).invoke(o, entityManager);
        return o;
    }

    protected void invokeTestMethod(Method method, RunNotifier notifier) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            super.invokeTestMethod(method, notifier);
        } finally {
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public void run(final RunNotifier notifier) {
        try {
            JPAConfig config = getTestClass().getJavaClass().getAnnotation(JPAConfig.class);            
            entityManagerFactory = Persistence.createEntityManagerFactory(config.value());
            super.run(notifier);
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        } finally {
            if (entityManagerFactory != null){
                entityManagerFactory.close();
            }                
        }

    }
}
