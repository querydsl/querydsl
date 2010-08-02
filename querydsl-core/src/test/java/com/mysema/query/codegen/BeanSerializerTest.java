package com.mysema.query.codegen;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.mysema.codegen.JavaWriter;

public class BeanSerializerTest {
    
    private EntityType type;

    private Writer writer = new StringWriter();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp(){
        TypeFactory typeFactory = new TypeFactory();

        // type
        Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false);
        type = new EntityType("Q", typeModel);

        // property
        type.addProperty(new Property(type, "entityField", type, new String[0]));
        type.addProperty(new Property(type, "collection", new ClassType(TypeCategory.COLLECTION, Collection.class, typeModel), new String[0]));
        type.addProperty(new Property(type, "listField", new ClassType(TypeCategory.LIST, List.class, typeModel), new String[0]));
        type.addProperty(new Property(type, "setField", new ClassType(TypeCategory.SET, Set.class, typeModel), new String[0]));
        type.addProperty(new Property(type, "arrayField", new ClassType(TypeCategory.ARRAY, String[].class), new String[0]));
        type.addProperty(new Property(type, "mapField", new ClassType(TypeCategory.MAP, List.class, typeModel, typeModel), new String[0]));
        type.addProperty(new Property(type, "superTypeField", new TypeExtends(new ClassType(TypeCategory.MAP, List.class, typeModel, typeModel)), new String[0]));
        type.addProperty(new Property(type, "extendsTypeField", new TypeSuper(new ClassType(TypeCategory.MAP, List.class, typeModel, typeModel)), new String[0]));

        for (Class<?> cl : Arrays.asList(Boolean.class, Comparable.class, Integer.class, Date.class, java.sql.Date.class, java.sql.Time.class)){
            Type classType = new ClassType(TypeCategory.get(cl.getName()), cl);
            type.addProperty(new Property(type, StringUtils.uncapitalize(cl.getSimpleName()), classType, new String[0]));
        }

        // constructor
        Parameter firstName = new Parameter("firstName", new ClassType(TypeCategory.STRING, String.class));
        Parameter lastName = new Parameter("lastName", new ClassType(TypeCategory.STRING, String.class));
        type.addConstructor(new Constructor(Arrays.asList(firstName, lastName)));

        // method
        Method method = new Method(typeFactory.create(String.class), "method", "abc", typeFactory.create(String.class));
        type.addMethod(method);
    }

    @Test
    public void test() throws IOException{
        BeanSerializer serializer = new BeanSerializer();
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        String str = writer.toString();
        for (String prop : Arrays.asList(
                "String[] arrayField;",
                "Boolean boolean_;",
                "java.util.Collection<DomainClass> collection;",
                "Comparable comparable;",
                "java.util.Date date;",
                "DomainClass entityField;",
                "Object extendsTypeField;",
                "Integer integer;",
                "java.util.List<DomainClass> listField;",
                "java.util.List<DomainClass,DomainClass> mapField;",
                "java.util.Set<DomainClass> setField;",
                "java.util.List<DomainClass,DomainClass> superTypeField;",
                "java.sql.Time time;")){
            assertTrue(prop + " was not contained", str.contains(prop));
        }
    }
    
}
