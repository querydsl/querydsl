package com.mysema.query.codegen;

import com.mysema.util.AbstractModule;

/**
 * @author tiwe
 *
 */
public class CodegenModule  extends AbstractModule{

    public static final String PREFIX = "prefix";
    
    public static final String SUFFIX = "suffix";
    
    public static final String KEYWORDS = "keywords";
    
    public static final String PACKAGE_SUFFIX = "packageSuffix";
    
    @Override
    protected void configure() {
        bind(TypeMappings.class);
        bind(QueryTypeFactory.class, QueryTypeFactoryImpl.class);
        bind(EntitySerializer.class);
        bind(EmbeddableSerializer.class);
        bind(ProjectionSerializer.class);
        bind(SupertypeSerializer.class);
        
        bind(PREFIX, "Q");
        bind(SUFFIX, "");
        bind(PACKAGE_SUFFIX, "");
    }

}