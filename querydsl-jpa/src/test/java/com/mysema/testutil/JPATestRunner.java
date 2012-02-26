/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.testutil;

import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    
    private boolean isDerby;

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
                        try {
                            Method method = target.getClass().getMethod("setEntityManager", EntityManager.class); 
                            method.invoke(target, entityManager);
                            entityManager.getTransaction().begin();
                            base.evaluate();
                        } finally {
                            try {
                                if (entityManager.getTransaction().isActive()) {
                                    entityManager.getTransaction().rollback();    
                                }                                
                                entityManager.clear();
                            } finally {
                                entityManager.close();    
                            }                                         
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
            isDerby = config.value().contains("derby");
            if (isDerby) {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            }
            entityManagerFactory = Persistence.createEntityManagerFactory(config.value());
            super.run(notifier);
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        } finally {
            shutdown();
        }

    }
    
    private void shutdown() {
        entityManagerFactory.getCache().evictAll();
        
        if (entityManagerFactory != null){
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
        
        // clean shutdown of derby
        if (isDerby) {
            try {                    
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException e) {
                if (!e.getMessage().equals("Derby system shutdown.")) {
                    throw new RuntimeException(e);    
                }                    
            } 
        }
    }
}
