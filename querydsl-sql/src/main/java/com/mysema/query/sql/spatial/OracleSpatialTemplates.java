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
package com.mysema.query.sql.spatial;

import org.geolatte.geom.Geometry;

import com.mysema.query.sql.OracleTemplates;
import com.mysema.query.sql.SQLTemplates;

/**
 * @author tiwe
 *
 */
public class OracleSpatialTemplates extends OracleTemplates {

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new OracleSpatialTemplates(escape, quote);
            }
        };
    }

    public OracleSpatialTemplates() {
        this('\\', false);
    }

    public OracleSpatialTemplates(boolean quote) {
        this('\\', quote);
    }

    public OracleSpatialTemplates(char escape, boolean quote) {
        super(escape, quote);
        addCustomType(JGeometryType.DEFAULT);
        // TODO
    }

    @Override
    public String asLiteral(Object o) {
        if (o instanceof Geometry) {
            // TODO
            return null;
        } else {
            return super.asLiteral(o);
        }
    }

}
