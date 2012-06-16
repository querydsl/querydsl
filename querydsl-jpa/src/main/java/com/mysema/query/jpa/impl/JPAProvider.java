/*
 * Copyright 2012, Mysema Ltd
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
package com.mysema.query.jpa.impl;

import javax.persistence.EntityManager;

import com.mysema.query.jpa.EclipseLinkTemplates;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.jpa.OpenJPATemplates;

/**
 * JPAProvider provides an enumeration of supported JPA providers
 * 
 * @author tiwe
 *
 */
public enum JPAProvider {

    /**
     * Hibernate
     */
    HIBERNATE("org.hibernate.ejb.HibernateEntityManager", HQLTemplates.DEFAULT),
    
    /**
     * EclipseLink
     */
    ECLIPSELINK("org.eclipse.persistence.jpa.JpaEntityManager", EclipseLinkTemplates.DEFAULT),
    
    /**
     * OpenJPA
     */
    OPEN_JPA("org.apache.openjpa.persistence.OpenJPAEntityManager", OpenJPATemplates.DEFAULT),
    
    /**
     * Generic JPA provider
     */
    GENERIC("javax.persistence.EntityManager", JPQLTemplates.DEFAULT); 
    
    private Class<?> emClass;
    
    private final JPQLTemplates templates;
    
    /**
     * @param emClassName class name of EntityManager implementation
     * @param templates
     */
    JPAProvider(String emClassName, JPQLTemplates templates) {
        this.templates = templates;
        try {            
            this.emClass = Class.forName(emClassName);            
        } catch (ClassNotFoundException e) {}
    }
    
    public JPQLTemplates getTemplates() {
        return templates;
    }

    public static JPAProvider get(EntityManager em) {
        for (JPAProvider provider : values()) {
            if (provider.emClass != null && provider.emClass.isAssignableFrom(em.getClass())) {
                return provider;
            }
        }
        throw new IllegalStateException("No Provider for " + em.getClass().getName());
    }
    
    public static JPQLTemplates getTemplates(EntityManager em) {
        return get(em).getTemplates();
    }
}
