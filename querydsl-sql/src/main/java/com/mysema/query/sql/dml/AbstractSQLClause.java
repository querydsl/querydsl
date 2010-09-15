/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.dml;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mysema.query.sql.Configuration;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.Param;

/**
 * AbstractSQLClause is a superclass for SQL based DMLClause implementations
 * 
 * @author tiwe
 *
 */
public class AbstractSQLClause {
    
    protected final Configuration configuration;
    
    public AbstractSQLClause(Configuration configuration) {
        this.configuration = configuration;
    }
    
    protected void setParameters(PreparedStatement stmt, List<?> objects, List<Path<?>> constantPaths, Map<Param<?>, ?> params){
        if (objects.size() != constantPaths.size()){
            throw new IllegalArgumentException("Expected " + objects.size() + " paths, but got " + constantPaths.size());
        }
        int counter = 1;
        for (int i = 0; i < objects.size(); i++){
            Object o = objects.get(i);
            try {
                if (Param.class.isInstance(o)){
                    if (!params.containsKey(o)){
                        throw new ParamNotSetException((Param<?>) o);
                    }
                    o = params.get(o);
                }
                counter += configuration.set(stmt, constantPaths.get(i), counter, o);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

}
