/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * @author tiwe
 *
 * @param <T>
 */
public class SimpleType implements Type {

    private final TypeCategory category;
    
    private final String fullName, packageName, simpleName, localName;
    
    private final List<Type> parameters;
    
    private final boolean primitiveClass, finalClass;

    public SimpleType(Type type, Type... parameters) {
        this(type.getCategory(), type.getFullName(), type.getPackageName(), type.getSimpleName(), 
            type.isPrimitive(), type.isFinal(), Arrays.asList(parameters));
    }
    
    public SimpleType(TypeCategory category, String fullName, String packageName, String simpleName, 
            boolean primitiveClass, boolean finalClass,
            List<Type> parameters) {
        this.category = category;
        this.fullName = fullName;
        this.packageName = packageName;
        this.simpleName = simpleName;
        if (packageName.length() > 0){
            this.localName = fullName.substring(packageName.length()+1);    
        }else{
            this.localName = fullName;
        }        
        this.primitiveClass = primitiveClass;
        this.finalClass = finalClass;
        this.parameters = parameters;
    }

    public SimpleType(TypeCategory typeCategory, String fullName, String packageName, String simpleName, 
            boolean p, boolean f, Type... parameters) {
        this(typeCategory, fullName, packageName, simpleName, p, f, Arrays
                .asList(parameters));
    }

    @Override
    public Type as(TypeCategory c) {
        if (category != c){
            return new SimpleType(c, fullName, packageName, simpleName, primitiveClass, finalClass, parameters); 
        }else{
            return this;
        }
    }
    
    @Override
    public Type asArrayType() {
        String fullName = getFullName()+"[]";
        String simpleName = getSimpleName()+"[]";
        return new SimpleType(TypeCategory.ARRAY, fullName, getPackageName(), simpleName, false, false);
    }
    

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof Type){
            Type t = (Type)o;
            return t.getFullName().equals(fullName) && t.getParameters().equals(parameters);
        }else{
            return false;
        }
    }
    
    public TypeCategory getCategory() {
        return category;
    }
    
    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getGenericName(boolean asArgType) {
        return getGenericName(asArgType, Collections.<String>emptySet(), Collections.<String>emptySet());
    }

    @Override
    public String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes) {
        if (parameters.isEmpty()){
            return getRawName(packages, classes);
        }else{
            StringBuilder builder = new StringBuilder();
            builder.append(getRawName(packages, classes));
            builder.append("<");
            boolean first = true;
            for (Type parameter : parameters){                
                if (!first){
                    builder.append(",");
                }
                if (parameter == null || parameter.equals(this)){
                    builder.append("?");
                }else{
                    builder.append(parameter.getGenericName(true, packages, classes));    
                }                
                first = false;
            }
            builder.append(">");
            return builder.toString();
        }
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
    public String getPrimitiveName() {
        return null;
    }

    @Override
    public String getRawName(Set<String> packages, Set<String> classes) {
        if (packages.contains(packageName) || classes.contains(fullName)){
            return localName;
        }else{
            return fullName;
        }
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public int hashCode(){
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
    
    public String toString(){
        return getGenericName(true);
    }
    
}
