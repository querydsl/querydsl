package com.mysema.query.sql;

import java.sql.Connection;

import javax.inject.Provider;

import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLMergeClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Expression;

/**
 * @author tiwe
 *
 */
public abstract class AbstractSQLQueryFactory<Q extends SQLCommonQuery<?>> implements SQLQueryFactory<Q, SQLSubQuery, 
    SQLDeleteClause, SQLUpdateClause, SQLInsertClause, SQLMergeClause>{
    
    protected final Configuration configuration;

    protected final Provider<Connection> connection;

    public AbstractSQLQueryFactory(Configuration configuration, Provider<Connection> connection) {
        this.configuration = configuration;
        this.connection = connection;
    }

    public final SQLDeleteClause delete(RelationalPath<?> path) {
        return new SQLDeleteClause(connection.get(), configuration, path);
    }

    public final Q from(Expression<?> from) {
        return (Q) query().from(from);
    }

    public final SQLInsertClause insert(RelationalPath<?> path) {
        return new SQLInsertClause(connection.get(), configuration, path);
    }

    public final SQLMergeClause merge(RelationalPath<?> path) {
        return new SQLMergeClause(connection.get(), configuration, path);
    }

    public final SQLUpdateClause update(RelationalPath<?> path) {
        return new SQLUpdateClause(connection.get(), configuration, path);
    }

    public final SQLSubQuery subQuery() {
        return new SQLSubQuery();
    }

    public final SQLSubQuery subQuery(Expression<?> from) {
        return subQuery().from(from);
    }

    public final Configuration getConfiguration() {
        return configuration;
    }

    public final Connection getConnection() {
        return connection.get();
    }

}
