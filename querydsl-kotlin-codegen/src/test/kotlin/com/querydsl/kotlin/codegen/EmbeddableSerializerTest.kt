/*
 * Copyright 2021, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.kotlin.codegen

import com.querydsl.codegen.Delegate
import com.querydsl.codegen.EntitySerializer
import com.querydsl.codegen.EntityType
import com.querydsl.codegen.GeneratedAnnotationResolver
import com.querydsl.codegen.Property
import com.querydsl.codegen.QueryTypeFactory
import com.querydsl.codegen.QueryTypeFactoryImpl
import com.querydsl.codegen.SimpleSerializerConfig
import com.querydsl.codegen.Supertype
import com.querydsl.codegen.TypeMappings
import com.querydsl.codegen.utils.JavaWriter
import com.querydsl.codegen.utils.model.ClassType
import com.querydsl.codegen.utils.model.SimpleType
import com.querydsl.codegen.utils.model.TypeCategory
import com.querydsl.codegen.utils.model.Types
import com.querydsl.core.annotations.Generated
import com.querydsl.core.annotations.PropertyType
import com.querydsl.kotlin.codegen.CompileUtils.assertCompiles
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import java.io.StringWriter
import java.sql.Time
import java.util.*

class EmbeddableSerializerTest {
    private val queryTypeFactory: QueryTypeFactory = QueryTypeFactoryImpl("Q", "", "")
    private val typeMappings: TypeMappings = KotlinTypeMappings()
    private val serializer: EntitySerializer = KotlinEmbeddableSerializer(typeMappings, emptySet())
    private val writer = StringWriter()

    @Test
    fun properties() {
        val type = SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity", false, false)
        val entityType = EntityType(type)
        entityType.addProperty(Property(entityType, "b", ClassType(TypeCategory.BOOLEAN, Boolean::class.java)))
        entityType.addProperty(Property(entityType, "c", ClassType(TypeCategory.COMPARABLE, String::class.java)))
        //entityType.addProperty(new Property(entityType, "cu", new ClassType(TypeCategory.CUSTOM, PropertyType.class)));
        entityType.addProperty(Property(entityType, "d", ClassType(TypeCategory.DATE, Date::class.java)))
        entityType.addProperty(Property(entityType, "e", ClassType(TypeCategory.ENUM, PropertyType::class.java)))
        entityType.addProperty(Property(entityType, "dt", ClassType(TypeCategory.DATETIME, Date::class.java)))
        entityType.addProperty(Property(entityType, "i", ClassType(TypeCategory.NUMERIC, Int::class.java)))
        entityType.addProperty(Property(entityType, "s", ClassType(TypeCategory.STRING, String::class.java)))
        entityType.addProperty(Property(entityType, "t", ClassType(TypeCategory.TIME, Time::class.java)))
        entityType.addProperty(Property(entityType, "o", ClassType(TypeCategory.SIMPLE, Object::class.java)))
        entityType.addProperty(Property(entityType, "a", ClassType(TypeCategory.SIMPLE, Any::class.java)))
        typeMappings.register(entityType, queryTypeFactory.create(entityType))
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        assertCompiles("QEntity", writer.toString())
    }

    @Test
    fun originalCategory() {
        val categoryToSuperClass: MutableMap<TypeCategory, String> = EnumMap(TypeCategory::class.java)
        categoryToSuperClass[TypeCategory.COMPARABLE] = "ComparablePath<Entity>"
        categoryToSuperClass[TypeCategory.ENUM] = "EnumPath<Entity>"
        categoryToSuperClass[TypeCategory.DATE] = "DatePath<Entity>"
        categoryToSuperClass[TypeCategory.DATETIME] = "DateTimePath<Entity>"
        categoryToSuperClass[TypeCategory.TIME] = "TimePath<Entity>"
        categoryToSuperClass[TypeCategory.NUMERIC] = "NumberPath<Entity>"
        categoryToSuperClass[TypeCategory.STRING] = "StringPath"
        categoryToSuperClass[TypeCategory.BOOLEAN] = "BooleanPath"
        for ((key, value) in categoryToSuperClass) {
            val w = StringWriter()
            val type = SimpleType(key, "Entity", "", "Entity", false, false)
            val entityType = EntityType(type)
            typeMappings.register(entityType, queryTypeFactory.create(entityType))
            serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, JavaWriter(w))
            Assert.assertTrue("$value is missing from $w", w.toString().contains("class QEntity : $value {"))
        }
    }

    @Test
    fun empty() {
        val type = SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity", false, false)
        val entityType = EntityType(type)
        typeMappings.register(entityType, queryTypeFactory.create(entityType))
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        assertCompiles("QEntity", writer.toString())
    }

    @Test
    fun no_package() {
        val type = SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity", false, false)
        val entityType = EntityType(type)
        typeMappings.register(entityType, queryTypeFactory.create(entityType))
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        Assert.assertTrue(writer.toString().contains("class QEntity : BeanPath<Entity> {"))
        assertCompiles("QEntity", writer.toString())
    }

    @Test
    fun correct_superclass() {
        val type = SimpleType(TypeCategory.ENTITY, "java.util.Locale", "java.util", "Locale", false, false)
        val entityType = EntityType(type)
        typeMappings.register(entityType, queryTypeFactory.create(entityType))
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        Assert.assertTrue(writer.toString().contains("class QLocale : BeanPath<Locale> {"))
        assertCompiles("QLocale", writer.toString())
    }

    @Test
    fun primitive_array() {
        val type = SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity", false, false)
        val entityType = EntityType(type)
        entityType.addProperty(Property(entityType, "bytes", ClassType(ByteArray::class.java)))
        typeMappings.register(entityType, queryTypeFactory.create(entityType))
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        Assert.assertTrue(writer.toString().contains("val bytes: SimplePath<ByteArray>"))
        assertCompiles("QEntity", writer.toString())
    }

    @Test
    fun include() {
        val type = SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity", false, false)
        val entityType = EntityType(type)
        entityType.addProperty(Property(entityType, "b", ClassType(TypeCategory.BOOLEAN, Boolean::class.java)))
        entityType.addProperty(Property(entityType, "c", ClassType(TypeCategory.COMPARABLE, String::class.java)))
        //entityType.addProperty(new Property(entityType, "cu", new ClassType(TypeCategory.CUSTOM, PropertyType.class)));
        entityType.addProperty(Property(entityType, "d", ClassType(TypeCategory.DATE, Date::class.java)))
        entityType.addProperty(Property(entityType, "e", ClassType(TypeCategory.ENUM, PropertyType::class.java)))
        entityType.addProperty(Property(entityType, "dt", ClassType(TypeCategory.DATETIME, Date::class.java)))
        entityType.addProperty(Property(entityType, "i", ClassType(TypeCategory.NUMERIC, Int::class.java)))
        entityType.addProperty(Property(entityType, "s", ClassType(TypeCategory.STRING, String::class.java)))
        entityType.addProperty(Property(entityType, "t", ClassType(TypeCategory.TIME, Time::class.java)))
        entityType.addProperty(Property(entityType, "o", ClassType(TypeCategory.SIMPLE, Object::class.java)))
        entityType.addProperty(Property(entityType, "a", ClassType(TypeCategory.SIMPLE, Any::class.java)))
        val subType = EntityType(SimpleType(TypeCategory.ENTITY, "Entity2", "", "Entity2", false, false))
        subType.include(Supertype(type, entityType))
        typeMappings.register(entityType, queryTypeFactory.create(entityType))
        typeMappings.register(subType, queryTypeFactory.create(subType))
        serializer.serialize(subType, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        assertCompiles("QEntity2", writer.toString())
    }

    @Test
    fun superType() {
        val superType = EntityType(SimpleType(TypeCategory.ENTITY, "Entity2", "", "Entity2", false, false))
        val type = SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity", false, false)
        val entityType = EntityType(type, setOf(Supertype(superType, superType)))
        typeMappings.register(superType, queryTypeFactory.create(superType))
        typeMappings.register(entityType, queryTypeFactory.create(entityType))
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        Assert.assertTrue(writer.toString().contains("val _super: QEntity2 by lazy {\n    QEntity2(this)\n  }"))
        //CompileUtils.assertCompiles("QEntity", writer.toString());
    }

    @Test
    @Ignore
    fun delegates() {
        val type = SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity", false, false)
        val entityType = EntityType(type)
        val delegate = Delegate(type, type, "test", emptyList(), Types.STRING)
        entityType.addDelegate(delegate)
        typeMappings.register(entityType, queryTypeFactory.create(entityType))
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        Assert.assertTrue(writer.toString().contains("return Entity.test(this);"))
        assertCompiles("QEntity", writer.toString())
    }

    @Test
    fun defaultGeneratedAnnotation() {
        val type = SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity", false, false)
        val entityType = EntityType(type)
        typeMappings.register(entityType, queryTypeFactory.create(entityType))
        serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        val generatedSource = writer.toString()
        Assert.assertThat(generatedSource, Matchers.containsString("import $generatedAnnotationImport"))
        Assert.assertThat(generatedSource, Matchers.containsString("@Generated(\"com.querydsl.kotlin.codegen.KotlinEmbeddableSerializer\")\npublic class"))
        assertCompiles("QEntity", generatedSource)
    }

    @Test
    fun customGeneratedAnnotation() {
        val type = SimpleType(TypeCategory.ENTITY, "Entity", "", "Entity", false, false)
        val entityType = EntityType(type)
        typeMappings.register(entityType, queryTypeFactory.create(entityType))
        KotlinEmbeddableSerializer(typeMappings, emptySet(), Generated::class.java).serialize(entityType, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        val generatedSourceCode = writer.toString()
        Assert.assertThat(generatedSourceCode, Matchers.containsString("@Generated(\"com.querydsl.kotlin.codegen.KotlinEmbeddableSerializer\")\npublic class"))
        assertCompiles("QEntity", generatedSourceCode)
    }
}