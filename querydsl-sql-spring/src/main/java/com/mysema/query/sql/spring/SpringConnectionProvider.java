package com.mysema.query.sql.spring;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;

import org.springframework.jdbc.datasource.DataSourceUtils;

public class SpringConnectionProvider implements Provider<Connection> {
    
    private final DataSource dataSource;
    
    public SpringConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public Connection get() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if (!DataSourceUtils.isConnectionTransactional(connection, dataSource)) {
            throw new IllegalStateException("Connection is not transactional");
        }
        return connection;
    }
    
}