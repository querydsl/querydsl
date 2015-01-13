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
package com.querydsl.sql.codegen;

import javax.inject.Inject;
import javax.inject.Named;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.querydsl.codegen.QueryTypeFactory;

/**
 * SQLQueryTypeFactory is a QueryTypeFactory implementation with configuration data from this module
 * 
 * @author tiwe
 *
 */
public final class SQLQueryTypeFactory implements QueryTypeFactory{

    private final String packageName, beanPackageName;
    
    private final int stripStart, stripEnd;

    private final String prefix, suffix;
    
    private final boolean replacePackage;
    
    @Inject
    public SQLQueryTypeFactory(
            @Named(SQLCodegenModule.PACKAGE_NAME) String packageName,
            @Named(SQLCodegenModule.BEAN_PACKAGE_NAME) String beanPackageName,
            @Named(SQLCodegenModule.BEAN_PREFIX) String beanPrefix,
            @Named(SQLCodegenModule.BEAN_SUFFIX) String beanSuffix,
            @Named(SQLCodegenModule.PREFIX) String prefix,
            @Named(SQLCodegenModule.SUFFIX) String suffix) {
        this.packageName = packageName;
        this.beanPackageName = beanPackageName;
        this.replacePackage = !packageName.equals(beanPackageName);
        this.stripStart = beanPrefix.length();
        this.stripEnd = beanSuffix.length();
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public Type create(Type type) {
        String packageName = type.getPackageName();
        if (replacePackage) {
            packageName = this.packageName + packageName.substring(beanPackageName.length());
        }
        String simpleName = type.getSimpleName();        
        simpleName = prefix + simpleName.substring(stripStart, simpleName.length()-stripEnd) + suffix;
        return new SimpleType(packageName + "." + simpleName, packageName, simpleName);
    }


}
