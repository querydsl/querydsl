/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.dml;

import javax.jdo.PersistenceManager;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.jdoql.JDOQLTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;

/**
 * @author tiwe
 *
 */
public class JDOQLUpdateClause implements UpdateClause<JDOQLUpdateClause>{
    
    private static final JDOQLTemplates DEFAULT_TEMPLATES = new JDOQLTemplates();
    
    private final QueryMetadata md = new DefaultQueryMetadata();
    
    private final PersistenceManager pm;
    
    private final JDOQLTemplates templates;
    
    public JDOQLUpdateClause(PersistenceManager pm, PEntity<?> entity){
        this(pm, entity, DEFAULT_TEMPLATES);
    }
    
    public JDOQLUpdateClause(PersistenceManager pm, PEntity<?> entity, JDOQLTemplates templates){
        this.pm = pm;
        this.templates = templates;
        md.addFrom(entity);        
    }

    @Override
    public long execute() {
        // TODO : implement
        throw new RuntimeException("Not yet implemented");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> JDOQLUpdateClause set(Path<T> path, T value) {
        md.addProjection(((Expr<T>)path).eq(value));
        return this;
    }

    @Override
    public JDOQLUpdateClause where(EBoolean... o) {
        md.addWhere(o);
        return this;
    }

}
