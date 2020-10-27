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
package com.querydsl.jdo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.querydsl.jdo.test.domain.Product;
import com.querydsl.jdo.test.domain.QProduct;

public class AggregateTest extends AbstractJDOTest {

    private final QProduct product = QProduct.product;

    @Test
    public void unique() {
        double min = 200.00, avg = 400.00, max = 600.00;
        assertEquals(Double.valueOf(min), query().from(product).select(product.price.min()).fetchOne().get());
        assertEquals(Double.valueOf(avg), query().from(product).select(product.price.avg()).fetchOne().get());
        assertEquals(Double.valueOf(max), query().from(product).select(product.price.max()).fetchOne().get());
    }

    @Test
    public void list() {
        double min = 200.00, avg = 400.00, max = 600.00;
        assertEquals(Double.valueOf(min), query().from(product).select(product.price.min()).fetch().get(0));
        assertEquals(Double.valueOf(avg), query().from(product).select(product.price.avg()).fetch().get(0));
        assertEquals(Double.valueOf(max), query().from(product).select(product.price.max()).fetch().get(0));
    }

    @BeforeClass
    public static void doPersist() {
        List<Product> entities = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            entities.add(new Product("C" + i, "F", 200.00, 2));
            entities.add(new Product("B" + i, "E", 400.00, 4));
            entities.add(new Product("A" + i, "D", 600.00, 6));
        }
        doPersist(entities);
    }

}
