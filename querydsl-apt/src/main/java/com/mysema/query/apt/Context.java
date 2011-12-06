package com.mysema.query.apt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;

import com.mysema.query.codegen.EntityType;

/**
 * @author tiwe
 *
 */
public class Context {

    final Map<String, EntityType> actualSupertypes  = new HashMap<String, EntityType>();

    final Map<String, EntityType> allSupertypes = new HashMap<String, EntityType>();

    final Map<String, EntityType> projectionTypes = new HashMap<String, EntityType>();

    final Map<String, EntityType> embeddables = new HashMap<String,EntityType>();

    final Map<String, EntityType> entityTypes = new HashMap<String, EntityType>();

    final Map<String, EntityType> extensionTypes = new HashMap<String,EntityType>();
    
    final Map<String, Set<TypeElement>> typeElements = new HashMap<String,Set<TypeElement>>();

    public void clean() {
        for (String key : actualSupertypes.keySet()) {
            entityTypes.remove(key);
            extensionTypes.remove(key);
            embeddables.remove(key);
        }
        
        for (String key : entityTypes.keySet()) {
            actualSupertypes.remove(key);
            extensionTypes.remove(key);
            embeddables.remove(key);
        }
    }
    
}
