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
package com.querydsl.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.path.ComparablePath;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.SimplePath;

/**
 * @author tiwe
 *
 */
public class CustomFinder {
    
    public static <T> List<T> findCustom(EntityManager em, Class<T> entityClass,Map<String,?> filters, String sort) {
        EntityPath<T> entityPath = new EntityPathBase<T>(entityClass, "entity");
        BooleanBuilder builder = new BooleanBuilder();
        for (Map.Entry<String, ?> entry : filters.entrySet()) {
            SimplePath<Object> property = new SimplePath<Object>(entry.getValue().getClass(), entityPath, entry.getKey()); 
            builder.and(property.eq(entry.getValue()));
        }
        ComparablePath<?> sortProperty = new ComparablePath(Comparable.class, entityPath, sort);
        return new JPAQuery(em).from(entityPath).where(builder.getValue()).orderBy(sortProperty.asc()).list(entityPath);
    }

}
