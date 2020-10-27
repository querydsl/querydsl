/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.jpa.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import com.querydsl.jpa.BatooTemplates;
import com.querydsl.jpa.DataNucleusTemplates;
import com.querydsl.jpa.EclipseLinkTemplates;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.Hibernate5Templates;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.OpenJPATemplates;

/**
 * {@code JPAProvider} provides detection of the JPA provider based on the EntityManager instance
 *
 * @author tiwe
 *
 */
public final class JPAProvider {

    private static final Map<Class<?>, JPQLTemplates> mappings = new HashMap<>();

    private static final Map<String, JPQLTemplates> templatesByName = new HashMap<>();

    private static void addMapping(String className, JPQLTemplates templates) {
        try {
            mappings.put(Class.forName(className), templates);
        } catch (Exception e) { }
    }

    static {
        boolean hibernate5;

        try {
            String version = Class.forName("org.hibernate.Session").getPackage().getImplementationVersion();
            String[] versionParts = version.split("\\.");
            int major = Integer.parseInt(versionParts[0]);
            hibernate5 = major >= 5;
        } catch (ClassNotFoundException e) {
            hibernate5 = false;
        }

        JPQLTemplates hibernateTemplates = hibernate5 ? Hibernate5Templates.DEFAULT : HQLTemplates.DEFAULT;

        addMapping("org.batoo.jpa.core.impl.manager.EntityManagerImpl", BatooTemplates.DEFAULT);
        addMapping("org.hibernate.Session", hibernateTemplates);
        addMapping("org.hibernate.ejb.HibernateEntityManager", hibernateTemplates);
        addMapping("org.hibernate.jpa.HibernateEntityManager", hibernateTemplates);
        addMapping("org.eclipse.persistence.jpa.JpaEntityManager", EclipseLinkTemplates.DEFAULT);
        addMapping("org.apache.openjpa.persistence.OpenJPAEntityManager", OpenJPATemplates.DEFAULT);
        addMapping("org.datanucleus.jpa.EntityManagerImpl", DataNucleusTemplates.DEFAULT);
        addMapping("org.datanucleus.ObjectManager", DataNucleusTemplates.DEFAULT);
        addMapping("org.datanucleus.ObjectManagerImpl", DataNucleusTemplates.DEFAULT);

        templatesByName.put("batoo", BatooTemplates.DEFAULT);
        templatesByName.put("eclipselink", EclipseLinkTemplates.DEFAULT);
        templatesByName.put("hibernate", HQLTemplates.DEFAULT);
        templatesByName.put("hibernate5", Hibernate5Templates.DEFAULT);
        templatesByName.put("openjpa", OpenJPATemplates.DEFAULT);
        templatesByName.put("datanucleus", DataNucleusTemplates.DEFAULT);
    }

    public static JPQLTemplates getTemplates(EntityManager em) {
        for (Map.Entry<Class<?>, JPQLTemplates> entry : mappings.entrySet()) {
            Class<?> entityManagerClass = entry.getKey();
            try {
                if (entityManagerClass.isInstance(em.unwrap(entityManagerClass))) {
                    return entry.getValue();
                }
            } catch (Exception e) { // The PersistenceException is wrapped in an InvocationException for EclipseLink
                // detect by delegate
                if (entityManagerClass.isAssignableFrom(em.getDelegate().getClass())) {
                    return entry.getValue();
                }
            }
        }
        // detect by properties
        for (String key : em.getEntityManagerFactory().getProperties().keySet()) {
            key = key.toLowerCase();
            for (Map.Entry<String, JPQLTemplates> entry : templatesByName.entrySet()) {
                if (key.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return JPQLTemplates.DEFAULT;
    }

    private JPAProvider() { }

}
