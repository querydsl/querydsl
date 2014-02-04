/*
 * Copyright 2011, Mysema Ltd
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
package com.mysema.query.sql.postgres;

import com.mysema.query.sql.PostgresTemplates;
import com.mysema.query.sql.SQLTemplates;

/**
 * @author tiwe
 *
 */
public class PostGISTemplates extends PostgresTemplates {

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new PostGISTemplates(escape, quote);
            }
        };
    }

    public PostGISTemplates() {
        this('\\', false);
    }

    public PostGISTemplates(boolean quote) {
        this('\\', quote);
    }

    public PostGISTemplates(char escape, boolean quote) {
        super(escape, quote);
        addSpatialOps(true);
        addCustomType(PGgeometryType.DEFAULT);
    }

}
