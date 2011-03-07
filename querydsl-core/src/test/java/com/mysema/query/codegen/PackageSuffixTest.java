package com.mysema.query.codegen;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import org.junit.Test;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.TypeCategory;

public class PackageSuffixTest {
    
    private final QueryTypeFactory queryTypeFactory = new QueryTypeFactoryImpl("Q", "", ".query");

    private final TypeMappings typeMappings = new TypeMappings();

    private final EntitySerializer serializer = new EntitySerializer(typeMappings, Collections.<String>emptySet());

    private final StringWriter writer = new StringWriter();

    @Test
    public void Correct_Imports() throws IOException {
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "test.Entity", "test", "Entity",false,false);
        EntityType entityType = new EntityType(type);
        typeMappings.register(entityType, queryTypeFactory.create(entityType));

        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        assertTrue(writer.toString().contains("import test.Entity;"));
        assertTrue(writer.toString().contains("public class QEntity extends EntityPathBase<Entity> {"));
    }

}
