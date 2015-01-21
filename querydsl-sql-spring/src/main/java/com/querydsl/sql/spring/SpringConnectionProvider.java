/*
 * Copyright 2014, Timo Westkämper
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql.spring;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;

import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * SpringConnectionProvider is a Provider implementation which provides a transactionally bound connection
 *
 * <p>Usage example</p>
 * <pre>
 * {@code
 * Provider<Connection> provider = new SpringConnectionProvider(dataSource());
 * SQLQueryFactory queryFactory = SQLQueryFactory(configuration, provider);
 * }
 * </pre>
 */
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