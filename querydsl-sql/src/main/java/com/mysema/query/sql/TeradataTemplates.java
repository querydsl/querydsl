/*
 * Copyright 2013, Mysema Ltd
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


/**
 * TeradataTemplates is a SQL dialect for Teradata
 *
 * @author tiwe
 *
 */
public class TeradataTemplates extends SQLTemplates {

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new TeradataTemplates(escape, quote);
            }
        };
    }

    public TeradataTemplates() {
        this('\\', false);
    }

    public TeradataTemplates(boolean quote) {
        this('\\', quote);
    }

    public TeradataTemplates(char escape, boolean quote) {
        super("\"", escape, quote);
        setDummyTable(null);
        addClass2TypeMappings("double precision", Double.class);
    }

}
