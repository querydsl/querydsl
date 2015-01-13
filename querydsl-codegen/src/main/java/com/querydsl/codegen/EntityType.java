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
package com.querydsl.codegen;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.*;

import com.mysema.codegen.StringUtils;
import com.mysema.codegen.model.Constructor;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeAdapter;
import com.mysema.codegen.model.TypeCategory;
import com.querydsl.core.util.JavaSyntaxUtils;

/**
 * EntityType represents a model of a querydsl domain type with properties
 *
 * @author tiwe
 */
public class EntityType extends TypeAdapter implements Comparable<EntityType> {

    private final Map<Class<?>,Annotation> annotations = new HashMap<Class<?>,Annotation>();

    private final Set<Constructor> constructors = new HashSet<Constructor>();

    private int escapeSuffix = 1;
 
    private final Set<Delegate> delegates = new HashSet<Delegate>();

    private final Set<Property> properties = new TreeSet<Property>();
    
    private final Set<String> propertyNames = new HashSet<String>();
    
    private final Set<String> escapedPropertyNames = new HashSet<String>();

    private final Set<Supertype> superTypes;

    private final Map<Object, Object> data = new HashMap<Object,Object>();

    private String uncapSimpleName;
    
    /**
     * Create a new EntityType instance for the given type
     * 
     * @param type
     */
    public EntityType(Type type) {
        this(type, new LinkedHashSet<Supertype>());
    }
    
    /**
     * Create a new EntityType instance for the given type and superTypes
     * 
     * @param type
     * @param superTypes
     */
    public EntityType(Type type, Set<Supertype> superTypes) {
        super(type);
        this.uncapSimpleName = StringUtils.uncapitalize(type.getSimpleName());
        if (JavaSyntaxUtils.isReserved(uncapSimpleName)) {
            this.uncapSimpleName = uncapSimpleName + "$";    
        }
        this.superTypes = superTypes;
    }

    public void addAnnotation(Annotation annotation) {
        annotations.put(annotation.annotationType(), annotation);
    }

    public void addConstructor(Constructor co) {
        constructors.add(co);
    }

    public void addDelegate(Delegate delegate) {
        delegates.add(delegate);
    }

    public void addProperty(Property field) {
        if (!propertyNames.contains(field.getName())) {
            propertyNames.add(field.getName());
            escapedPropertyNames.add(field.getEscapedName());
            properties.add(validateField(field));
        }
    }

    public void addSupertype(Supertype entityType) {
        superTypes.add(entityType);
    }

    @Override
    public int compareTo(EntityType o) {
        return getType().getSimpleName().compareTo(o.getType().getSimpleName());
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Type) {
            return getFullName().equals(((Type)o).getFullName());    
        } else {
            return false;
        }        
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> type) {
        return (T) annotations.get(type);
    }

    public Collection<Annotation> getAnnotations() {
        return annotations.values();
    }

    @Override
    public Type as(TypeCategory category) {
        if (getCategory() == category) {
            return this;
        } else {
            return super.as(category);
        }
    }

    @Override
    public TypeCategory getCategory() {       
        if (getType().getCategory() == TypeCategory.ENTITY || !properties.isEmpty()) {
            return TypeCategory.ENTITY;    
        } else {
            return TypeCategory.CUSTOM;
        }        
    }

    public Set<Constructor> getConstructors() {
        return constructors;
    }

    public Map<Object, Object> getData() {
        return data;
    }

    public Set<Delegate> getDelegates() {
        return delegates;
    }

    public TypeCategory getOriginalCategory() {
        return super.getCategory();
    }

    public Set<Property> getProperties() {
        return properties;
    }
    
    @Nullable
    public Supertype getSuperType() {
        return superTypes.size() == 1 ? superTypes.iterator().next() : null;
    }

    public Set<Supertype> getSuperTypes() {
        return superTypes;
    }

    public String getUncapSimpleName() {
        return uncapSimpleName;
    }

    @Override
    public int hashCode() {
        return getFullName().hashCode();
    }
    
    public boolean hasArrays() {
        return hasPropertyWithType(TypeCategory.ARRAY);
    }
    
    public boolean hasEntityFields() {
        return hasPropertyWithType(TypeCategory.ENTITY);
    }
    
    public boolean hasInits() {
        for (Property property : properties) {
            if (!property.getInits().isEmpty()) {
                return true;
            }
        }
        return false;        
    }
    
    public boolean hasLists() {
        return hasPropertyWithType(TypeCategory.LIST);
    }
    
    public boolean hasCollections() {
        return hasPropertyWithType(TypeCategory.COLLECTION);
    }
    
    public boolean hasSets() {
        return hasPropertyWithType(TypeCategory.SET);
    }
    
    public boolean hasMaps() {
        return hasPropertyWithType(TypeCategory.MAP);
    }
    
    private boolean hasPropertyWithType(TypeCategory category) {
        for (Property property : properties) {
            if (property.getType().getCategory() == category) {
                return true;
            }
        }
        return false;
    }

    public void include(Supertype supertype) {
        EntityType entityType = supertype.getEntityType();
        for (Delegate delegate : entityType.getDelegates()) {
            addDelegate(delegate);
        }
        for (Property property : entityType.getProperties()) {
            addProperty(property.createCopy(this));
        }
    }
    
    private Property validateField(Property field) {
        if (field.getName().equals(uncapSimpleName) || field.getEscapedName().equals(uncapSimpleName)) {
            do {
                uncapSimpleName = StringUtils.uncapitalize(getType().getSimpleName()) + (escapeSuffix++);
            } while (propertyNames.contains(uncapSimpleName));
        }
        return field;
    }

    public Set<String> getPropertyNames() {
        return propertyNames;
    }

    public Set<String> getEscapedPropertyNames() {
        return escapedPropertyNames;
    }
    
    public Type getInnerType() {
        return type;
    }
}
