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
package com.querydsl.r2dbc.types;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;

/**
 * Defines the de/serialization of a typed Java object from a Result or to a Statement
 *
 * <p>getValue(Result, int) is used for extraction and setValue(Statement, int, T) is
 * used for population</p>
 *
 * @param <T>
 * @author tiwe
 */
public interface Type<T> {

    /**
     * Get the SQL supported SQL types
     *
     * @return sql types
     */
    int[] getSQLTypes();

    /**
     * Get the returned type
     *
     * @return returned class
     */
    Class<T> getReturnedClass();

    /**
     * Get the literal representation
     *
     * @param value value
     * @return literal representation
     */
    String getLiteral(T value);

    /**
     * Get the object from the result set
     *
     * @param row        result set
     * @param startIndex column index in result set
     * @return value
     */
    T getValue(Row row, int startIndex);

    /**
     * Set the object to the statement
     *
     * @param st         statement
     * @param startIndex column index in statement
     * @param value      value to be set
     */
    void setValue(Statement st, int startIndex, T value);

}