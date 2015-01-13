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

import com.querydsl.core.QueryException;
import com.querydsl.sql.codegen.support.SQLExceptionWrapper;

/**
 * Default implementation of the SQLExceptionTranslator interface
 *
 * @author tiwe
 *
 */
public final class DefaultSQLExceptionTranslator implements SQLExceptionTranslator {

    public static final SQLExceptionTranslator DEFAULT = new DefaultSQLExceptionTranslator();

    private static final SQLExceptionWrapper WRAPPER = SQLExceptionWrapper.INSTANCE;

    @Override
    public RuntimeException translate(SQLException e) {
        if (containsAdditionalExceptions(e)) {
            return WRAPPER.wrap(e);
        } else {
            return new QueryException(e);
        }
    }

    @Override
    public RuntimeException translate(String sql, List<Object> bindings, SQLException e) {
        String message = "Caught " + e.getClass().getSimpleName()
                + " for " + sql;
        if (containsAdditionalExceptions(e)) {
            return WRAPPER.wrap(message, e);
        } else {
            return new QueryException(message, e);
        }
    }

    private static boolean containsAdditionalExceptions(SQLException e) {
        return e.getNextException() != null;
    }

    private DefaultSQLExceptionTranslator() {}
}
