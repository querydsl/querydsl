/*
 * Copyright 2010, Mysema Ltd
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
package com.mysema.codegen.model;

import java.util.List;
import java.util.Set;

/**
 * TypeAdapter is a basic adapter implementation for the Type interface
 * 
 * @author tiwe
 * 
 */
public class TypeAdapter implements Type {

    protected final Type type;

    public TypeAdapter(Type type) {
        this.type = type;
    }

    @Override
    public Type as(TypeCategory category) {
        return type.as(category);
    }

    @Override
    public Type asArrayType() {
        return type.asArrayType();
    }

    @Override
    public Type getComponentType() {
        return type.getComponentType();
    }

    @Override
    public Type getEnclosingType() {
        return type.getEnclosingType();
    }

    @Override
    public boolean equals(Object o) {
        return type.equals(o);
    }

    @Override
    public TypeCategory getCategory() {
        return type.getCategory();
    }

    @Override
    public String getFullName() {
        return type.getFullName();
    }

    @Override
    public String getGenericName(boolean asArgType) {
        return type.getGenericName(asArgType);
    }

    @Override
    public String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes) {
        return type.getGenericName(asArgType, packages, classes);
    }

    @Override
    public Class<?> getJavaClass() {
        return type.getJavaClass();
    }

    @Override
    public String getPackageName() {
        return type.getPackageName();
    }

    @Override
    public List<Type> getParameters() {
        return type.getParameters();
    }

    @Override
    public String getRawName(Set<String> packages, Set<String> classes) {
        return type.getRawName(packages, classes);
    }

    @Override
    public String getSimpleName() {
        return type.getSimpleName();
    }

    protected Type getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public boolean isFinal() {
        return type.isFinal();
    }

    @Override
    public boolean isPrimitive() {
        return type.isPrimitive();
    }

    @Override
    public String toString() {
        return type.toString();
    }

}
