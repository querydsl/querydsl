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

import com.querydsl.codegen.EntityType
import com.querydsl.codegen.ProjectionSerializer
import com.querydsl.codegen.SimpleSerializerConfig
import com.querydsl.codegen.utils.JavaWriter
import com.querydsl.codegen.utils.model.Constructor
import com.querydsl.codegen.utils.model.Parameter
import com.querydsl.codegen.utils.model.SimpleType
import com.querydsl.codegen.utils.model.Type
import com.querydsl.codegen.utils.model.TypeCategory
import com.querydsl.codegen.utils.model.Types
import com.querydsl.core.annotations.Generated
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test
import java.io.StringWriter
import java.io.Writer
import java.util.*

class ProjectionSerializerTest {
    @Test
    fun constructors() {
        val typeModel: Type = SimpleType(TypeCategory.ENTITY, "com.querydsl.DomainClass", "com.querydsl", "DomainClass", false, false)
        val type = EntityType(typeModel)

        // constructor
        val firstName = Parameter("firstName", Types.STRING)
        val lastName = Parameter("lastName", Types.STRING)
        val age = Parameter("age", Types.INTEGER)
        type.addConstructor(Constructor(Arrays.asList(firstName, lastName, age)))
        val writer: Writer = StringWriter()
        val serializer: ProjectionSerializer = KotlinProjectionSerializer(KotlinTypeMappings())
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        Assert.assertTrue(writer.toString().contains("firstName: Expression<String>"))
        Assert.assertTrue(writer.toString().contains("lastName: Expression<String>"))
        Assert.assertTrue(writer.toString().contains("age: Expression<Int>"))
    }

    @Test
    fun defaultGeneratedAnnotation() {
        val typeModel: Type = SimpleType(TypeCategory.ENTITY, "com.querydsl.DomainClass", "com.querydsl", "DomainClass", false, false)
        val type = EntityType(typeModel)
        val writer: Writer = StringWriter()
        val serializer: ProjectionSerializer = KotlinProjectionSerializer(KotlinTypeMappings())
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        val generatedSource = writer.toString()
        Assert.assertThat(generatedSource, Matchers.containsString("import javax.annotation.Generated"))
        Assert.assertThat(generatedSource, Matchers.containsString("@Generated(\"com.querydsl.kotlin.codegen.KotlinProjectionSerializer\")\nclass"))
    }

    @Test
    fun customGeneratedAnnotation() {
        val typeModel: Type = SimpleType(TypeCategory.ENTITY, "com.querydsl.DomainClass", "com.querydsl", "DomainClass", false, false)
        val type = EntityType(typeModel)
        val writer: Writer = StringWriter()
        val serializer: ProjectionSerializer = KotlinProjectionSerializer(KotlinTypeMappings(), Generated::class.java)
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, JavaWriter(writer))
        val generatedSource = writer.toString()
        Assert.assertThat(generatedSource, Matchers.containsString("import com.querydsl.core.annotations.Generated"))
        Assert.assertThat(generatedSource, Matchers.containsString("@Generated(\"com.querydsl.kotlin.codegen.KotlinProjectionSerializer\")\nclass"))
    }
}