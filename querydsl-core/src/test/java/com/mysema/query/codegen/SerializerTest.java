/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;

import javax.annotation.Nullable;

import org.junit.Test;

/**
 * SerializerTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class SerializerTest {

    private ClassModel type;

    private Writer writer = new StringWriter();

    /**
     * Instantiates a new serializer test.
     */
    public SerializerTest() {
        type = new ClassModel(
                "Q",
                "com.mysema.query.DomainSuperClass",
                "com.mysema.query", 
                "com.mysema.query.DomainClass",
                "DomainClass");
        
        FieldModel field = new FieldModel(type, "field", new TypeModel(){
            @Override
            public TypeCategory getTypeCategory() {
                return TypeCategory.STRING;
            }
            @Override
            public String getName() {
                return String.class.getName();
            }
            @Override
            @Nullable
            public TypeModel getKeyType() {
                return null;
            }
            @Override
            public String getPackageName() {
                return String.class.getPackage().getName();
            }
            @Override
            public String getSimpleName() {
                return String.class.getSimpleName();
            }
            @Override
            public String getLocalName() {
                return String.class.getSimpleName();
            }
            @Override
            @Nullable
            public TypeModel getValueType() {
                return null;
            }}, "field");
        type.addField(field);
        ParameterModel param = new ParameterModel("name", "java.lang.String");
        type.addConstructor(new ConstructorModel(Collections.singleton(param)));
    }

    /**
     * Test domain types as outer classes.
     * 
     * @throws Exception the exception
     */
    @Test
    public void testDomainTypesAsOuterClasses() throws Exception {
        Serializers.DOMAIN.serialize(type, writer);
//        System.out.println(writer);
    }

}
