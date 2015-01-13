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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;

public class TypeMappingsTest {

    static class Entity { }

    @Test
    public void GetPathType_Of_InnerClass() {
        TypeMappings typeMappings = new JavaTypeMappings();
        EntityType model = new EntityType(new ClassType(TypeMappingsTest.class));
        EntityType type = new EntityType(new ClassType(Entity.class));
        typeMappings.register(type, new QueryTypeFactoryImpl("Q","","").create(type));

        Type pathType = typeMappings.getPathType(type, model, false);
        assertEquals("QTypeMappingsTest_Entity", pathType.getSimpleName());
    }

    @Test
    public void IsRegistered() {
        TypeMappings typeMappings = new JavaTypeMappings();
        typeMappings.register(new ClassType(Double[].class), new ClassType(Point.class));
        assertTrue(typeMappings.isRegistered(new ClassType(Double[].class)));

    }

}
