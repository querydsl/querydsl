/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.codegen.ConstructorModel;
import com.mysema.query.codegen.FieldModel;
import com.mysema.query.codegen.FieldType;
import com.mysema.query.codegen.Parameter;
import com.mysema.query.codegen.Serializers;
import com.mysema.query.codegen.ClassModel;

/**
 * HibernateProcessorTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class GeneralProcessorTest {

    private ClassModel type;

    private Writer writer = new StringWriter();

    private Map<String, Object> model = new HashMap<String, Object>();

    public GeneralProcessorTest() {
        type = new ClassModel("com.mysema.query.DomainSuperClass",
                "com.mysema.query", "com.mysema.query.DomainClass",
                "DomainClass");

        FieldModel field = new FieldModel("field", null, "java.lang",
                "java.lang.String", "String", FieldType.STRING);
        type.addField(field);
        Parameter param = new Parameter("name", "java.lang.String");
        type.addConstructor(new ConstructorModel(Collections.singleton(param)));
    }

    @Test
    public void testDomainTypesAsOuterClasses() throws Exception {
        model.put("type", type);
        model.put("pre", "");
        model.put("include", "");
        model.put("package", "com.mysema.query");
        model.put("classSimpleName", "Test");

        // as outer classes
        Serializers.DOMAIN.serialize(model, writer);
    }

}
