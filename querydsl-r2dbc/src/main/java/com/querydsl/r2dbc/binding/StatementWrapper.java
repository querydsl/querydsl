package com.querydsl.r2dbc.binding;

import io.r2dbc.spi.Statement;

/**
 * StatementWrapper
 */
public class StatementWrapper implements BindTarget {

    final Statement statement;

    public StatementWrapper(Statement statement) {
        this.statement = statement;
    }

    @Override
    public void bind(String identifier, Object value) {
        this.statement.bind(identifier, value);
    }

    @Override
    public void bind(int index, Object value) {
        this.statement.bind(index, value);
    }

    @Override
    public void bindNull(String identifier, Class<?> type) {
        this.statement.bindNull(identifier, type);
    }

    @Override
    public void bindNull(int index, Class<?> type) {
        this.statement.bindNull(index, type);
    }
}