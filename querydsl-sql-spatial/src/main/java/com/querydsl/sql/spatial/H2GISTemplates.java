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
package com.querydsl.sql.spatial;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLTemplates;

/**
 * {@code GeoDBTemplates} is a spatial enabled SQL dialect for GeoDB
 *
 * @author tiwe
 *
 */
public class H2GISTemplates extends H2Templates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final H2GISTemplates DEFAULT = new H2GISTemplates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new H2GISTemplates(escape, quote);
            }
        };
    }

    public H2GISTemplates() {
        this('\\', false);
    }

    public H2GISTemplates(boolean quote) {
        this('\\', quote);
    }

    public H2GISTemplates(char escape, boolean quote) {
        super(escape, quote);
        addCustomType(H2GISWkbType.DEFAULT);
        add(SpatialTemplatesSupport.getSpatialOps(true));
    }

}