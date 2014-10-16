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
package com.mysema.query.sql.support;

import java.sql.SQLException;
import java.util.ArrayList;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mysema.query.QueryException;

/**
 * A {@link SQLExceptionWrapper} that wraps the additional exception
 * information in a {@code WrappedSQLCauseException} and links it to the chain.
 *
 * @author Shredder121
 */
class JavaSE6SQLExceptionWrapper extends SQLExceptionWrapper {

    @Override
    public RuntimeException wrap(SQLException exception) {
        Iterable<Throwable> linkedSQLExceptions = getLinkedSQLExceptions(exception);
        return new QueryException(
                new WrappedSQLCauseException(linkedSQLExceptions, exception));
    }

    @Override
    public RuntimeException wrap(String message, SQLException exception) {
        Iterable<Throwable> linkedSQLExceptions = getLinkedSQLExceptions(exception);
        return new QueryException(message,
                new WrappedSQLCauseException(linkedSQLExceptions, exception));
    }

    private static Iterable<Throwable> getLinkedSQLExceptions(SQLException exception) {
        ArrayList<Throwable> rv = Lists.newArrayList();
        SQLException nextException = exception.getNextException();
        while (nextException != null) {
            rv.add(nextException);
            nextException = nextException.getNextException();
        }
        return rv;
    }

    private static class WrappedSQLCauseException extends Exception {

        private static final long serialVersionUID = 1L;

        private WrappedSQLCauseException(Iterable<Throwable> exceptions, SQLException exception) {
            super("Detailed SQLException information:\n" + Iterables
                    .transform(exceptions, exceptionMessageFunction), exception);
        }
    }
    private static final Function<Throwable, String> exceptionMessageFunction = new Function<Throwable, String>() {
        @Override
        public String apply(Throwable input) {
            if (input instanceof SQLException) {
                SQLException sqle = (SQLException) input;
                return new StringBuilder()
                        .append("SQLState: ").append(sqle.getSQLState())
                        .append(", ")
                        .append("ErrorCode: ").append(sqle.getErrorCode())
                        .append(", ")
                        .append("Message: ").append(sqle.getMessage())
                        .toString();
            }
            return input.toString();
        }
    };

}
