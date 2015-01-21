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
package com.querydsl.core.types;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.path.StringPath;

public class DeepPopulationTest {

    public static class Entity1 {

        private Entity2 entity2;

        public Entity2 getEntity2() {
            return entity2;
        }

        public void setEntity2(Entity2 entity2) {
            this.entity2 = entity2;
        }

    }

    public static class Entity2 {

        private String name, id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    @Test
    public void Deep_Population_Via_QBean() {
        StringPath name = new StringPath("name");
        StringPath id = new StringPath("id");
        QBean<Entity2> entity2Bean = new QBean<Entity2>(Entity2.class, name, id);
        QBean<Entity1> entity1Bean = new QBean<Entity1>(Entity1.class, 
                Collections.singletonMap("entity2", entity2Bean));

        Entity1 entity1 = FactoryExpressionUtils.wrap(entity1Bean).newInstance("nameX","idX");
        assertEquals("nameX", entity1.getEntity2().getName());
        assertEquals("idX", entity1.getEntity2().getId());
    }

    @Test
    public void Deep_Population_Via_QTuple() {
        StringPath name = new StringPath("name");
        StringPath id = new StringPath("id");
        QBean<Entity2> entity2Bean = new QBean<Entity2>(Entity2.class, name, id);
        QTuple tupleExpr = new QTuple(entity2Bean);

        Tuple tuple = FactoryExpressionUtils.wrap(tupleExpr).newInstance("nameX","idX");
        assertEquals("nameX", tuple.get(entity2Bean).getName());
        assertEquals("idX", tuple.get(entity2Bean).getId());
    }

}
