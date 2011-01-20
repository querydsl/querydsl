/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.dml;

import java.util.List;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.NullExpr;

/**
 * UpdateClause implementation for JDO
 *
 * @author tiwe
 *
 */
public class JDOQLUpdateClause implements UpdateClause<JDOQLUpdateClause>{

    private final QueryMetadata metadata = new DefaultQueryMetadata();

//    private final PersistenceManager persistenceManager;
//
//    private final JDOQLTemplates templates;

//    public JDOQLUpdateClause(PersistenceManager persistenceManager, PEntity<?> entity){
//        this(persistenceManager, entity, JDOQLTemplates.DEFAULT);
//    }
//
//    public JDOQLUpdateClause(PersistenceManager persistenceManager, PEntity<?> entity, JDOQLTemplates templates){
//        this.persistenceManager = persistenceManager;
//        this.templates = templates;
//        metadata.addFrom(entity);
//    }

    @Override
    public long execute() {
        // TODO : implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressWarnings("unchecked")
    @Override
    public JDOQLUpdateClause set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++){
            if (values.get(i) != null){
                metadata.addProjection(((Expr)paths.get(i).asExpr()).eq(values.get(i)));
            }else{
                metadata.addProjection(((Expr)paths.get(i).asExpr()).eq(new NullExpr(paths.get(i).getType())));
            }
        }
        return this;
    }

    @Override
    public <T> JDOQLUpdateClause set(Path<T> path, T value) {
        if (value != null){
            metadata.addProjection(path.asExpr().eq(value));
        }else{
            metadata.addProjection(path.asExpr().eq(new NullExpr<T>(path.getType())));
        }

        return this;
    }

    @Override
    public JDOQLUpdateClause where(EBoolean... o) {
        metadata.addWhere(o);
        return this;
    }

}
