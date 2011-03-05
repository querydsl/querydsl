package com.mysema.query.jdo;

import javax.inject.Provider;
import javax.jdo.PersistenceManager;

import com.mysema.query.jdo.dml.JDOQLDeleteClause;
import com.mysema.query.types.EntityPath;

/**
 * Factory class for query and DML clause creation
 * 
 * @author tiwe
 *
 */
public class JDOQueryFactory {
    
    private final Provider<PersistenceManager> persistenceManager;
    
    public JDOQueryFactory(Provider<PersistenceManager> persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
    
    public JDOQLDeleteClause delete(EntityPath<?> path) {
        return new JDOQLDeleteClause(persistenceManager.get(), path);
    }

    public JDOQLQuery from(EntityPath<?> from) {
        return query().from(from);
    }
    
    public JDOQLQuery query(){
        return new JDOQLQueryImpl(persistenceManager.get());    
    }
    
}