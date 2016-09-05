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
package com.querydsl.sql.namemapping;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.querydsl.sql.SchemaAndTable;

/**
 * Simple implementation of {@link NameMapping} that changes the letter-case
 * (lower-case or upper-case) of the schema, table and column names. The
 * information how the database stores the identifiers are available normally
 * from the <code>stores*Identifiers</code> function of the
 * {@link java.sql.DatabaseMetaData}
 */
public class ChangeCaseNameMapping implements NameMapping {

    /**
     * The target letter-case (lower or upper) that the
     * {@link ChangeCaseNameMapping} should use to convert the identifiers
     * names.
     */
    public enum LetterCase {
        LOWER, UPPER
    }

    private final LetterCase targetCase;

    public ChangeCaseNameMapping(LetterCase targetCase) {
        this.targetCase = Preconditions.checkNotNull(targetCase);
    }

    @Override
    public Optional<String> getColumnOverride(SchemaAndTable key, String column) {
        return Optional.of(column.toUpperCase());
    }

    @Override
    public Optional<SchemaAndTable> getOverride(SchemaAndTable key) {
        String schema = key.getSchema();
        if (schema != null) {
            schema = schema.toUpperCase();
        }
        return Optional.of(new SchemaAndTable(targetCaseOrNull(key.getSchema()), targetCaseOrNull(key.getTable())));
    }

    private String targetCaseOrNull(String text) {
        if (text == null) {
            return null;
        }

        if (targetCase == LetterCase.LOWER) {
            return text.toLowerCase();
        } else {
            return text.toUpperCase();
        }
    }

}
