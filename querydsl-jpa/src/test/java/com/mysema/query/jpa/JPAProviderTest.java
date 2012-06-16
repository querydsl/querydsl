package com.mysema.query.jpa;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.mysema.query.jpa.impl.JPAProvider;

public class JPAProviderTest {

    @Test
    public void Hibernate() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager em = factory.createEntityManager();
        try {
            assertEquals(JPAProvider.HIBERNATE, JPAProvider.get(em));
        } finally {
            em.close();
            factory.close();
        }
    }
    
    @Test
    public void EclipseLink() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2-eclipselink");
        EntityManager em = factory.createEntityManager();
        try {
            assertEquals(JPAProvider.ECLIPSELINK, JPAProvider.get(em));
        } finally {
            em.close();
            factory.close();
        }
    }
    
    @Test
    public void OpenJPA() {
        
    }
    
}
