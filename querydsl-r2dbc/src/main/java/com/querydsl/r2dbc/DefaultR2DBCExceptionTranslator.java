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
package com.querydsl.r2dbc;

import com.querydsl.core.QueryException;
import com.querydsl.r2dbc.support.R2DBCExceptionWrapper;
import com.querydsl.sql.SQLExceptionTranslator;

import java.sql.SQLException;
import java.util.List;

/**
 * Default implementation of the {@link SQLExceptionTranslator} interface
 *
 * @author tiwe
 */
public final class DefaultR2DBCExceptionTranslator implements R2DBCExceptionTranslator {

    public static final R2DBCExceptionTranslator DEFAULT = new DefaultR2DBCExceptionTranslator();

    private static final R2DBCExceptionWrapper WRAPPER = R2DBCExceptionWrapper.INSTANCE;

    @Override
    public RuntimeException translate(Throwable e) {
        if (containsAdditionalExceptions(e)) {
            return WRAPPER.wrap(e);
        } else {
            return new QueryException(e);
        }
    }

    @Override
    public RuntimeException translate(String sql, List<Object> bindings, Throwable e) {
        String message = "Caught " + e.getClass().getSimpleName()
                + " for " + sql;
        if (containsAdditionalExceptions(e)) {
            return WRAPPER.wrap(message, e);
        } else {
            return new QueryException(message, e);
        }
    }

    private static boolean containsAdditionalExceptions(Throwable e) {
        if (!SQLException.class.isAssignableFrom(e.getClass())) {
            return false;
        }

        SQLException sqlException = (SQLException) e;
        return sqlException.getNextException() != null;
    }

    private DefaultR2DBCExceptionTranslator() {
    }
}
