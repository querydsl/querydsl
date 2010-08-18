/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import com.mysema.codegen.support.ClassUtils;

/**
 * @author tiwe
 *
 * @param <T>
 */
@Immutable
public class ClassType<T> implements Type {

    private final TypeCategory category;
    
    private final Class<T> javaClass;
    
    private final List<Type> parameters;
    
    @Nullable
    private final Class<?> primitiveClass;
    

    public ClassType(Class<T> javaClass) {
        this(TypeCategory.SIMPLE, javaClass, null, Collections.<Type>emptyList());
    }
    
    public ClassType(TypeCategory category, Class<T> javaClass, List<Type> parameters) {
        this(category, javaClass, null, parameters);
    }    
    
    public ClassType(TypeCategory category, Class<T> clazz, Type... parameters) {
        this(category, clazz, null, Arrays.asList(parameters));
    }
    
    public ClassType(TypeCategory category, Class<T> javaClass, Class<?> primitiveClass) {
        this(category, javaClass, primitiveClass, Collections.<Type>emptyList());
    }
        
    public ClassType(TypeCategory category, Class<T> javaClass, @Nullable Class<?> primitiveClass, List<Type> parameters) {
        this.category = category;
        this.javaClass = javaClass;
        this.primitiveClass = primitiveClass;
        this.parameters = parameters;
    }
        
    @Override
    public Type as(TypeCategory c) {
        if (category == c){
            return this;
        }else{
            return new ClassType<T>(c, javaClass);
        }
    }

    @Override
    public Type asArrayType() {
        String fullName = javaClass.getName()+"[]";
        String simpleName = javaClass.getSimpleName()+"[]";
        return new SimpleType(TypeCategory.ARRAY, fullName, getPackageName(), simpleName, false, false);
    }
    

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof Type){
            Type t = (Type)o;
            return t.getFullName().equals(javaClass.getName()) && t.getParameters().equals(parameters);
        }else{
            return false;
        }
    }
    
    public TypeCategory getCategory() {
        return category;
    }
    
    @Override
    public String getFullName() {
        return javaClass.getName();
    }

    @Override
    public String getGenericName(boolean asArgType) {
        return getGenericName(asArgType, Collections.<String>emptySet(), Collections.<String>emptySet());
    }

    @Override
    public String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes) {
        if (parameters.isEmpty()){
            return ClassUtils.getName(javaClass, packages, classes);
        }else{
            StringBuilder builder = new StringBuilder();
            builder.append(ClassUtils.getName(javaClass, packages, classes));
            builder.append("<");
            boolean first = true;
            for (Type parameter : parameters){                
                if (!first){
                    builder.append(",");
                }
                if (parameter == null || parameter.getFullName().equals(getFullName())){
                    builder.append("?");
                }else{
                    builder.append(parameter.getGenericName(false, packages, classes));    
                }                
                first = false;
            }
            builder.append(">");
            return builder.toString();
        }
    }

    public Class<T> getJavaClass() {
        return javaClass;
    }

    @Override
    public String getPackageName() {
        return javaClass.getPackage().getName();
    }

    @Override
    public List<Type> getParameters() {
        return parameters;
    }

    @Override
    public String getPrimitiveName() {
        return primitiveClass != null ? primitiveClass.getName() : null;
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
    public int hashCode(){
        return javaClass.getName().hashCode();
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(javaClass.getModifiers());
    }
    
    @Override
    public boolean isPrimitive() {
//        return javaClass.isPrimitive();
        return primitiveClass != null;
    }

    public String toString(){
        return getGenericName(true);
    }

    
}
