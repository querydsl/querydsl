/*
 * Copyright 2014, Timo Westkämper
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
package com.querydsl.sql.codegen.support;

import java.sql.SQLException;

import com.querydsl.core.QueryException;

/**
 * A {@link SQLExceptionWrapper} that adds the additional
 * {@code SQLException}s as suppressed exceptions.
 *
 * @author Shredder121
 */
class JaveSE7SQLExceptionWrapper extends SQLExceptionWrapper {

    @Override
    public RuntimeException wrap(SQLException exception) {
        QueryException rv = new QueryException(exception);
        SQLException linkedException = exception.getNextException();
        while (linkedException != null) {
            rv.addSuppressed(linkedException);
            linkedException = linkedException.getNextException();
        }
        return rv;
    }

    @Override
    public RuntimeException wrap(String message, SQLException exception) {
        QueryException rv = new QueryException(message, exception);
        SQLException linkedException = exception.getNextException();
        while (linkedException != null) {
            rv.addSuppressed(linkedException);
            linkedException = linkedException.getNextException();
        }
        return rv;
    }

}
