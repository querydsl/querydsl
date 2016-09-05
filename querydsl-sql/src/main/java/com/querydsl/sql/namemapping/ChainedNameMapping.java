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
import com.querydsl.sql.SchemaAndTable;

/**
 * A {@link NameMapping} implementation that accepts zero or more
 * {@link NameMapping}s and returns the first non-null mapping result.
 */
public class ChainedNameMapping implements NameMapping {

    private NameMapping[] nameMappings;

    public ChainedNameMapping(NameMapping... nameMappings) {
        if (nameMappings == null) {
            throw new NullPointerException("Name mapping array must not be null");
        }
        for (NameMapping nameMapping : nameMappings) {
            if (nameMapping == null) {
                throw new NullPointerException("Name mapping array must not contain null element");
            }
        }
        this.nameMappings = nameMappings.clone();
    }

    @Override
    public Optional<String> getColumnOverride(SchemaAndTable key, String column) {
        for (NameMapping nameMapping : nameMappings) {
            Optional<String> overriddenColumnName = nameMapping.getColumnOverride(key, column);
            if (overriddenColumnName.isPresent()) {
                return overriddenColumnName;
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<SchemaAndTable> getOverride(SchemaAndTable key) {
        for (NameMapping nameMapping : nameMappings) {
            Optional<SchemaAndTable> overridden = nameMapping.getOverride(key);
            if (overridden.isPresent()) {
                return overridden;
            }
        }
        return Optional.absent();
    }

}
