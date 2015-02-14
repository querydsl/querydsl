/*
 * Copyright 2015, Timo Westkämper
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
package com.querydsl.maven;

import com.google.common.collect.ImmutableList;
import com.querydsl.sql.Configuration;

public class RenameMapping implements Mapping {

    String fromSchema, fromTable, fromColumn;

    String toSchema, toTable, toColumn;

    @Override
    public void apply(Configuration configuration) {
        if (fromSchema != null) {
            if (fromTable != null && fromColumn != null && toColumn != null) {
                configuration.registerColumnOverride(fromSchema, fromTable, fromColumn, toColumn);
            } else if (fromTable != null && toTable != null) {
                if (toSchema != null) {
                    configuration.registerTableOverride(fromSchema, fromTable, toSchema, toTable);
                } else {
                    configuration.registerTableOverride(fromSchema, fromTable, toTable);
                }
            } else if (toSchema != null) {
                configuration.registerSchemaOverride(fromSchema, toSchema);
            } else {
                insufficientArgs();
            }
        } else if (fromTable != null) {
            if (fromColumn != null && toColumn != null) {
                configuration.registerColumnOverride(fromTable, fromColumn, toColumn);
            } else if (toTable != null) {
                configuration.registerTableOverride(fromTable, toTable);
            } else {
                insufficientArgs();
            }
        } else {
            insufficientArgs();
        }
    }

    private void insufficientArgs() {
        throw new IllegalArgumentException("Insufficient args " +
                ImmutableList.of(fromSchema, fromTable, fromColumn) + " to " +
                ImmutableList.of(toSchema, toTable, toColumn));
    }
}
