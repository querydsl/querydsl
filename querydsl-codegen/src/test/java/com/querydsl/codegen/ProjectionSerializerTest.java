/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Name;

import org.junit.Test;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.*;


public class ProjectionSerializerTest {

    @Test
    public void constructors() throws IOException {
        Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.querydsl.DomainClass", "com.querydsl", "DomainClass", false,false);
        EntityType type = new EntityType(typeModel);

        // constructor
        Parameter firstName = new Parameter("firstName", Types.STRING);
        Parameter lastName = new Parameter("lastName", Types.STRING);
        Parameter age = new Parameter("age", Types.INTEGER);
        type.addConstructor(new Constructor(Arrays.asList(firstName, lastName, age)));

        Writer writer = new StringWriter();
        ProjectionSerializer serializer = new ProjectionSerializer(new JavaTypeMappings());
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        assertTrue(writer.toString().contains("Expression<String> firstName"));
        assertTrue(writer.toString().contains("Expression<String> lastName"));
        assertTrue(writer.toString().contains("Expression<Integer> age"));
    }

    @Test
    public void defaultGeneratedAnnotation() throws IOException {
        Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.querydsl.DomainClass", "com.querydsl", "DomainClass", false,false);
        EntityType type = new EntityType(typeModel);

        Writer writer = new StringWriter();
        ProjectionSerializer serializer = new ProjectionSerializer(new JavaTypeMappings());
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        String generatedSource = writer.toString();
        assertFalse(generatedSource.contains("import javax.annotation.Generated"));
        assertTrue(generatedSource.contains("@javax.annotation.Generated(\"com.querydsl.codegen.ProjectionSerializer\")\npublic class"));
    }

    @Test
    public void customGeneratedAnnotation() throws IOException {
        Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.querydsl.DomainClass", "com.querydsl", "DomainClass", false,false);
        EntityType type = new EntityType(typeModel);

        Writer writer = new StringWriter();
        ProjectionSerializer serializer = new ProjectionSerializer(new JavaTypeMappings(), "some.Generated");
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        String generatedSource = writer.toString();
        assertFalse(generatedSource.contains("import some.Generated"));
        assertTrue(generatedSource.contains("@some.Generated(\"com.querydsl.codegen.ProjectionSerializer\")\npublic class"));
    }

}
