/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

/**
 * 
 * @author tiwe
 *
 */
public final class Serializers {
    
    private static final Configuration configuration;

    static {
        configuration = new Configuration();
        configuration.setClassForTemplateLoading(Serializers.class, "/");
        configuration.setObjectWrapper(new DefaultObjectWrapper());
    }
    
    private Serializers(){}
    
    public static final Serializer DOMAIN = new Serializer(configuration,"/codegen/domain.ftl");
    
    public static final Serializer EMBEDDABLE = new Serializer(configuration,"/codegen/embeddable.ftl");
    
    public static final Serializer DTO = new Serializer(configuration,"/codegen/dto.ftl");
    
}
