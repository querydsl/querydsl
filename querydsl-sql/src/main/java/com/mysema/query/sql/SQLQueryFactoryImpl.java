package com.mysema.query.sql;

import java.sql.Connection;

import javax.inject.Provider;

import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLMergeClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Expression;

/**
 * Factory class for query and DML clause creation
 *
 * @author tiwe
 *
 */
public class SQLQueryFactoryImpl implements SQLQueryFactory<SQLQueryImpl, SQLSubQuery, SQLDeleteClause, SQLUpdateClause, SQLInsertClause, SQLMergeClause>{

    private final Configuration configuration;

    private final Provider<Connection> connection;

    public SQLQueryFactoryImpl(SQLTemplates templates, Provider<Connection> connection) {
        this(new Configuration(templates), connection);
    }

    public SQLQueryFactoryImpl(Configuration configuration, Provider<Connection> connection) {
        this.configuration = configuration;
        this.connection = connection;
    }

    public SQLDeleteClause delete(RelationalPath<?> path) {
        return new SQLDeleteClause(connection.get(), configuration, path);
    }

    public SQLQueryImpl from(Expression<?> from) {
        return query().from(from);
    }

    public SQLInsertClause insert(RelationalPath<?> path) {
        return new SQLInsertClause(connection.get(), configuration, path);
    }

    public SQLMergeClause merge(RelationalPath<?> path) {
        return new SQLMergeClause(connection.get(), configuration, path);
    }

    public SQLUpdateClause update(RelationalPath<?> path) {
        return new SQLUpdateClause(connection.get(), configuration, path);
    }

    public SQLQueryImpl query(){
        return new SQLQueryImpl(connection.get(), configuration);
    }

    public SQLSubQuery subQuery(){
        return new SQLSubQuery();
    }

    public SQLSubQuery subQuery(Expression<?> from){
        return subQuery().from(from);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Connection getConnection() {
        return connection.get();
    }

}
