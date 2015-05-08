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
package com.mysema.query.jpa.impl;

import java.util.Map;

import javax.persistence.EntityManager;

import com.google.common.collect.Maps;
import com.mysema.query.jpa.BatooTemplates;
import com.mysema.query.jpa.DataNucleusTemplates;
import com.mysema.query.jpa.EclipseLinkTemplates;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.jpa.OpenJPATemplates;

/**
 * JPAProvider provides detection of the JPA provider based on the EntityManager instance
 *
 * @author tiwe
 *
 */
public final class JPAProvider {

    private static final Map<Class<?>, JPQLTemplates> mappings = Maps.newHashMap();

    private static final Map<String, JPQLTemplates> templatesByName = Maps.newHashMap();

    private static void addMapping(String className, JPQLTemplates templates) {
        try {
            mappings.put(Class.forName(className), templates);
        } catch (Exception e) {}
    }

    static {
        addMapping("org.batoo.jpa.core.impl.manager.EntityManagerImpl", BatooTemplates.DEFAULT);
        addMapping("org.hibernate.Session", HQLTemplates.DEFAULT);
        addMapping("org.hibernate.ejb.HibernateEntityManager", HQLTemplates.DEFAULT);
        addMapping("org.eclipse.persistence.jpa.JpaEntityManager", EclipseLinkTemplates.DEFAULT);
        addMapping("org.apache.openjpa.persistence.OpenJPAEntityManager", OpenJPATemplates.DEFAULT);
        addMapping("org.datanucleus.jpa.EntityManagerImpl", DataNucleusTemplates.DEFAULT);
        addMapping("org.datanucleus.ObjectManager", DataNucleusTemplates.DEFAULT);
        addMapping("org.datanucleus.ObjectManagerImpl", DataNucleusTemplates.DEFAULT);

        templatesByName.put("batoo", BatooTemplates.DEFAULT);
        templatesByName.put("eclipselink", EclipseLinkTemplates.DEFAULT);
        templatesByName.put("hibernate", HQLTemplates.DEFAULT);
        templatesByName.put("openjpa", OpenJPATemplates.DEFAULT);
        templatesByName.put("datanucleus", DataNucleusTemplates.DEFAULT);
    }

    public static JPQLTemplates getTemplates(EntityManager em) {
        // detect by delegate
        for (Map.Entry<Class<?>, JPQLTemplates> entry : mappings.entrySet()) {
            if (entry.getKey().isAssignableFrom(em.getDelegate().getClass())) {
                return entry.getValue();
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

    private JPAProvider() {}

}
