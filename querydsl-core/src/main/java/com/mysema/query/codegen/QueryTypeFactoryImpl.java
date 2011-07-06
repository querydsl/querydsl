/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import javax.inject.Inject;
import javax.inject.Named;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;

public class QueryTypeFactoryImpl implements QueryTypeFactory {
    
    private final String prefix, suffix, packageSuffix;
    
    @Inject
    public QueryTypeFactoryImpl(
            @Named("prefix") String prefix, 
            @Named("suffix") String suffix, 
            @Named("packageSuffix") String packageSuffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.packageSuffix = packageSuffix;
    }
    
    @Override
    public Type create(Type type){
        if (type.getPackageName().isEmpty()){
            return createWithoutPackage(type);
        }else{
            return createWithPackage(type);
        }
    }
    
    private Type createWithPackage(Type type){
        String packageName = type.getPackageName();
        String simpleName = prefix + normalizeName(type.getFullName().substring(packageName.length()+1)) + suffix;        
        packageName = (packageName.startsWith("java") ? "ext." : "") + packageName + packageSuffix; 
        return new SimpleType(type.getCategory(), packageName+"."+simpleName, packageName, simpleName, false, false);
    }
    
    private Type createWithoutPackage(Type type){
        String simpleName = prefix + normalizeName(type.getFullName()) + suffix;
        return new SimpleType(type.getCategory(), simpleName, "", simpleName, false, false);
    }
    
    private String normalizeName(String name){
        return name.replace('.', '_').replace('$', '_');
    }

    
}
