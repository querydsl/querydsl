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

import org.junit.Test;

import com.querydsl.core.types.path.PathBuilder;

public class QBeanPropertyTest {

    public static class Entity {

        private Integer cId;

        private Integer eId;

        public Integer getcId() {
            return cId;
        }

        public void setcId(Integer cId) {
            this.cId = cId;
        }

        public Integer geteId() {
            return eId;
        }

        public void seteId(Integer eId) {
            this.eId = eId;
        }



    }

    @Test
    public void Field_Access() {
        PathBuilder<Entity> entity = new PathBuilder<Entity>(Entity.class, "entity");
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, true,
                entity.getNumber("cId",Integer.class),
                entity.getNumber("eId", Integer.class));

        Entity bean = beanProjection.newInstance(1,2);
        assertEquals(Integer.valueOf(1), bean.getcId());
        assertEquals(Integer.valueOf(2), bean.geteId());
    }

    @Test
    public void Property_Access() {
        PathBuilder<Entity> entity = new PathBuilder<Entity>(Entity.class, "entity");
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class,
                entity.getNumber("cId",Integer.class),
                entity.getNumber("eId", Integer.class));

        Entity bean = beanProjection.newInstance(1,2);
        assertEquals(Integer.valueOf(1), bean.getcId());
        assertEquals(Integer.valueOf(2), bean.geteId());
    }

}
