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

import com.querydsl.codegen.AbstractModule;
import com.querydsl.codegen.Extension;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.codegen.SQLCodegenModule;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;

/**
 * {@code SpatialSupport} provides support for spatial types in code generation
 *
 * @author tiwe
 *
 */
public final class SpatialSupport implements Extension {

    private static void registerTypes(Configuration configuration) {
        // mysql & postgresql
        configuration.registerType("geometry", Geometry.class);
        configuration.registerType("point", Point.class);
        configuration.registerType("linestring", LineString.class);
        configuration.registerType("polygon", Polygon.class);
        configuration.registerType("multipoint", MultiPoint.class);
        configuration.registerType("multilinestring", MultiLineString.class);
        configuration.registerType("multipolygon", MultiPolygon.class);
        configuration.registerType("geometrycollection", GeometryCollection.class);
        // teradata
        configuration.registerType("sysudtlib.st_geometry", Geometry.class);
    }

    /**
     * Register spatial types to the given codegen module
     *
     * @param module module to be customized for spatial support
     */
    public void addSupport(AbstractModule module) {
        module.bindInstance(SQLCodegenModule.ENTITYPATH_TYPE, RelationalPathSpatial.class);
        registerTypes(module.get(Configuration.class));
    }

}
