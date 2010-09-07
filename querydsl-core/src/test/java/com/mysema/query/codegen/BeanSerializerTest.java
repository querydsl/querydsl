/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Constructor;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;

public class BeanSerializerTest {
    
    private EntityType type;

    private Writer writer = new StringWriter();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp(){
        // type
        Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false,false);
        type = new EntityType("Q", typeModel);

        // property
        type.addProperty(new Property(type, "entityField", type));
        type.addProperty(new Property(type, "collection", new SimpleType(Types.COLLECTION, typeModel)));
        type.addProperty(new Property(type, "listField", new SimpleType(Types.LIST, typeModel)));
        type.addProperty(new Property(type, "setField", new SimpleType(Types.SET, typeModel)));
        type.addProperty(new Property(type, "arrayField", new ClassType(TypeCategory.ARRAY, String[].class)));
        type.addProperty(new Property(type, "mapField", new SimpleType(Types.MAP, typeModel, typeModel)));

        for (Class<?> cl : Arrays.asList(Boolean.class, Comparable.class, Integer.class, Date.class, java.sql.Date.class, java.sql.Time.class)){
            Type classType = new ClassType(TypeCategory.get(cl.getName()), cl);
            type.addProperty(new Property(type, StringUtils.uncapitalize(cl.getSimpleName()), classType));
        }

        // constructor
        Parameter firstName = new Parameter("firstName", new ClassType(TypeCategory.STRING, String.class));
        Parameter lastName = new Parameter("lastName", new ClassType(TypeCategory.STRING, String.class));
        type.addConstructor(new Constructor(Arrays.asList(firstName, lastName)));

        // method
        Method method = new Method(Types.STRING, "method", "abc", Types.STRING);
        type.addMethod(method);
    }

    @Test
    public void test() throws IOException{
        BeanSerializer serializer = new BeanSerializer();
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        String str = writer.toString();
        System.err.println(str);
        for (String prop : Arrays.asList(
                "String[] arrayField;",
                "Boolean boolean_;",
                "java.util.Collection<DomainClass> collection;",
                "Comparable comparable;",
                "java.util.Date date;",
                "DomainClass entityField;",
//                "Object extendsTypeField;",
                "Integer integer;",
                "List<DomainClass> listField;",
                "Map<DomainClass, DomainClass> mapField;",
                "Set<DomainClass> setField;",
//                "List<DomainClass, DomainClass> superTypeField;",
                "java.sql.Time time;")){
            assertTrue(prop + " was not contained", str.contains(prop));
        }
    }
    
}
