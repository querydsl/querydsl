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
package com.querydsl.sql.spatial;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLTemplates;

/**
 * GeoDBTemplates is a spatial enabled SQL dialect for GeoDB
 *
 * @author tiwe
 *
 */
public class GeoDBTemplates extends H2Templates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final GeoDBTemplates DEFAULT = new GeoDBTemplates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new GeoDBTemplates(escape, quote);
            }
        };
    }

    public GeoDBTemplates() {
        this('\\', false);
    }

    public GeoDBTemplates(boolean quote) {
        this('\\', quote);
    }

    public GeoDBTemplates(char escape, boolean quote) {
        super(escape, quote);
        addCustomType(GeoDBWkbType.DEFAULT);
        add(SpatialTemplatesSupport.getSpatialOps(true));
    }

}