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

import com.mysema.util.JavaWriter;

/**
 * SerializerTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class SerializerTest {
    
    private EntityType type;

    private Writer writer = new StringWriter();

    public SerializerTest() {
        TypeFactory typeFactory = new TypeFactory();
        Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false);
        type = new EntityType("Q", typeModel);
        
        Property field = new Property(type, "field", typeFactory.create(String.class), new String[0]);
        type.addProperty(field);
        Parameter param = new Parameter("name", new ClassType(TypeCategory.STRING, String.class));
        type.addConstructor(new Constructor(Collections.singleton(param)));
    }

    @Test
    public void testDomainTypesAsOuterClasses() throws Exception {
        TypeMappings typeMappings = new TypeMappings();
        new EntitySerializer(typeMappings).serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
//        System.out.println(writer);
    }

}
