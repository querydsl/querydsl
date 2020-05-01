package com.querydsl.sql.spring;

import com.querydsl.core.QueryException;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.SQLBaseListener;
import com.querydsl.sql.SQLListenerContext;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * If you want to control the transaction more finely, such as using ShardingSphere or MyCat.
 *       You need to control the connection closing operation of non-transactional operations. Below is a configuration example:
 *     @Bean
 *     public SQLQueryFactory queryFactory(@Autowired DataSource dataSource) {
 *         Configuration configuration = new Configuration(MySQLTemplates.builder().build());
 *         configuration.addListener(new SpringSQLCloseListener(dataSource));
 *         return new SQLQueryFactory(
 *                 configuration,
 *                 () -> DataSourceUtils.getConnection(dataSource));
 *     }
 *
 * @see com.querydsl.sql.SQLCloseListener
 *
 */
public class SpringSQLCloseListener extends SQLBaseListener {
    private final DataSource dataSource;

    public SpringSQLCloseListener(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void end(SQLListenerContext context) {
        Connection connection = context.getConnection();
        if (connection != null
                && context.getData(AbstractSQLQuery.PARENT_CONTEXT) == null
                && !DataSourceUtils.isConnectionTransactional(connection, dataSource)) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new QueryException(e);
            }
        }
    }
}
