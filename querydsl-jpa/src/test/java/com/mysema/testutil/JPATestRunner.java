/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.testutil;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.rules.MethodRule;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * @author tiwe
 *
 */
public class JPATestRunner extends BlockJUnit4ClassRunner {

    private EntityManagerFactory entityManagerFactory;

    public JPATestRunner(Class<?> klass) throws InitializationError{
        super(klass);
    }

    @Override
    protected List<MethodRule> rules(Object test) {
        List<MethodRule> rules = super.rules(test);
        rules.add(new MethodRule(){
            @Override
            public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
                return new Statement(){
                    @Override
                    public void evaluate() throws Throwable {
                        EntityManager entityManager = entityManagerFactory.createEntityManager();
                        target.getClass().getMethod("setEntityManager", EntityManager.class).invoke(target, entityManager);
                        entityManager.getTransaction().begin();
                        try {
                            base.evaluate();
                        } finally {
                            entityManager.getTransaction().rollback();
                            entityManager.close();    
                        } 
                    }                    
                };
            }
            
        });
        return rules;
    }
    
    @Override
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
