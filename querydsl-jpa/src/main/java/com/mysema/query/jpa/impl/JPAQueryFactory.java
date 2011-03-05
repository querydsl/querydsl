package com.mysema.query.jpa.impl;

import javax.inject.Provider;
import javax.persistence.EntityManager;

import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.types.EntityPath;

/**
 * Factory class for query and DML clause creation
 * 
 * @author tiwe
 *
 */
public class JPAQueryFactory {
    
    private final JPQLTemplates templates;
    
    private final Provider<EntityManager> entityManager;
    
    public JPAQueryFactory(Provider<EntityManager> entityManager) {
        this(HQLTemplates.DEFAULT, entityManager);
    }
    
    public JPAQueryFactory(JPQLTemplates templates, Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
        this.templates = templates;
    }
    
    public JPADeleteClause delete(EntityPath<?> path) {
        return new JPADeleteClause(entityManager.get(), path, templates);
    }
    
    public JPAQuery from(EntityPath<?> from) {
        return query().from(from);
    }

    public JPAUpdateClause update(EntityPath<?> path) {
        return new JPAUpdateClause(entityManager.get(), path, templates);
    }

    public JPAQuery query(){
        return new JPAQuery(entityManager.get(), templates);    
    }
}
