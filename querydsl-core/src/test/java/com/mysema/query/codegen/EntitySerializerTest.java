/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import org.junit.Test;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.TypeCategory;

public class EntitySerializerTest {

    EntitySerializer serializer = new EntitySerializer(new TypeMappings(), Collections.<String>emptySet());
    StringWriter writer = new StringWriter();
    
    @Test
    public void testSerialize_no_package() throws IOException {
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity",false,false);
        EntityType entityType = new EntityType("Q",type);        
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        assertTrue(writer.toString().contains("public class QEntity extends PEntity<Entity> {"));
    }
    
    @Test
    public void testSerialize() throws IOException {
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "java.util.Locale", "java.util", "Locale",false,false);
        EntityType entityType = new EntityType("Q",type);        
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        System.out.println(writer);
        assertTrue(writer.toString().contains("public class QLocale extends PEntity<java.util.Locale> {"));
    }

}
