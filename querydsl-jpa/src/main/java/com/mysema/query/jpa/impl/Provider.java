package com.mysema.query.jpa.impl;

/**
 * @author tiwe
 *
 */
public enum Provider {

    /**
     * 
     */
    HIBERNATE("org.hibernate.ejb.HibernateEntityManager"),
    
    /**
     * 
     */
    ECLIPSELINK("org.eclipse.persistence.jpa.JpaEntityManager"),
    
    /**
     * 
     */
    OPEN_JPA("org.apache.openjpa.persistence.OpenJPAEntityManager"),
    
    /**
     * 
     */
    GENERIC_JPA("javax.persistence.EntityManager"); 
    
    Provider(String clazz) {
        
    }
}
