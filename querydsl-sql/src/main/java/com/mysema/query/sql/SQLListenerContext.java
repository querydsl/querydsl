package com.mysema.query.sql;

import com.mysema.query.QueryMetadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Map;

/**
 * A context object that is progressively filled out during query execution and is
 * passed to each {@link SQLDetailedListener} callback method
 */
public interface SQLListenerContext
{
    /**
     * The context map is a general purpose place that listeners can place objects.  It allows listeners to pass context
     * between themselves during callbacks.
     * <p/>
     * This is never null but can be empty
     *
     * @return the context map
     */
    Map<String, Object> map();

    /**
     * @return the underlying query metadata
     */
    QueryMetadata queryMetadata();

    /**
     * NOTE : This can be null depending on the stage of the query execution
     *
     * @return the underlying sql
     */
    String sql();

    /**
     * NOTE : This can be null depending on the stage of the query execution
     *
     * @return the underlying entity affected
     */
    RelationalPath<?> entity();

    /**
     * NOTE : This can be null depending on the stage of the query execution
     *
     * @return the underlying connection if there is one
     */
    Connection connection();

    /**
     * NOTE : This can be null depending on whether an exception occurred
     *
     * @return the underlying exception that has happened during query execution
     */
    Exception exception();

    /**
     * NOTE : This can be null depending on the stage of the query execution
     *
     * @return the underlying set of prepared statements
     */
    Collection<PreparedStatement> preparedStatements();

}
