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
package com.mysema.query.sql.codegen;

import com.mysema.query.codegen.CodegenModule;
import com.mysema.query.codegen.QueryTypeFactory;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.sql.Configuration;

import java.util.Set;

/**
 * SQLCodegenModule is a dependency injection module with codegen configuration
 * 
 * @author tiwe
 *
 */
public class SQLCodegenModule extends CodegenModule{
    
    public static final String BEAN_SERIALIZER = "beanSerializer";
    
    public static final String BEAN_SUFFIX = "beanSuffix";

    public static final String BEAN_PREFIX = "beanPrefix";
    
    public static final String BEAN_PACKAGE_NAME = "beanPackageName";
    
    public static final String PACKAGE_NAME = "packageName";

    public static final String INNER_CLASSES_FOR_KEYS = "innerClassesForKeys";
    
    public static final String SCHEMA_TO_PACKAGE = "schemaToPackage";
    
    @Override
    protected void configure() {
        super.configure();
        bind(NamingStrategy.class, DefaultNamingStrategy.class);
        bind(Configuration.class, Configuration.DEFAULT);
        bind(Serializer.class, MetaDataSerializer.class);
        bind(QueryTypeFactory.class, SQLQueryTypeFactory.class);
        
        bind(INNER_CLASSES_FOR_KEYS, false);
        bind(BEAN_PREFIX, "");
        bind(BEAN_SUFFIX, "");        
        bind(BEAN_PACKAGE_NAME, "com.example");
        bind(PACKAGE_NAME, "com.example");
        bind(BEAN_SERIALIZER, (Class<?>)null);
        bind(SCHEMA_TO_PACKAGE, false);
    }
    
    public String getPrefix() {
        return get(String.class, PREFIX);
    }
    
    public String getSuffix() {
        return get(String.class, SUFFIX);
    }
    
    public String getBeanPrefix() {
        return get(String.class, BEAN_PREFIX);
    }
    
    public String getBeanSuffix() {
        return get(String.class, BEAN_SUFFIX);
    }
    
    public String getPackageName() {
        return get(String.class, PACKAGE_NAME);
    }

	public Set<String> getImports() {
		return get(Set.class, IMPORTS);
	}
}
