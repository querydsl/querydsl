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
package com.querydsl.jdo.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.jdo.JDOQLSerializer;
import com.querydsl.jdo.JDOQLTemplates;
import com.querydsl.jdo.test.domain.Book;
import com.querydsl.jdo.test.domain.Product;
import com.querydsl.jdo.test.domain.QBook;
import com.querydsl.jdo.test.domain.QProduct;
import com.querydsl.jdo.test.domain.QStore;
import com.querydsl.core.types.Expression;

public class ExprSerializationTest {

    private QBook book = QBook.book;

    private QProduct product = QProduct.product;

    private QStore store = QStore.store;

    @Test
    public void InstanceOf() {
        assertEquals(
                "product instanceof com.querydsl.jdo.test.domain.Book",
                serialize(product.instanceOf(Book.class)));
    }

    @Test
    public void Eq() {
        assertEquals("this.name == product.name", serialize(book.name.eq(product.name)));
        assertEquals("this == product", serialize(book.eq(product)));
    }

    @Test
    public void Aggregation() {
        assertEquals("sum(product.price)", serialize(product.price.sum()));
        assertEquals("min(product.price)", serialize(product.price.min()));
        assertEquals("max(product.price)", serialize(product.price.max()));
        assertEquals("avg(product.price)", serialize(product.price.avg()));
        assertEquals("count(product.price)", serialize(product.price.count()));
    }

    @Test
    public void BooleanTests() {
        // boolean
        assertEquals("product.name == a1 && product.price <= a2",
                serialize(product.name.eq("Sony Discman").and(product.price.loe(300.00))));
        assertEquals("product.name == a1 || product.price <= a2",
                serialize(product.name.eq("Sony Discman").or(product.price.loe(300.00))));
        assertEquals("!(product.name == a1)", serialize(product.name.eq(
                "Sony MP3 player").not()));
    }

    @Test
    public void CollectionTests() {
        Product product = new Product();
        // collection
        assertEquals("store.products.contains(a1)",
                serialize(store.products.contains(product)));
//        assertEquals("store.products.get(0) == a1",
//                serialize(store.products.get(0).eq(product)));
        assertEquals("store.products.isEmpty()",
                serialize(store.products.isEmpty()));
        assertEquals("!store.products.isEmpty()",
                serialize(store.products.isNotEmpty()));
        assertEquals("store.products.size() == a1",
                serialize(store.products.size().eq(1)));
    }

    @Test
    public void MapTests() {
        assertEquals("store.productsByName.containsKey(a1)",
                serialize(store.productsByName.containsKey("")));
        assertEquals("store.productsByName.containsValue(a1)",
                serialize(store.productsByName.containsValue(new Product())));

        assertEquals("store.productsByName.isEmpty()",
                serialize(store.productsByName.isEmpty()));
        assertEquals("!store.productsByName.isEmpty()",
                serialize(store.productsByName.isNotEmpty()));
    }

    @Test
    public void NumericTests() {
        // numeric
        assertEquals("product.price == a1", serialize(product.price.eq(200.00)));
        assertEquals("product.price != a1", serialize(product.price.ne(100.00)));
        assertEquals("product.price > a1", serialize(product.price.gt(100.00)));
        assertEquals("product.price < a1", serialize(product.price.lt(300.00)));
        assertEquals("product.price >= a1", serialize(product.price.goe(100.00)));
        assertEquals("product.price <= a1", serialize(product.price.loe(300.00)));
        // TODO +
        // TODO -
        // TODO *
        // TODO /
        // TODO %
        // TODO Math.abs
        // TODO Math.sqrt
    }

    @Test
    public void StringTests() {
        // string
        assertEquals("product.name.startsWith(a1)", serialize(product.name.startsWith("Sony Discman")));
        assertEquals("product.name.endsWith(a1)", serialize(product.name.endsWith("Discman")));
        assertEquals("product.name.toLowerCase() == a1", serialize(product.name.lower().eq("sony discman")));
        assertEquals("product.name.toUpperCase() == a1", serialize(product.name.upper().eq("SONY DISCMAN")));
        assertEquals("product.name.indexOf(a1) == a2", serialize(product.name.indexOf("S").eq(0)));
        // TODO indexOf
        // TODO matches
        assertEquals("product.name.substring(a1,a2) == a3", serialize(product.name.substring(0, 4).eq("Sony")));
        assertEquals("product.name.substring(a1) == a2", serialize(product.name.substring(5).eq("Discman")));

        assertEquals("product.name == \"\"", serialize(product.name.isEmpty()));
        assertEquals("!(product.name == \"\")", serialize(product.name.isNotEmpty()));
    }

    private String serialize(Expression<?> expr) {
        return new JDOQLSerializer(JDOQLTemplates.DEFAULT, book).handle(expr).toString();
    }

}
