package com.mysema.query.codegen;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import org.junit.Test;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.TypeCategory;

public class CustomTypeTest {

    private final QueryTypeFactory queryTypeFactory = new QueryTypeFactoryImpl("Q", "");

    private final TypeMappings typeMappings = new TypeMappings();

    private final EntitySerializer serializer = new EntitySerializer(typeMappings, Collections.<String>emptySet());

    private final StringWriter writer = new StringWriter();

    @Test
    public void CustomType() throws IOException{
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity",false,false);
        EntityType entityType = new EntityType(type);
        entityType.addProperty(new Property(entityType, "property", new ClassType(Double[].class)));
        typeMappings.register(new ClassType(Double[].class), new ClassType(Point.class));
        typeMappings.register(entityType, queryTypeFactory.create(entityType));
        assertTrue(typeMappings.isRegistered(entityType.getProperties().iterator().next().getType()));

        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        assertTrue(writer.toString().contains(
                "public final com.mysema.query.codegen.Point property = " +
        	"new com.mysema.query.codegen.Point(forProperty(\"property\"));"));
    }

}
