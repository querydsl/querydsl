/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.mysema.codegen.JavaWriter;

/**
 * SerializerTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class SerializerTest {
    
    private EntityType type;

    private Writer writer = new StringWriter();

    private TypeMappings typeMappings = new TypeMappings();
    
    @Before
    public void setUp(){
        TypeFactory typeFactory = new TypeFactory();
        
        // type
        Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false);
        type = new EntityType("Q", typeModel);
         
        // property
        Property property = new Property(type, "field", typeFactory.create(String.class), new String[0]);
        type.addProperty(property);
        
        // constructor
        Parameter param = new Parameter("name", new ClassType(TypeCategory.STRING, String.class));
        type.addConstructor(new Constructor(Collections.singleton(param)));
        
        // method
        Method method = new Method(typeFactory.create(String.class), "method", "abc", typeFactory.create(String.class));
        type.addMethod(method);
    }

    @Test
    public void EntitySerializer() throws Exception {        
        new EntitySerializer(typeMappings).serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
    }
    
    @Test
    public void EmbeddableSerializer() throws Exception {        
        new EmbeddableSerializer(typeMappings).serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
    }
    
    @Test
    public void SupertypeSerializer() throws IOException{
        new SupertypeSerializer(typeMappings).serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
    }

    @Test
    public void DTOSerializer() throws IOException{
        new DTOSerializer(typeMappings).serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
    }
}
