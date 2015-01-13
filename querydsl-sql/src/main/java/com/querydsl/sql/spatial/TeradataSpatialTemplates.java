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

import com.querydsl.spatial.SpatialOps;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.SchemaAndTable;
import com.querydsl.sql.TeradataTemplates;
import com.querydsl.sql.types.StringAsObjectType;

/**
 * TeradataSpatialTemplates is a spatial enabled SQL dialect for Teradata
 *
 * @author tiwe
 *
 */
public class TeradataSpatialTemplates extends TeradataTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final TeradataSpatialTemplates DEFAULT = new TeradataSpatialTemplates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new TeradataSpatialTemplates(escape, quote);
            }
        };
    }

    public TeradataSpatialTemplates() {
        this('\\', false);
    }

    public TeradataSpatialTemplates(boolean quote) {
        this('\\', quote);
    }

    public TeradataSpatialTemplates(char escape, boolean quote) {
        super(escape, quote);
        addCustomType(GeometryWktClobType.DEFAULT);
        addCustomType(StringAsObjectType.DEFAULT);
        addTableOverride(new SchemaAndTable("public", "geometry_columns"),
                new SchemaAndTable("sysspatial", "geometry_columns"));
        addTableOverride(new SchemaAndTable("public", "spatial_ref_sys"),
                new SchemaAndTable("sysspatial", "spatial_ref_sys"));
        add(SpatialTemplatesSupport.getSpatialOps(false));
        add(SpatialOps.DISTANCE_SPHERE, "{0}.ST_SPHERICALDISTANCE({1})");
        add(SpatialOps.DISTANCE_SPHEROID, "{0}.ST_SPHEROIDALDISTANCE({1})");
    }

}
