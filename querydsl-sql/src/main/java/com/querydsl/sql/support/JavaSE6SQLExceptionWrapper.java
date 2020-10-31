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
package com.querydsl.sql.support;

import com.querydsl.core.QueryException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link SQLExceptionWrapper} that wraps the additional exception
 * information in a {@code WrappedSQLCauseException} and links it to the chain.
 *
 * @author Shredder121
 */
class JavaSE6SQLExceptionWrapper extends SQLExceptionWrapper {

    @Override
    public RuntimeException wrap(SQLException exception) {
        List<Throwable> linkedSQLExceptions = getLinkedSQLExceptions(exception);
        return new QueryException(
                new WrappedSQLCauseException(linkedSQLExceptions, exception));
    }

    @Override
    public RuntimeException wrap(String message, SQLException exception) {
        List<Throwable> linkedSQLExceptions = getLinkedSQLExceptions(exception);
        return new QueryException(message,
                new WrappedSQLCauseException(linkedSQLExceptions, exception));
    }

    private static List<Throwable> getLinkedSQLExceptions(SQLException exception) {
        ArrayList<Throwable> rv = new ArrayList<>();
        SQLException nextException = exception.getNextException();
        while (nextException != null) {
            rv.add(nextException);
            nextException = nextException.getNextException();
        }
        return rv;
    }

    private static final class WrappedSQLCauseException extends Exception {

        private static final long serialVersionUID = 1L;

        private WrappedSQLCauseException(List<Throwable> exceptions, SQLException exception) {
            super("Detailed SQLException information:" + System.lineSeparator()
                    + exceptions.stream().map(exceptionMessageFunction).collect(Collectors.joining(System.lineSeparator())), exception);
        }
    }

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
