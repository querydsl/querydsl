package com.mysema.query.sql;

import com.mysema.query.codegen.QueryTypeFactory;
import com.mysema.query.codegen.QueryTypeFactoryImpl;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.util.AbstractModule;

public class CodegenModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(NamingStrategy.class, DefaultNamingStrategy.class);
        bind(Configuration.class, Configuration.DEFAULT);
        bind(TypeMappings.class);
        bind(QueryTypeFactory.class, QueryTypeFactoryImpl.class);
        bind(Serializer.class, MetaDataSerializer.class);
        
        bind("innerClassesForKeys", false);
        bind("prefix", "Q");
        bind("suffix", "");
        bind("beanPrefix", "");
        bind("beanSuffix", "");
    }

}
