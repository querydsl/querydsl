package com.mysema.query.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.RunNotifier;

import com.mysema.query.grammar.hql.HqlDomain;
import com.mysema.query.grammar.hql.HqlIntegrationTest;

/**
 * HibernateTestRunner provides
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
            // TODO : make this configurable
            cfg.setNamingStrategy(new CustomNamingStrategy());
            Properties props = new Properties();
            // TODO : make this configurable
            props.load(HqlIntegrationTest.class.getResourceAsStream("default.properties"));
            cfg.setProperties(props);
            sessionFactory = cfg.buildSessionFactory();    
            super.run(notifier);            
        } catch (IOException e) {
                String error = "Caught " + e.getClass().getName();
                throw new RuntimeException(error, e);
        }finally{
            if (sessionFactory != null) sessionFactory.close();
        }
           
    }
    
}
