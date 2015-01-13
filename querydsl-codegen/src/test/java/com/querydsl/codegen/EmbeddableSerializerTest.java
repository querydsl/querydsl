/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.codegen;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Time;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.*;
import com.querydsl.core.annotations.PropertyType;
import org.junit.Test;

public class EmbeddableSerializerTest {

    private final QueryTypeFactory queryTypeFactory = new QueryTypeFactoryImpl("Q", "", "");

    private final TypeMappings typeMappings = new JavaTypeMappings();

    private final EntitySerializer serializer = new EmbeddableSerializer(typeMappings, Collections.<String>emptySet());

    private final StringWriter writer = new StringWriter();

    @Test
    public void Properties() throws IOException{
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity",false,false);
        EntityType entityType = new EntityType(type);
        entityType.addProperty(new Property(entityType, "b", new ClassType(TypeCategory.BOOLEAN, Boolean.class)));
        entityType.addProperty(new Property(entityType, "c", new ClassType(TypeCategory.COMPARABLE, String.class)));
        entityType.addProperty(new Property(entityType, "cu", new ClassType(TypeCategory.CUSTOM, PropertyType.class)));
        entityType.addProperty(new Property(entityType, "d", new ClassType(TypeCategory.DATE, Date.class)));
        entityType.addProperty(new Property(entityType, "e", new ClassType(TypeCategory.ENUM, PropertyType.class)));
        entityType.addProperty(new Property(entityType, "dt", new ClassType(TypeCategory.DATETIME, Date.class)));
        entityType.addProperty(new Property(entityType, "i", new ClassType(TypeCategory.NUMERIC, Integer.class)));
        entityType.addProperty(new Property(entityType, "s", new ClassType(TypeCategory.STRING, String.class)));
        entityType.addProperty(new Property(entityType, "t", new ClassType(TypeCategory.TIME, Time.class)));

        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        // TODO : assertions
    }

    @Test
    public void OriginalCategory() throws IOException{
        Map<TypeCategory, String> categoryToSuperClass
                = new EnumMap<TypeCategory, String>(TypeCategory.class);
        categoryToSuperClass.put(TypeCategory.COMPARABLE, "ComparablePath<Entity>");
        categoryToSuperClass.put(TypeCategory.ENUM, "EnumPath<Entity>");
        categoryToSuperClass.put(TypeCategory.DATE, "DatePath<Entity>");
        categoryToSuperClass.put(TypeCategory.DATETIME, "DateTimePath<Entity>");
        categoryToSuperClass.put(TypeCategory.TIME, "TimePath<Entity>");
        categoryToSuperClass.put(TypeCategory.NUMERIC, "NumberPath<Entity>");
        categoryToSuperClass.put(TypeCategory.STRING, "StringPath");
        categoryToSuperClass.put(TypeCategory.BOOLEAN, "BooleanPath");

        for (Map.Entry<TypeCategory, String> entry : categoryToSuperClass.entrySet()) {
            SimpleType type = new SimpleType(entry.getKey(), "Entity", "", "Entity",false,false);
            EntityType entityType = new EntityType(type);
            typeMappings.register(entityType, queryTypeFactory.create(entityType));

            serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
            assertTrue(entry.toString(), writer.toString().contains("public class QEntity extends "+entry.getValue()+" {"));
        }

    }

    @Test
    public void Empty() throws IOException{
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity",false,false);
        EntityType entityType = new EntityType(type);

        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        // TODO : assertions
    }

    @Test
    public void No_Package() throws IOException {
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity",false,false);
        EntityType entityType = new EntityType(type);
        typeMappings.register(entityType, queryTypeFactory.create(entityType));
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        assertTrue(writer.toString().contains("public class QEntity extends BeanPath<Entity> {"));
    }

    @Test
    public void Correct_Superclass() throws IOException {
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "java.util.Locale", "java.util", "Locale",false,false);
        EntityType entityType = new EntityType(type);
        typeMappings.register(entityType, queryTypeFactory.create(entityType));
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        //System.out.println(writer);
        assertTrue(writer.toString().contains("public class QLocale extends BeanPath<Locale> {"));
    }

    @Test
    public void Primitive_Array() throws IOException{
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity",false,false);
        EntityType entityType = new EntityType(type);
        entityType.addProperty(new Property(entityType, "bytes", new ClassType(byte[].class)));
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        assertTrue(writer.toString().contains("public final SimplePath<byte[]> bytes"));
    }

    @Test
    public void Include() throws IOException{
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity",false,false);
        EntityType entityType = new EntityType(type);
        entityType.addProperty(new Property(entityType, "b", new ClassType(TypeCategory.BOOLEAN, Boolean.class)));
        entityType.addProperty(new Property(entityType, "c", new ClassType(TypeCategory.COMPARABLE, String.class)));
        entityType.addProperty(new Property(entityType, "cu", new ClassType(TypeCategory.CUSTOM, PropertyType.class)));
        entityType.addProperty(new Property(entityType, "d", new ClassType(TypeCategory.DATE, Date.class)));
        entityType.addProperty(new Property(entityType, "e", new ClassType(TypeCategory.ENUM, PropertyType.class)));
        entityType.addProperty(new Property(entityType, "dt", new ClassType(TypeCategory.DATETIME, Date.class)));
        entityType.addProperty(new Property(entityType, "i", new ClassType(TypeCategory.NUMERIC, Integer.class)));
        entityType.addProperty(new Property(entityType, "s", new ClassType(TypeCategory.STRING, String.class)));
        entityType.addProperty(new Property(entityType, "t", new ClassType(TypeCategory.TIME, Time.class)));

        EntityType subType = new EntityType(new SimpleType(TypeCategory.ENTITY, "Entity2", "", "Entity2",false,false));
        subType.include(new Supertype(type,entityType));

        serializer.serialize(subType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        // TODO : assertions
    }

    @Test
    public void SuperType() throws IOException{
        EntityType superType = new EntityType(new SimpleType(TypeCategory.ENTITY, "Entity2", "", "Entity2",false,false));
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity",false,false);
        EntityType entityType = new EntityType(type, Collections.singleton(new Supertype(superType, superType)));
        typeMappings.register(superType, queryTypeFactory.create(superType));
        typeMappings.register(entityType, queryTypeFactory.create(entityType));

        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        assertTrue(writer.toString().contains("public final QEntity2 _super = new QEntity2(this);"));
    }

    @Test
    public void Delegates() throws IOException{
        SimpleType type = new SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity",false,false);
        EntityType entityType = new EntityType(type);
        Delegate delegate = new Delegate(type, type, "test", Collections.<Parameter>emptyList(), Types.STRING);
        entityType.addDelegate(delegate);

        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        assertTrue(writer.toString().contains("return Entity.test(this);"));
    }

}
