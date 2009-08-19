/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * HibernateProcessorTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class SerializerTest {

    /** The type. */
    private ClassModel type;

    /** The writer. */
    private Writer writer = new StringWriter();

    /** The model. */
    private Map<String, Object> model = new HashMap<String, Object>();

    /**
     * Instantiates a new serializer test.
     */
    public SerializerTest() {
        type = new ClassModel(
                "com.mysema.query.DomainSuperClass",
                "com.mysema.query", 
                "com.mysema.query.DomainClass",
                "DomainClass");
        
        FieldModel field = new FieldModel(type, "field", new TypeModel(){
            @Override
            public FieldType getFieldType() {
                return FieldType.STRING;
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
        model.put("type", type);
        model.put("pre", "");
        model.put("include", "");
        model.put("package", "com.mysema.query");
        model.put("classSimpleName", "Test");

        // as outer classes
        Serializers.DOMAIN.serialize(model, writer);
//        System.out.println(writer);
    }

}
