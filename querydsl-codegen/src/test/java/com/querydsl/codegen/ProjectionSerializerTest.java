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

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.*;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;


public class ProjectionSerializerTest {
    
    @Test
    public void Constructors() throws IOException{
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

}
