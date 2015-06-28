package com.querydsl.example.sql.repository;

import java.sql.Connection;

import javax.inject.Inject;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Expression;
import com.querydsl.example.sql.guice.ConnectionContext;

public abstract class AbstractRepository {
    @Inject
    private Configuration configuration;

    @Inject
    private ConnectionContext context;

    private Connection getConnection() {
        return context.getConnection();
    }

    private SQLQuery query() {
        return new SQLQuery(getConnection(), configuration);
    }

    protected SQLQuery from(Expression<?> expression) {
        return query().from(expression);
    }

    protected SQLInsertClause insert(RelationalPath<?> path) {
        return new SQLInsertClause(getConnection(), configuration, path);
    }

    protected SQLUpdateClause update(RelationalPath<?> path) {
        return new SQLUpdateClause(getConnection(), configuration, path);
    }

    protected SQLDeleteClause delete(RelationalPath<?> path) {
        return new SQLDeleteClause(getConnection(), configuration, path);
    }
}
