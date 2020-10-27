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

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jdo.test.domain.Product;
import com.querydsl.jdo.test.domain.QProduct;
import com.querydsl.jdo.test.domain.QStore;

public class JDOQLMethodsTest extends AbstractJDOTest {

    private QProduct product = QProduct.product;

    private QStore store = QStore.store;

    @Test
    public void test() {
        Product p = query().from(product).limit(1).select(product).fetchOne().get();
        for (BooleanExpression f : getFilters(
                product.name, product.description, "A0",
                store.products, p,
                store.productsByName, "A0", p,
                product.amount)) {
            query().from(store, product).where(f).select(store, product);
        }
    }

    private <A,K,V> List<BooleanExpression> getFilters(
            StringExpression str, StringExpression other, String knownString,
            ListPath<A,?> list, A element,
            MapPath<K,V, ?> map, K key, V value,
            NumberExpression<Integer> number) {
        return Arrays.<BooleanExpression>asList(
           // java.lang.String
           str.startsWith(knownString),
           str.endsWith(knownString),
           str.indexOf(knownString).gt(-1),
           str.indexOf(knownString, 1).gt(-1),
           str.substring(1).eq(knownString),
           str.substring(1,2).eq(knownString),
           str.lower().eq(knownString),
           str.likeIgnoreCase(knownString),
           str.upper().eq(knownString),
           str.matches(".*"),
           // java.util.Collection
           list.isEmpty(),
           list.isNotEmpty(),
           list.contains(element),
           list.size().gt(0),
           // java.util.Map
           map.isEmpty(),
           map.isNotEmpty(),
           map.containsKey(key),
           map.containsValue(value),
           map.get(key).eq(value),
           map.size().gt(0),
           number.abs().gt(0),
           number.sqrt().gt(0)
        );
    }

    @BeforeClass
    public static void doPersist() {
        List<Object> entities = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            entities.add(new Product("C" + i, "F" + i, i * 200.00, 2));
            entities.add(new Product("B" + i, "E" + i, i * 200.00, 4));
            entities.add(new Product("A" + i, "D" + i, i * 200.00, 6));
        }
        doPersist(entities);
    }
}
