package com.mysema.query.sql;

import com.mysema.query.codegen.CodegenModule;
import com.mysema.query.codegen.QueryTypeFactory;
import com.mysema.query.codegen.Serializer;

public class SQLCodegenModule extends CodegenModule{
    
    public static final String BEAN_SUFFIX = "beanSuffix";

    public static final String BEAN_PREFIX = "beanPrefix";
    
    public static final String PACKAGE_NAME = "packageName";
    
    public static final String INNER_CLASSES_FOR_KEYS = "innerClassesForKeys";
    
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
        bind(PACKAGE_NAME, "com.example");
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

}
