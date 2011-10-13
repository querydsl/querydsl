package com.mysema.query;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.SimplePath;

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
