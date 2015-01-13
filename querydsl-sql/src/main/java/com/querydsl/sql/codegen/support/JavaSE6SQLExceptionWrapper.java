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

import static com.google.common.base.StandardSystemProperty.LINE_SEPARATOR;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryException;

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
            super("Detailed SQLException information:" + LINE_SEPARATOR.value()
                    + lineJoiner.join(Iterables
                            .transform(exceptions, exceptionMessageFunction)), exception);
        }
    }

    private static final Joiner lineJoiner = Joiner.on(LINE_SEPARATOR.value());
    private static final Function<Throwable, String> exceptionMessageFunction = new Function<Throwable, String>() {
        @Override
        public String apply(Throwable input) {
            if (input instanceof SQLException) {
                SQLException sqle = (SQLException) input;
                StringWriter writer = new StringWriter();
                new PrintWriter(writer, true)
                        .printf("SQLState: %s%n", sqle.getSQLState())
                        .printf("ErrorCode: %s%n", sqle.getErrorCode())
                        .printf("Message: %s%n", sqle.getMessage());
                return writer.toString();
            }
            return input.toString();
        }
    };

}
