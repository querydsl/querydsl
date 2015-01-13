/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.sql.types;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Defines the de/serialization of a typed Java object from a ResultSet or to a PreparedStatement
 *
 * <p>getValue(ResultSet, int) is used for extraction and setValue(PreparedStatement, int, T) is
 * used for population</p>
 *
 * @author tiwe
 *
 * @param <T>
 */
// TODO : rename this since it clashes with com.mysema.codegen.model.Type
public interface Type<T> {

    /**
     * Get the SQL supported SQL types
     *
     * @return
     */
    int[] getSQLTypes();

    /**
     * Get the returned type
     *
     * @return
     */
    Class<T> getReturnedClass();

    /**
     * Get the literal representation
     *
     * @param value
     * @return
     */
    String getLiteral(T value);

    /**
     * Get the object from the result set
     *
     * @param rs result set
     * @param startIndex column index in result set
     * @return
     * @throws SQLException
     */
    @Nullable
    T getValue(ResultSet rs, int startIndex) throws SQLException;

    /**
     * Set the object to the statement
     *
     * @param st statement
     * @param startIndex column index in statement
     * @param value value to be set
     * @throws SQLException
     */
    void setValue(PreparedStatement st, int startIndex, T value) throws SQLException;

}