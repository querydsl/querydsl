/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.querydsl.r2dbc.ddl;

import com.querydsl.r2dbc.Configuration;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import reactor.core.publisher.Mono;

/**
 * DropTableClause defines a DROP TABLE clause
 *
 * @author tiwe
 */
public class DropTableClause {

    private final Connection connection;

    private final String table;

    public DropTableClause(Connection connection, Configuration c, String table) {
        this.connection = connection;
        this.table = c.getTemplates().quoteIdentifier(table);
    }

    @SuppressWarnings("SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE")
    public Mono<Void> execute() {
        Statement statement = connection.createStatement("DROP TABLE " + table);
        return Mono.from(statement.execute()).then();
    }

}
