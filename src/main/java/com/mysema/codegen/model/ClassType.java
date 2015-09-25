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

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.mysema.codegen.support.ClassUtils;

/**
 * @author tiwe
 *
 */
public class ClassType implements Type {

    private final TypeCategory category;

    private final Class<?> javaClass;

    private final String className;

    private final List<Type> parameters;

    private Type arrayType, componentType, enclosingType;

    public ClassType(Class<?> javaClass, Type... parameters) {
        this(TypeCategory.SIMPLE, javaClass, Arrays.asList(parameters));
    }

    public ClassType(TypeCategory category, Class<?> clazz, Type... parameters) {
        this(category, clazz, Arrays.asList(parameters));
    }

    public ClassType(TypeCategory category, Class<?> clazz, List<Type> parameters) {
        this.category = category;
        this.javaClass = clazz;
        this.parameters = parameters;
        this.className = ClassUtils.getFullName(javaClass);
    }

    
    @Override
    public Type as(TypeCategory c) {
        if (category == c) {
            return this;
        } else {
            return new ClassType(c, javaClass);
        }
    }

    @Override
    public Type asArrayType() {
        if (arrayType == null) {
            String fullName = ClassUtils.getFullName(javaClass) + "[]";
            String simpleName = javaClass.getSimpleName() + "[]";
            arrayType = new SimpleType(TypeCategory.ARRAY, fullName, getPackageName(), simpleName,
                    false, false);
        }
        return arrayType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Type) {
            Type t = (Type) o;
            return t.getFullName().equals(className) && t.getParameters().equals(parameters);
        } else {
            return false;
        }
    }

    public TypeCategory getCategory() {
        return category;
    }

    @Override
    public Type getComponentType() {
        Class<?> clazz = javaClass.getComponentType();
        if (clazz != null && componentType == null) {
            componentType = new ClassType(TypeCategory.SIMPLE, clazz);
        }
        return componentType;
    }

    @Override
    public Type getEnclosingType() {
        if (enclosingType == null) {
            Class<?> enclosingClass = javaClass.getEnclosingClass();
            if (enclosingClass != null) {
                enclosingType = new ClassType(enclosingClass);
            }
        }
        return enclosingType;
    }

    @Override
    public String getFullName() {
        return className;
    }

    @Override
    public String getGenericName(boolean asArgType) {
        return getGenericName(asArgType, Collections.singleton("java.lang"),
                Collections.<String> emptySet());
    }

    @Override
    public String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes) {
        if (parameters.isEmpty()) {
            return ClassUtils.getName(javaClass, packages, classes);
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(ClassUtils.getName(javaClass, packages, classes));
            builder.append("<");
            boolean first = true;
            for (Type parameter : parameters) {
                if (!first) {
                    builder.append(", ");
                }
                if (parameter == null || parameter.getFullName().equals(getFullName())) {
                    builder.append("?");
                } else {
                    builder.append(parameter.getGenericName(false, packages, classes));
                }
                first = false;
            }
            builder.append(">");
            return builder.toString();
        }
    }

    public Class<?> getJavaClass() {
        return javaClass;
    }

    @Override
    public String getPackageName() {
        return ClassUtils.getPackageName(javaClass);
    }

    @Override
    public List<Type> getParameters() {
        return parameters;
    }

    @Override
    public String getRawName(Set<String> packages, Set<String> classes) {
        return ClassUtils.getName(javaClass, packages, classes);
    }

    @Override
    public String getSimpleName() {
        return javaClass.getSimpleName();
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(javaClass.getModifiers());
    }

    @Override
    public boolean isPrimitive() {
        return javaClass.isPrimitive();
    }

    @Override
    public String toString() {
        return getGenericName(true);
    }

}
