package com.mysema.query.sql;

import java.sql.Connection;

import javax.inject.Provider;

import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Expression;

/**
 * @author tiwe
 *
 */
public class SQLQueryFactory {
    
    private final Configuration configuration;
    
    private final Provider<Connection> connection;
    
    public SQLQueryFactory(SQLTemplates templates, Provider<Connection> connection) {
        this(new Configuration(templates), connection);
    }
    
    public SQLQueryFactory(Configuration configuration, Provider<Connection> connection) {
        this.configuration = configuration;
        this.connection = connection;
    }
    
    public SQLDeleteClause delete(RelationalPath<?> path) {
        return new SQLDeleteClause(connection.get(), configuration, path);
    }

    public SQLQuery query(){
        return new SQLQueryImpl(connection.get(), configuration);    
    }
    
    public SQLQuery from(Expression<?> from) {
        return query().from(from);
    }

    public SQLInsertClause insert(RelationalPath<?> path) {
        return new SQLInsertClause(connection.get(), configuration, path);
    }

    public SQLUpdateClause update(RelationalPath<?> path) {
        return new SQLUpdateClause(connection.get(), configuration, path);
    }
    
}
