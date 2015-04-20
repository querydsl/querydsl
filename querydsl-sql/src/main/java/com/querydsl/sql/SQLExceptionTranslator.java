/*
 * Copyright 2014, Mysema Ltd
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

import java.sql.SQLException;
import java.util.List;

/**
 * {@code SQLExceptionTranslator} translate SQLExceptions to runtime exceptions
 *
 * @author tiwe
 *
 */
public interface SQLExceptionTranslator {

    /**
     * Translate the given SQLException
     *
     * @param sql SQL string
     * @param bindings binding
     * @param e SQLException to translate
     * @return translated exception
     */
    RuntimeException translate(String sql, List<Object> bindings, SQLException e);

    /**
     * Translate the given SQLException
     *
     * @param e SQLException to translate
     * @return translated exception
     */
    RuntimeException translate(SQLException e);
}
