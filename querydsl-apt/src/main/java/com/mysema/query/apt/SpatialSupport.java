package com.mysema.query.apt;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mysema.codegen.model.SimpleType;
import com.mysema.query.codegen.AbstractModule;
import com.mysema.query.codegen.CodegenModule;
import com.mysema.query.codegen.TypeMappings;

public final class SpatialSupport {

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
            typeMappings.register(
                    new SimpleType("org.geolatte.geom."+ entry.getKey()),
                    new SimpleType("com.mysema.query.spatial.path." + entry.getValue()));
        }
    }
    
    private static void registerJTSTypes(TypeMappings typeMappings) {
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
    	for (Map.Entry<String, String> entry : additions.entrySet()) {
    		typeMappings.register(
    				new SimpleType("com.vividsolutions.jts.geom."+ entry.getKey()),
    				new SimpleType("com.mysema.query.spatial.jts.path." + entry.getValue()));
    	}
    }

    private static void addImports(AbstractModule module, String packageName) {
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

    public static void addSupport(AbstractModule module) {
        registerTypes(module.get(TypeMappings.class));
        addImports(module,"com.mysema.query.spatial.path");
    }
    
    public static void addJTSSupport(AbstractModule module) {
    	registerJTSTypes(module.get(TypeMappings.class));
    	addImports(module,"com.mysema.query.spatial.jts.path");
    }

    private SpatialSupport() {}
}
