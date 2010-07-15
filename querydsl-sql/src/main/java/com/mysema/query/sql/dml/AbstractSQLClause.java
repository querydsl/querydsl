package com.mysema.query.sql.dml;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.mysema.query.sql.Configuration;
import com.mysema.query.types.Param;
import com.mysema.query.types.ParamNotSetException;

/**
 * @author tiwe
 *
 */
public class AbstractSQLClause {
    
    protected final Configuration configuration;
    
    public AbstractSQLClause(Configuration configuration) {
        this.configuration = configuration;
    }
    
    protected void setParameters(PreparedStatement stmt, Collection<?> objects, Map<Param<?>, ?> params){
        int counter = 1;
        for (Object o : objects) {
            try {
                if (Param.class.isInstance(o)){
                    if (!params.containsKey(o)){
                        throw new ParamNotSetException((Param<?>) o);
                    }
                    o = params.get(o);
                }
                counter += configuration.set(stmt, counter, o);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

}
