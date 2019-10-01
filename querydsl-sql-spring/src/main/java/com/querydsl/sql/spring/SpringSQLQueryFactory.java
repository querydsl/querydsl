package com.querydsl.sql.spring;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLBaseListener;
import com.querydsl.sql.SQLListenerContext;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Support connection is not transactional when used in spring, spring manages and releases resources.
 * <p>Usage example</p>
 * <pre>
 * {@code
 * DataSource dataSource = dataSource();
 * SQLQueryFactory queryFactory = new SpringSQLQueryFactory(configuration, dataSource);
 * }
 * </pre>
 * @author <a href="mailto:hedyn@foxmail.com">HeDYn</a>
 */
public class SpringSQLQueryFactory extends SQLQueryFactory {

    private static final SpringSQLListener SPRING_SQL_LISTENER = new SpringSQLListener();

    public SpringSQLQueryFactory(SQLTemplates templates, DataSource dataSource) {
        this(new Configuration(templates), dataSource);
    }

    public SpringSQLQueryFactory(Configuration configuration, DataSource dataSource) {
        super(setupListener(configuration), new LazySpringConnectionProvider(dataSource));
    }

    private static Configuration setupListener(Configuration configuration) {
        configuration.addListener(SPRING_SQL_LISTENER);
        return configuration;
    }

    private static final class SpringSQLListener extends SQLBaseListener {

        private SpringSQLListener() {
        }

        @Override
        public void start(SQLListenerContext context) {
            Connection connection = context.getConnection();
            if (connection instanceof LazySpringConnection) {
                LazySpringConnection lazySpringConnection = (LazySpringConnection) connection;
                lazySpringConnection.insureCreate();
            }
        }

        @Override
        public void end(SQLListenerContext context) {
            Connection connection = context.getConnection();
            if (connection instanceof LazySpringConnection) {
                LazySpringConnection lazySpringConnection = (LazySpringConnection) connection;
                lazySpringConnection.insureRelease();
            }
        }
    }

    private static final class LazySpringConnectionProvider implements Provider<Connection> {

        private final DataSource dataSource;

        private LazySpringConnectionProvider(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public Connection get() {
            return new LazySpringConnection(dataSource);
        }

    }
}
