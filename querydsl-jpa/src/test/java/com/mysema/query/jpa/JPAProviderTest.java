package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Test;

import com.mysema.query.jpa.impl.JPAProvider;

public class JPAProviderTest {

    private EntityManagerFactory factory;
    
    private EntityManager em;
    
    @After
    public void tearDown() {
        if (em != null) {
            em.close();    
        }
        if (factory != null) {
            factory.close();    
        }        
    }
    
    @Test
    public void Hibernate() {
        factory = Persistence.createEntityManagerFactory("h2");
        em = factory.createEntityManager();
        System.out.println(em.getDelegate().getClass());
//        println(em.getEntityManagerFactory().getProperties());
        assertEquals(JPAProvider.HIBERNATE, JPAProvider.get(em));
    }
    
    @Test
    public void Hibernate_For_Proxy() {
        factory = Persistence.createEntityManagerFactory("h2");
        em = factory.createEntityManager();
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(em, args);
            }            
        };
        EntityManager proxy = (EntityManager) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(), 
                new Class[]{EntityManager.class}, 
                handler);
        assertEquals(JPAProvider.HIBERNATE, JPAProvider.get(proxy));
    }
    
    @Test
    public void EclipseLink() {
        factory = Persistence.createEntityManagerFactory("h2-eclipselink");
        em = factory.createEntityManager();
        System.out.println(em.getDelegate().getClass());
//        println(em.getEntityManagerFactory().getProperties());
        assertEquals(JPAProvider.ECLIPSELINK, JPAProvider.get(em));
    }
    
    @Test
    public void EclipseLink_For_Proxy() {
        factory = Persistence.createEntityManagerFactory("h2-eclipselink");
        em = factory.createEntityManager();
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(em, args);
            }            
        };
        EntityManager proxy = (EntityManager) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(), 
                new Class[]{EntityManager.class}, 
                handler);
        assertEquals(JPAProvider.ECLIPSELINK, JPAProvider.get(proxy));
    }
        
    @Test
    public void OpenJPA() {
        factory = Persistence.createEntityManagerFactory("derby-openjpa");
        em = factory.createEntityManager();
        System.out.println(em.getDelegate().getClass());
//        println(em.getEntityManagerFactory().getProperties());
        assertEquals(JPAProvider.OPEN_JPA, JPAProvider.get(em));
    }
    
//    private void println(Map<String, Object> properties) {
//        for (Map.Entry<String, Object> entry : properties.entrySet()) {
//            System.out.println(entry.getKey() + " = " + entry.getValue());
//        }
//        System.out.println();
//    }
    
}
