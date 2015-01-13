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
package com.querydsl.apt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;

import com.querydsl.codegen.EntityType;

/**
 * Context of handled types used by {@link AbstractQuerydslProcessor}
 * 
 * @author tiwe
 *
 */
public class Context {

    final Map<String, EntityType> supertypes  = new HashMap<String, EntityType>();

    final Map<String, EntityType> allTypes = new HashMap<String, EntityType>();

    final Map<String, EntityType> projectionTypes = new HashMap<String, EntityType>();

    final Map<String, EntityType> embeddableTypes = new HashMap<String,EntityType>();

    final Map<String, EntityType> entityTypes = new HashMap<String, EntityType>();

    final Map<String, EntityType> extensionTypes = new HashMap<String,EntityType>();
    
    final Map<String, Set<TypeElement>> typeElements = new HashMap<String,Set<TypeElement>>();

    public void clean() {
        for (String key : supertypes.keySet()) {
            entityTypes.remove(key);
            extensionTypes.remove(key);
            embeddableTypes.remove(key);
        }
        
        for (String key : entityTypes.keySet()) {
            supertypes.remove(key);
            extensionTypes.remove(key);
            embeddableTypes.remove(key);
        }
    }
    
}
