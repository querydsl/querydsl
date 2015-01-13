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
import java.util.Date;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.StringUtils;
import com.mysema.codegen.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class GroovyBeanSerializerTest {
    
    private Type typeModel;
    
    private EntityType type;

    private final Writer writer = new StringWriter();

    @Before
    public void setUp() {
        typeModel = new SimpleType(TypeCategory.ENTITY, "com.querydsl.DomainClass", "com.querydsl", "DomainClass", false,false);
        type = new EntityType(typeModel);                    
    }
        
    @Test
    public void Properties() throws IOException{
        // property
        type.addProperty(new Property(type, "entityField", type));
        type.addProperty(new Property(type, "collection", new SimpleType(Types.COLLECTION, typeModel)));
        type.addProperty(new Property(type, "listField", new SimpleType(Types.LIST, typeModel)));
        type.addProperty(new Property(type, "setField", new SimpleType(Types.SET, typeModel)));
        type.addProperty(new Property(type, "arrayField", new ClassType(TypeCategory.ARRAY, String[].class)));
        type.addProperty(new Property(type, "mapField", new SimpleType(Types.MAP, typeModel, typeModel)));

        for (Class<?> cl : Arrays.<Class<?>>asList(Boolean.class, Comparable.class, Integer.class, 
                Date.class, java.sql.Date.class, java.sql.Time.class)) {
            Type classType = new ClassType(TypeCategory.get(cl.getName()), cl);
            type.addProperty(new Property(type, StringUtils.uncapitalize(cl.getSimpleName()), classType));
        }

        GroovyBeanSerializer serializer = new GroovyBeanSerializer();
        serializer.serialize(type, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        String str = writer.toString();
        //System.err.println(str);
        for (String prop : Arrays.asList(
                "String[] arrayField;",
                "Boolean boolean$;",
                "Collection<DomainClass> collection;",
                "Comparable comparable;",
                "java.util.Date date;",
                "DomainClass entityField;",
                "Integer integer;",
                "List<DomainClass> listField;",
                "Map<DomainClass, DomainClass> mapField;",
                "Set<DomainClass> setField;",
                "java.sql.Time time;")) {
            assertTrue(prop + " was not contained", str.contains(prop));
        }
    }
    
}
