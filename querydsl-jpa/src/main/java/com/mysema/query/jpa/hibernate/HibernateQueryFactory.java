package com.mysema.query.jpa.hibernate;

import javax.inject.Provider;

import org.hibernate.Session;

import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.types.EntityPath;

/**
 * Factory class for query and DML clause creation
 *
 * @author tiwe
 *
 */
public class HibernateQueryFactory {

    private final JPQLTemplates templates;

    private final Provider<Session> session;

    public HibernateQueryFactory(Provider<Session> session) {
        this(HQLTemplates.DEFAULT, session);
    }

    public HibernateQueryFactory(JPQLTemplates templates, Provider<Session> session) {
        this.session = session;
        this.templates = templates;
    }

    public HibernateDeleteClause delete(EntityPath<?> path) {
        return new HibernateDeleteClause(session.get(), path, templates);
    }

    public HibernateQuery from(EntityPath<?> from) {
        return query().from(from);
    }

    public HibernateUpdateClause update(EntityPath<?> path) {
        return new HibernateUpdateClause(session.get(), path, templates);
    }

    public HibernateQuery query(){
        return new HibernateQuery(session.get(), templates);
    }

    public HibernateSubQuery subQuery(){
        return new HibernateSubQuery();
    }
}
