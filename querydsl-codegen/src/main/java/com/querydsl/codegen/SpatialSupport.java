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

package com.querydsl.codegen;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mysema.codegen.model.SimpleType;

/**
 * Registers spatial paths for both geolatte and JTS. Registration can be done either to an
 * {@link AbstractModule} or directly to {@link TypeMappings}.
 *
 */
public final class SpatialSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpatialSupport.class);

    private static void registerTypes(TypeMappings typeMappings) {
        Map<String, String> additions = Maps.newHashMap();
        additions.put("Geometry", "GeometryPath");
        additions.put("GeometryCollection", "GeometryCollectionPath");
        additions.put("LinearRing", "LinearRingPath");
        additions.put("LineString", "LineStringPath");
        additions.put("MultiLineString", "MultiLineStringPath");
        additions.put("MultiPoint", "MultiPointPath");
        additions.put("MultiPolygon", "MultiPolygonPath");
        additions.put("Point", "PointPath");
        additions.put("Polygon", "PolygonPath");
        additions.put("PolyHedralSurface", "PolyhedralSurfacePath");
        for (Map.Entry<String, String> entry : additions.entrySet()) {
            typeMappings.register(new SimpleType("org.geolatte.geom." + entry.getKey()),
                                  new SimpleType("com.querydsl.spatial." + entry.getValue()));
        }
    }

    private static void registerJTSTypes(TypeMappings typeMappings) {
        Map<String, String> additions = Maps.newHashMap();
        additions.put("Geometry", "JTSGeometryPath");
        additions.put("GeometryCollection", "JTSGeometryCollectionPath");
        additions.put("LinearRing", "JTSLinearRingPath");
        additions.put("LineString", "JTSLineStringPath");
        additions.put("MultiLineString", "JTSMultiLineStringPath");
        additions.put("MultiPoint", "JTSMultiPointPath");
        additions.put("MultiPolygon", "JTSMultiPolygonPath");
        additions.put("Point", "JTSPointPath");
        additions.put("Polygon", "JTSPolygonPath");
        for (Map.Entry<String, String> entry : additions.entrySet()) {
            typeMappings.register(new SimpleType("com.vividsolutions.jts.geom." + entry.getKey()),
                                  new SimpleType("com.querydsl.spatial.jts." + entry.getValue()));
        }
    }

    private static void addImports(AbstractModule module, String packageName) {
        @SuppressWarnings("unchecked")
        Set<String> imports = module.get(Set.class, CodegenModule.IMPORTS);
        if (imports.isEmpty()) {
            imports = ImmutableSet.of(packageName);
        } else {
            Set<String> old = imports;
            imports = Sets.newHashSet();
            imports.addAll(old);
            imports.add(packageName);
        }
        module.bind(CodegenModule.IMPORTS, imports);
    }

    /**
     * Registers support for spatial path mappings. Does nothing, if spatial module is not on the classpath.
     *
     * @param typeMappings
     *        the type mappings
     */
    public static void addSupport(TypeMappings typeMappings) {
        if (isSpatialOnClassPath()) {
            registerTypes(typeMappings);
            registerJTSTypes(typeMappings);
        }
    }

    /**
     * Adds support for spatial path mappings. Does nothing, if spatial module is not on the classpath.
     *
     * @param module
     *        the module
     */
    public static void addSupport(AbstractModule module) {
        if (isSpatialOnClassPath()) {
            addSupport(module.get(TypeMappings.class));
            addImports(module, "com.querydsl.spatial.path");
            addImports(module, "com.querydsl.spatial.jts.path");
        }
    }

    public static boolean isSpatialOnClassPath() {
        try {
            // register additional mappings if querydsl-spatial is on the classpath
            Class.forName("com.querydsl.spatial.GeometryExpression");
            return true;
        } catch (Exception e) {
            LOGGER.warn("Spatial module not found on classpath! Skip register spatial types.");
            return false;
        }

    }

    private SpatialSupport() {
    }
}
