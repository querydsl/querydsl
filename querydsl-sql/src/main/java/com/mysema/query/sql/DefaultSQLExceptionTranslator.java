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
package com.mysema.query.sql;

import java.sql.SQLException;

import com.mysema.query.QueryException;

/**
 * Default implementation of the SQLExceptionTranslator interface
 *
 * @author tiwe
 *
 */
public final class DefaultSQLExceptionTranslator implements SQLExceptionTranslator {

    public static final SQLExceptionTranslator DEFAULT = new DefaultSQLExceptionTranslator();

    @Override
    public RuntimeException translate(String sql, SQLException e) {
        if (sql != null) {
            return new QueryException("Caught " + e.getClass().getSimpleName() + " for " + sql);
        } else {
            return new QueryException(e);
        }
    }

    private DefaultSQLExceptionTranslator() {}
}
