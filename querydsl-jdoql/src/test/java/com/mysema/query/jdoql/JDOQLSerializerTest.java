/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jdoql.testdomain.Book;
import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.QBook;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.jdoql.testdomain.QStore;
import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.Expr;

public class JDOQLSerializerTest {

    private QBook book = QBook.book;

    private QProduct product = QProduct.product;
    
    private QStore store = QStore.store;

    @Test
    public void instanceOf() {
        assertEquals(
                "product instanceof com.mysema.query.jdoql.testdomain.Book",
                serialize(product.instanceOf(Book.class)));
    }

    @Test
    public void eq() {
        assertEquals("this.name == product.name", serialize(book.name.eq(product.name)));
        assertEquals("this == product", serialize(book.eq(product)));
    }
    
    @Test
    public void aggregation(){
        assertEquals("sum(product.price)", serialize(Grammar.sum(product.price)));
        assertEquals("min(product.price)", serialize(Grammar.min(product.price)));
        assertEquals("max(product.price)", serialize(Grammar.max(product.price)));
        assertEquals("avg(product.price)", serialize(Grammar.avg(product.price)));
        assertEquals("count(product.price)", serialize(Grammar.count(product.price)));
    }

    @Test
    public void booleanTests() {
        // boolean
        assertEquals("product.name == a1 && product.price <= a2",
                serialize(product.name.eq("Sony Discman").and(product.price.loe(300.00))));
        assertEquals("product.name == a1 || product.price <= a2",
                serialize(product.name.eq("Sony Discman").or(product.price.loe(300.00))));
        assertEquals("!(product.name == a1)", serialize(product.name.eq(
                "Sony MP3 player").not()));
    }

    @Test
    public void collectionTests() {
        Product product = new Product();
        // collection
        assertEquals("store.products.contains(a1)",
                serialize(store.products.contains(product)));
        assertEquals("store.products.get(0) == a1",
                serialize(store.products.get(0).eq(product)));
        assertEquals("store.products.isEmpty()",
                serialize(store.products.isEmpty()));
        assertEquals("!store.products.isEmpty()",
                serialize(store.products.isNotEmpty()));
        assertEquals("store.products.size() == a1",
                serialize(store.products.size().eq(1)));
    }

    @Test
    public void mapTests() {        
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
    public void numericTests() {
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
    public void stringTests() {
        // string
        assertEquals("product.name.startsWith(a1)", serialize(product.name.startsWith("Sony Discman")));
        assertEquals("product.name.endsWith(a1)", serialize(product.name.endsWith("Discman")));
        // FIXME assertEquals("like", 1,
        // serialize(product.name.like("Sony %")));
        assertEquals("product.name.toLowerCase() == a1", serialize(product.name.lower().eq("sony discman")));
        assertEquals("product.name.toUpperCase() == a1", serialize(product.name.upper().eq("SONY DISCMAN")));
        assertEquals("product.name.indexOf(a1) == a2", serialize(product.name.indexOf("S").eq(0)));
        // TODO indexOf
        // TODO matches
        assertEquals("product.name.substring(a1,a2) == a3", serialize(product.name.substring(0, 4).eq("Sony")));
        assertEquals("product.name.substring(a1) == a2", serialize(product.name.substring(5).eq("Discman")));
    }

    private String serialize(Expr<?> expr) {
        return new JDOQLSerializer(JDOQLPatterns.DEFAULT, book).handle(expr).toString();
    }

}
