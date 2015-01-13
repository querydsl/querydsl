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

import javax.inject.Inject;
import javax.inject.Named;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;

/**
 * QueryTypeFactoryImpl is the default implementation of the {@link QueryTypeFactory} interface 
 * 
 * @author tiwe
 *
 */
public class QueryTypeFactoryImpl implements QueryTypeFactory {
    
    private final String prefix, suffix, packageSuffix;
    
    @Inject
    public QueryTypeFactoryImpl(
            @Named(CodegenModule.PREFIX) String prefix, 
            @Named(CodegenModule.SUFFIX) String suffix, 
            @Named(CodegenModule.PACKAGE_SUFFIX) String packageSuffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.packageSuffix = packageSuffix;
    }
    
    @Override
    public Type create(Type type) {
        if (type.getPackageName().isEmpty()) {
            return createWithoutPackage(type);
        } else {
            return createWithPackage(type);
        }
    }
    
    private Type createWithPackage(Type type) {
        String packageName = type.getPackageName();
        String simpleName = prefix + normalizeName(type.getFullName()
                .substring(packageName.length()+1)) + suffix;        
        packageName = (packageName.startsWith("java") ? "ext." : "") 
                + packageName + packageSuffix; 
        return new SimpleType(type.getCategory(), packageName+"."+simpleName, 
                packageName, simpleName, false, false);
    }
    
    private Type createWithoutPackage(Type type) {
        String simpleName = prefix + normalizeName(type.getFullName()) + suffix;
        return new SimpleType(type.getCategory(), simpleName, "", simpleName, false, false);
    }
    
    private String normalizeName(String name) {
        return name.replace('.', '_').replace('$', '_');
    }

    
}
