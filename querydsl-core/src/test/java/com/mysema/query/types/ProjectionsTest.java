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
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.QBeanPropertyTest.Entity;
import com.mysema.query.types.path.PathBuilder;

public class ProjectionsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void Array() {
        FactoryExpression<String[]> expr = Projections.array(String[].class, new PathImpl(String.class, "p1"), new PathImpl(String.class, "p2"));
        assertEquals(String[].class, expr.newInstance("1","2").getClass());
    }

    @Test
    public void BeanClassOfTExpressionOfQArray() {
        PathBuilder<Entity> entity = new PathBuilder<Entity>(Entity.class, "entity");
        QBean<Entity> beanProjection = Projections.bean(Entity.class,
                entity.getNumber("cId",Integer.class),
                entity.getNumber("eId", Integer.class));

        assertEquals(Entity.class, beanProjection.newInstance(1,2).getClass());
    }

    @Test
    public void Constructor() {
        Expression<Long> longVal = ConstantImpl.create(1l);
        Expression<String> stringVal = ConstantImpl.create("");
        assertEquals(ProjectionExample.class, Projections.constructor(ProjectionExample.class, longVal, stringVal).newInstance(0l,"").getClass());
    }

    @Test
    public void FieldsClassOfTExpressionOfQArray() {
        PathBuilder<Entity> entity = new PathBuilder<Entity>(Entity.class, "entity");
        QBean<Entity> beanProjection = Projections.fields(Entity.class,
                entity.getNumber("cId",Integer.class),
                entity.getNumber("eId", Integer.class));

        assertEquals(Entity.class, beanProjection.newInstance(1,2).getClass());
    }


}
