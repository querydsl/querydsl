package com.mysema.query;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.RunNotifier;

import com.mysema.query.hql.domain.Domain;

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

//    public void run(final RunNotifier notifier) {
//        try {
//            AnnotationConfiguration cfg = new AnnotationConfiguration();
//            for (Class<?> cl : Domain.classes){
//                cfg.addAnnotatedClass(cl);
//            }            
//            Hibernate config = getTestClass().getJavaClass().getAnnotation(Hibernate.class);
//            Properties props = new Properties();
//            InputStream is = HibernateTestRunner.class.getResourceAsStream(config.properties());
//            if (is == null){
//                throw new IllegalArgumentException("No configuration available at classpath:" + config.properties());
//            }                
//            props.load(is);
//            cfg.setProperties(props);
//            sessionFactory = cfg.buildSessionFactory();
//            super.run(notifier);
//        } catch (Exception e) {
//            String error = "Caught " + e.getClass().getName();
//            throw new RuntimeException(error, e);
//        } finally {
//            if (sessionFactory != null){
//                sessionFactory.close();
//            }                
//        }
//
//    }
}
