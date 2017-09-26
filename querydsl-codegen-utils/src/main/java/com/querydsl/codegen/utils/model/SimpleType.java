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
package com.querydsl.codegen.utils.model;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author tiwe
 */
public class SimpleType implements Type {

    private static final Map<String, Class<?>> PRIMITIVES = new HashMap<String, Class<?>>();
    
    static {
        for (Class<?> cl : Arrays.<Class<?>>asList(byte.class, int.class, long.class, short.class,
                float.class, double.class, boolean.class, char.class)) {
            PRIMITIVES.put(cl.getName(), cl);
        }
    }
    
    private final TypeCategory category;

    private final String fullName, outerClassName, packageName, simpleName, localName;

    private final List<Type> parameters;

    private final boolean primitiveClass, finalClass, memberClass;

    private Type arrayType, componentType, enclosingType;
    
    private transient Class<?> javaClass;

    public SimpleType(String fullName, String packageName, String simpleName, Type... parameters) {
        this(TypeCategory.SIMPLE, fullName, packageName, simpleName, false, false, Arrays
                .asList(parameters));
    }

    public SimpleType(String simpleName) {
        this(TypeCategory.SIMPLE, simpleName, "", simpleName, false, false);
    }

    public SimpleType(Type type, List<Type> parameters) {
        this(type.getCategory(), type.getFullName(), type.getPackageName(), type.getSimpleName(),
                type.isPrimitive(), type.isFinal(), parameters);
    }

    public SimpleType(Type type, Type... parameters) {
        this(type.getCategory(), type.getFullName(), type.getPackageName(), type.getSimpleName(),
                type.isPrimitive(), type.isFinal(), Arrays.asList(parameters));
    }

    public SimpleType(TypeCategory category, String fullName, String packageName,
            String simpleName, boolean primitiveClass, boolean finalClass, List<Type> parameters) {
        this.category = category;
        this.fullName = fullName;
        this.packageName = packageName;
        this.simpleName = simpleName;
        if (packageName.length() > 0 && fullName.contains(".")) {
            this.localName = fullName.substring(packageName.length() + 1);
        } else {
            this.localName = fullName;
        }
        if (localName.contains(".")) {
            this.outerClassName = fullName.substring(0, fullName.lastIndexOf('.'));
        } else {
            this.outerClassName = fullName;
        }
        this.primitiveClass = primitiveClass;
        this.finalClass = finalClass;
        this.parameters = parameters;
        this.memberClass = localName.contains(".");
    }

    public SimpleType(TypeCategory typeCategory, String fullName, String packageName,
            String simpleName, boolean p, boolean f, Type... parameters) {
        this(typeCategory, fullName, packageName, simpleName, p, f, Arrays.asList(parameters));
    }

    @Override
    public Type as(TypeCategory c) {
        if (category != c) {
            return new SimpleType(c, fullName, packageName, simpleName, primitiveClass, finalClass,
                    parameters);
        } else {
            return this;
        }
    }

    @Override
    public Type asArrayType() {
        if (arrayType == null) {
            String newFullName = getFullName() + "[]";
            String newSimpleName = getSimpleName() + "[]";
            arrayType = new SimpleType(TypeCategory.ARRAY, newFullName, getPackageName(),
                    newSimpleName, false, false);
        }
        return arrayType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Type) {
            Type t = (Type) o;
            return t.getFullName().equals(fullName) && t.getParameters().equals(parameters);
        } else {
            return false;
        }
    }

    public TypeCategory getCategory() {
        return category;
    }

    @Override
    public Type getComponentType() {
        if (fullName.endsWith("[]")) {
            if (componentType == null) {
                String newFullName = fullName.substring(0, fullName.length() - 2);
                String newSimpleName = simpleName.substring(0, simpleName.length() - 2);
                componentType = new SimpleType(TypeCategory.SIMPLE, newFullName, getPackageName(),
                        newSimpleName, false, false);
            }
            return componentType;
        } else {
            return null;
        }
    }

    @Override
    public Type getEnclosingType() {
        if (enclosingType == null && memberClass) {
            String newLocalName = localName.substring(0, localName.lastIndexOf('.'));
            String newSimpleName = newLocalName.substring(newLocalName.lastIndexOf('.') + 1);
            enclosingType = new SimpleType(outerClassName, packageName, newSimpleName);
        }
        return enclosingType;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getGenericName(boolean asArgType) {
        return getGenericName(asArgType, Collections.singleton("java.lang"),
                Collections.<String> emptySet());
    }

    @Override
    public String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes) {
        if (parameters.isEmpty()) {
            return getRawName(packages, classes);
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(getRawName(packages, classes));
            builder.append("<");
            boolean first = true;
            for (Type parameter : parameters) {
                if (!first) {
                    builder.append(", ");
                }
                if (parameter == null || parameter.getFullName().equals(fullName)) {
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

    @Override
    public Class<?> getJavaClass() {
        if (javaClass == null) {
            String className;
            if (packageName.length() > 0) {
                className = packageName + "." + localName.replace('.', '$');
            } else {
                className = localName.replace('.', '$');
            }
            try {
                if (className.endsWith("[]")) {
                    Class<?> component = getComponentType().getJavaClass();
                    javaClass = Array.newInstance(component, 0).getClass();
                } else if (PRIMITIVES.containsKey(className)) {
                    javaClass = PRIMITIVES.get(className);
                } else {
                    javaClass = Class.forName(className);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return javaClass;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public List<Type> getParameters() {
        return parameters;
    }

    @Override
    public String getRawName(Set<String> packages, Set<String> classes) {
        if (classes.contains(fullName)) {
            return simpleName;
        } else if (classes.contains(outerClassName)) {
            return fullName.substring(outerClassName.lastIndexOf('.') + 1);
        } else if (packages.contains(packageName)) {
            return localName;
        } else {
            return fullName;
        }
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

    @Override
    public boolean isFinal() {
        return finalClass;
    }

    @Override
    public boolean isPrimitive() {
        return primitiveClass;
    }

    @Override
    public boolean isMember() {
        return memberClass;
    }

    @Override
    public String toString() {
        return getGenericName(true);
    }

}
