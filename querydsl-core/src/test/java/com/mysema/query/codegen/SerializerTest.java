/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;

import org.junit.Test;

/**
 * SerializerTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class SerializerTest {
    
    private EntityModel type;

    private Writer writer = new StringWriter();

    public SerializerTest() {
        TypeModelFactory typeFactory = new TypeModelFactory();
        TypeModel typeModel = new SimpleTypeModel(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false);
        type = new EntityModel("Q", typeModel, Collections.singleton("com.mysema.query.DomainSuperClass"));
        
        PropertyModel field = new PropertyModel(type, "field", typeFactory.create(String.class), new String[0]);
        type.addProperty(field);
        ParameterModel param = new ParameterModel("name", new ClassTypeModel(TypeCategory.STRING, String.class));
        type.addConstructor(new ConstructorModel(Collections.singleton(param)));
    }

    @Test
    public void testDomainTypesAsOuterClasses() throws Exception {
        TypeMappings typeMappings = new TypeMappings();
        new EntitySerializer(typeMappings).serialize(type, SimpleSerializerConfig.DEFAULT, writer);
//        System.out.println(writer);
    }

}
