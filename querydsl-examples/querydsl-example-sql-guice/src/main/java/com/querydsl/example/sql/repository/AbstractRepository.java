package com.querydsl.example.sql.repository;

import com.querydsl.example.sql.guice.ConnectionContext;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;

public abstract class AbstractRepository {
    @Inject
    private Configuration configuration;

    @Inject
    private ConnectionContext context;

    @Inject
    private DataSource datasource;

    private Connection getConnection() {
        return context.getConnection();
    }

    protected SQLQueryFactory queryFactory() {
        return new SQLQueryFactory(configuration, datasource);
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
