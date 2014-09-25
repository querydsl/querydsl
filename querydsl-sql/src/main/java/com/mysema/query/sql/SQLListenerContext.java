package com.mysema.query.sql;

import com.mysema.query.QueryMetadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;

/**
 * A context object that is progressively filled out during query execution and is passed to each {@link
 * SQLDetailedListener} callback method
 */
public interface SQLListenerContext {
    /**
     * The context getData is a general purpose place that listeners can place objects.  It allows listeners to pass
     * context between themselves during callbacks.
     * <p>
     *
     * @param dataKey the key to look up
     * @return the context object under that key
     */
    Object getData(String dataKey);

    /**
     * The context setData is a general purpose place that listeners can place objects.  It allows listeners to pass
     * context between themselves during callbacks.
     * <p>
     * A good time to place objects into the context is during {@link com.mysema.query.sql.SQLDetailedListener#start(SQLListenerContext)}
     * and then access if after that.
     *
     * @param dataKey the key to use
     * @param value   the value to place under that key
     */
    void setData(String dataKey, Object value);

    /**
     * @return the underlying query metadata
     */
    QueryMetadata getMetadata();

    /**
     * NOTE : This can be null depending on the stage of the query execution
     *
     * @return the underlying sql or first in a batch query
     */
    String getSQL();

    /**
     * NOTE : This can be empty depending on the stage of the query execution
     *
     * @return the underlying sql collection if the query is a batch query
     */
    Collection<String> getSQLStatements();

    /**
     * NOTE : This can be null depending on the stage of the query execution
     *
     * @return the underlying entity affected
     */
    RelationalPath<?> getEntity();

    /**
     * NOTE : This can be null depending on the stage of the query execution
     *
     * @return the underlying connection if there is one
     */
    Connection getConnection();

    /**
     * NOTE : This can be null depending on whether an exception occurred
     *
     * @return the underlying exception that has happened during query execution
     */
    Exception getException();

    /**
     * NOTE : This can be null depending on the stage of the query execution
     *
     * @return the underlying prepared statement or the first if its batch query
     */
    PreparedStatement getPreparedStatement();

    /**
     * NOTE : This can be empty depending on the stage of the query execution
     *
     * @return the underlying set of prepared statements
     */
    Collection<PreparedStatement> getPreparedStatements();

}
