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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.StringUtils;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Constructor;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.TypeExtends;
import com.mysema.codegen.model.TypeSuper;

public class SerializerTest {

    private EntityType type;

    private Writer writer = new StringWriter();

    private TypeMappings typeMappings = new JavaTypeMappings();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        // type
        Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.querydsl.DomainClass", "com.querydsl", "DomainClass", false, false);
        type = new EntityType(typeModel);

        // property
        type.addProperty(new Property(type, "entityField", type));
        type.addProperty(new Property(type, "collection", new ClassType(TypeCategory.COLLECTION, Collection.class, typeModel)));
        type.addProperty(new Property(type, "listField", new ClassType(TypeCategory.LIST, List.class, typeModel)));
        type.addProperty(new Property(type, "setField", new ClassType(TypeCategory.SET, Set.class, typeModel)));
        type.addProperty(new Property(type, "arrayField", new ClassType(TypeCategory.ARRAY, String[].class, typeModel)));
        type.addProperty(new Property(type, "mapField", new ClassType(TypeCategory.MAP, List.class, typeModel, typeModel)));
        type.addProperty(new Property(type, "superTypeField", new TypeExtends(new ClassType(TypeCategory.MAP, List.class, typeModel, typeModel))));
        type.addProperty(new Property(type, "extendsTypeField", new TypeSuper(new ClassType(TypeCategory.MAP, List.class, typeModel, typeModel))));

        for (Class<?> cl : Arrays.asList(Boolean.class, Comparable.class, Integer.class, Date.class, java.sql.Date.class, java.sql.Time.class)) {
            Type classType = new ClassType(TypeCategory.get(cl.getName()), cl);
            type.addProperty(new Property(type, StringUtils.uncapitalize(cl.getSimpleName()), classType));
        }

        // constructor
        Parameter firstName = new Parameter("firstName", new ClassType(TypeCategory.STRING, String.class));
        Parameter lastName = new Parameter("lastName", new ClassType(TypeCategory.STRING, String.class));
        type.addConstructor(new Constructor(Arrays.asList(firstName, lastName)));
    }

    @Test
    public void EntitySerializer() throws Exception {
        new EntitySerializer(typeMappings, Collections.<String>emptyList())
            .serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
    }

    @Test
    public void EntitySerializer2() throws Exception {
        new EntitySerializer(typeMappings,Collections.<String>emptyList())
            .serialize(type, new SimpleSerializerConfig(true,true,true,true,""), new JavaWriter(writer));
    }

    @Test
    public void EmbeddableSerializer() throws Exception {
        new EmbeddableSerializer(typeMappings,Collections.<String>emptyList())
            .serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
    }

    @Test
    public void SupertypeSerializer() throws IOException{
        new SupertypeSerializer(typeMappings,Collections.<String>emptyList())
            .serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
    }

    @Test
    public void DTOSerializer() throws IOException{
        new ProjectionSerializer(typeMappings)
            .serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
    }
}
