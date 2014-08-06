package com.mysema.query.sql;

import com.google.common.collect.Lists;
import com.mysema.query.QueryMetadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A builder of {@link com.mysema.query.sql.SQLListenerContext} objects
 */
public class SQLListenerContextBuilder
{
    private Map<String, Object> contextMap = new HashMap<String, Object>();

    private QueryMetadata md;

    private String sql;

    private RelationalPath<?> entity;

    private Connection connection;

    private Exception exception;

    private Collection<PreparedStatement> preparedStatements;

    SQLListenerContextBuilder(final QueryMetadata md)
    {
        this.md = md;
    }

    public static SQLListenerContextBuilder newContext(QueryMetadata md)
    {
        return new SQLListenerContextBuilder(md);
    }

    public static SQLListenerContextBuilder newContext(SQLListenerContext context)
    {
        return new SQLListenerContextBuilder(context.getMetadata())
                .with(context.getMap())
                .with(context.getConnection())
                .with(context.getEntity())
                .with(context.getException())
                .with(context.getPreparedStatements())
                .with(context.getSQL())
                ;
    }

    SQLListenerContextBuilder with(Map<String, Object> contextMap)
    {
        this.contextMap = contextMap;
        return this;
    }

    public SQLListenerContextBuilder with(RelationalPath<?> entity)
    {
        this.entity = entity;
        return this;
    }

    public SQLListenerContextBuilder with(String sql)
    {
        this.sql = sql;
        return this;
    }

    public SQLListenerContextBuilder with(Exception exception)
    {
        this.exception = exception;
        return this;
    }

    public SQLListenerContextBuilder with(Connection connection)
    {
        this.connection = connection;
        return this;
    }

    public SQLListenerContextBuilder with(PreparedStatement preparedStatement)
    {
        if (preparedStatement != null)
        {
            this.preparedStatements = Lists.newArrayList(preparedStatement);
        }
        return this;
    }

    public SQLListenerContextBuilder with(Collection<PreparedStatement> preparedStatements)
    {
        if (preparedStatements != null)
        {
            this.preparedStatements = Lists.newArrayList(preparedStatements);
        }
        return this;
    }

    public SQLListenerContext build()
    {
        return new SQLListenerContextImpl();
    }

    /**
     * A simple implementation that looks at the builder for values
     */
    class SQLListenerContextImpl implements SQLListenerContext
    {
        @Override
        public QueryMetadata getMetadata()
        {
            return md;
        }

        @Override
        public RelationalPath<?> getEntity()
        {
            return entity;
        }

        @Override
        public String getSQL()
        {
            return sql;
        }

        @Override
        public Exception getException()
        {
            return exception;
        }

        @Override
        public Connection getConnection()
        {
            return connection;
        }

        @Override
        public Collection<PreparedStatement> getPreparedStatements()
        {
            return preparedStatements;
        }

        @Override
        public Map<String, Object> getMap()
        {
            return contextMap;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder()
                    .append(" sql:").append(nicerSql(sql))
                    .append(" connection:").append(connection == null ? "not connected" : "connected")
                    .append(" entity:").append(entity)
                    .append(" exception:").append(exception);

            for (Map.Entry<String, Object> entry : contextMap.entrySet())
            {
                sb.append(" [").append(entry.getKey()).append(":").append(entry.getValue()).append("]");
            }
            return sb.toString();
        }

        private String nicerSql(final String sql)
        {
            return "'" + (sql == null ? null : sql.replace('\n', ' ')) + "'";
        }
    }

}
