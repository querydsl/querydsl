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

import com.querydsl.codegen.utils.StringUtils;
import com.querydsl.codegen.utils.model.Constructor;
import com.querydsl.codegen.utils.model.Type;
import com.querydsl.codegen.utils.model.TypeAdapter;
import com.querydsl.codegen.utils.model.TypeCategory;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * {@code EntityType} represents a model of a query domain type with properties
 *
 * @author tiwe
 */
public class EntityType extends TypeAdapter implements Comparable<EntityType> {

    private final Map<Class<?>,Annotation> annotations = new HashMap<Class<?>,Annotation>();

    private final Set<Constructor> constructors = new LinkedHashSet<Constructor>();

    private int escapeSuffix = 1;

    private final Set<Delegate> delegates = new HashSet<Delegate>();

    private final Set<Property> properties = new TreeSet<Property>();

    private final Set<String> propertyNames = new HashSet<String>();

    private final Set<String> escapedPropertyNames = new HashSet<String>();

    private final Set<Supertype> superTypes;

    private final Map<Object, Object> data = new HashMap<Object,Object>();

    private String modifiedSimpleName;

    /**
     * Create a new {@code EntityType} instance for the given type
     *
     * @param type
     */
    public EntityType(Type type) {
        this(type, new LinkedHashSet<Supertype>(), DefaultVariableNameFunction.INSTANCE);
    }

    /**
     * Create a new {@code EntityType} instance for the given type
     *
     * @param type the type to be used
     * @param variableNameFunction the variable name function to be used
     */
    public EntityType(Type type, Function<EntityType, String> variableNameFunction) {
        this(type, new LinkedHashSet<Supertype>(), variableNameFunction);
    }

    /**
     * Create a new {@code EntityType} instance for the given type and superTypes
     *
     * @param type
     * @param superTypes
     */
    public EntityType(Type type, Set<Supertype> superTypes) {
        this(type, superTypes, DefaultVariableNameFunction.INSTANCE);
    }

    /**
     * Create a new {@code EntityType} instance for the given type and superTypes
     *
     * @param type the type to be used
     * @param superTypes the super types to be used
     * @param variableNameFunction the variable name function to be used
     */
    private EntityType(Type type, Set<Supertype> superTypes, Function<EntityType, String> variableNameFunction) {
        super(type);
        this.superTypes = superTypes;
        this.modifiedSimpleName = variableNameFunction.apply(this);
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
            return getFullName().equals(((Type) o).getFullName());
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

    /**
     * Use {@link #getModifiedSimpleName()}
     */
    @Deprecated
    public String getUncapSimpleName() {
        return modifiedSimpleName;
    }

    public String getModifiedSimpleName() {
        return modifiedSimpleName;
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
        if (field.getName().equals(modifiedSimpleName) || field.getEscapedName().equals(modifiedSimpleName)) {
            do {
                modifiedSimpleName = StringUtils.uncapitalize(getType().getSimpleName()) + (escapeSuffix++);
            } while (propertyNames.contains(modifiedSimpleName));
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
