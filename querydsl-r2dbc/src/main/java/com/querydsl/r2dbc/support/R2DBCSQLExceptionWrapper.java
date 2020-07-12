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
package com.querydsl.r2dbc.support;

import com.querydsl.core.QueryException;

import java.sql.SQLException;

/**
 * A {@link AbstractR2DBCExceptionWrapper} that adds the additional
 * {@code SQLException}s as suppressed exceptions.
 *
 * @author Shredder121
 */
class R2DBCSQLExceptionWrapper extends AbstractR2DBCExceptionWrapper {

    @Override
    public RuntimeException wrap(Throwable exception) {
        if (!SQLException.class.isAssignableFrom(exception.getClass())) {
            return new QueryException(exception);
        }

        SQLException sqlException = (SQLException) exception;

        QueryException rv = new QueryException(sqlException);
        SQLException linkedException = sqlException.getNextException();
        while (linkedException != null) {
            rv.addSuppressed(linkedException);
            linkedException = linkedException.getNextException();
        }
        return rv;
    }

    @Override
    public RuntimeException wrap(String message, Throwable exception) {
        if (!SQLException.class.isAssignableFrom(exception.getClass())) {
            return new QueryException(message, exception);
        }

        SQLException sqlException = (SQLException) exception;

        QueryException rv = new QueryException(message, sqlException);
        SQLException linkedException = sqlException.getNextException();
        while (linkedException != null) {
            rv.addSuppressed(linkedException);
            linkedException = linkedException.getNextException();
        }
        return rv;
    }

}
