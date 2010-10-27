package com.mysema.query.codegen;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import org.junit.Test;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.Constructor;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;


public class ProjectionSerializerTest {
    
    @Test
    public void Constructors() throws IOException{
        Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false,false);
        EntityType type = new EntityType("Q", typeModel);

        // constructor
        Parameter firstName = new Parameter("firstName", Types.STRING);
        Parameter lastName = new Parameter("lastName", Types.STRING);
        Parameter age = new Parameter("age", Types.INTEGER);
        type.addConstructor(new Constructor(Arrays.asList(firstName, lastName, age)));
        
        Writer writer = new StringWriter();
        ProjectionSerializer serializer = new ProjectionSerializer(new TypeMappings());
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        assertTrue(writer.toString().contains("StringExpression firstName"));
        assertTrue(writer.toString().contains("StringExpression lastName"));
        assertTrue(writer.toString().contains("NumberExpression<Integer> age"));
    }

}
