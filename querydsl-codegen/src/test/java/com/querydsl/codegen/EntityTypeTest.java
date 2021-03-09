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

import java.util.Collections;

import org.junit.Test;

import com.querydsl.codegen.utils.model.ClassType;
import com.querydsl.codegen.utils.model.TypeCategory;

public class EntityTypeTest {

    @Test
    public void uncapSimpleName_escaped() {
        ClassType typeModel = new ClassType(TypeCategory.ENTITY, Object.class);
        EntityType entityModel = new EntityType(typeModel);
        assertEquals("object", entityModel.getModifiedSimpleName());

        entityModel.addProperty(new Property(entityModel, "object", typeModel));
        assertEquals("object1", entityModel.getModifiedSimpleName());
    }

    @Test
    public void uncapSimpleName_escaped2() {
        ClassType typeModel = new ClassType(TypeCategory.ENTITY, Object.class);
        EntityType entityModel = new EntityType(typeModel);
        assertEquals("object", entityModel.getModifiedSimpleName());

        entityModel.addProperty(new Property(entityModel, "OBJECT", "object", typeModel,
                Collections.<String> emptyList(), false));
        assertEquals("object1", entityModel.getModifiedSimpleName());
    }

    @Test
    public void uncapSimpleName_escaped3() {
        ClassType typeModel = new ClassType(TypeCategory.ENTITY, Void.class);
        EntityType entityModel = new EntityType(typeModel);
        assertEquals("void$", entityModel.getModifiedSimpleName());
    }

}
