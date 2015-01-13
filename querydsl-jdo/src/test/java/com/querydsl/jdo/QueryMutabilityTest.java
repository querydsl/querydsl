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
package com.querydsl.jdo;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.querydsl.core.QueryMutability;
import com.querydsl.jdo.test.domain.QProduct;

public class QueryMutabilityTest extends AbstractJDOTest{

    @Test
    public void QueryMutability() throws IOException, SecurityException,
            IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        QProduct product = QProduct.product;
        JDOQuery query = (JDOQuery) query().from(product);
        new QueryMutability(query).test(product.name, product.description);
    }

    @Test
    public void Clone() {
        QProduct product = QProduct.product;
        JDOQuery query = new JDOQuery().from(product).where(product.name.isNotNull());
        JDOQuery query2 = query.clone(pm);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(product);
    }

}
