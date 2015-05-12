/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;

import com.querydsl.core.QueryMetadata;

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
     * A good time to place objects into the context is during {@link com.querydsl.sql.SQLDetailedListener#start(SQLListenerContext)}
     * and then access if after that.
     *
     * @param dataKey the key to use
     * @param value   the value to place under that key
     */
    void setData(String dataKey, Object value);

    /**
     * Return the underlying query metadata
     *
     * @return the underlying query metadata
     */
    QueryMetadata getMetadata();

    /**
     * Return the underlying sql or first in a batch query
     *
     * <p>NOTE : This can be null depending on the stage of the query execution</p>
     *
     * @return the underlying sql or first in a batch query
     */
    String getSQL();

    /**
     * Return the underlying sql collection if the query is a batch query
     *
     * <p>NOTE : This can be empty depending on the stage of the query execution</p>
     *
     * @return the underlying sql collection if the query is a batch query
     */
    Collection<String> getSQLStatements();

    /**
     * Return the underlying entity affected
     *
     * <p>NOTE : This can be null depending on the stage of the query execution</p>
     *
     * @return the underlying entity affected
     */
    RelationalPath<?> getEntity();

    /**
     * Return the underlying connection if there is one
     *
     * <p>NOTE : This can be null depending on the stage of the query execution</p>
     *
     * @return the underlying connection if there is one
     */
    Connection getConnection();

    /**
     * Return the underlying exception that has happened during query execution
     *
     * <p>NOTE : This can be null depending on whether an exception occurred</p>
     *
     * @return the underlying exception that has happened during query execution
     */
    Exception getException();

    /**
     * Return the underlying prepared statement or the first if its batch query
     *
     * <p>NOTE : This can be null depending on the stage of the query execution</p>
     *
     * @return the underlying prepared statement or the first if its batch query
     */
    PreparedStatement getPreparedStatement();

    /**
     * Return the underlying set of prepared statements
     *
     * <p>NOTE : This can be empty depending on the stage of the query execution</p>
     *
     * @return the underlying set of prepared statements
     */
    Collection<PreparedStatement> getPreparedStatements();

}
