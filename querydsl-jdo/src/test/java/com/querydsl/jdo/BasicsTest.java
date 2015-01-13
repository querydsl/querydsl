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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.io.IOException;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jdo.test.domain.Book;
import com.querydsl.jdo.test.domain.Product;
import com.querydsl.jdo.test.domain.QBook;
import com.querydsl.jdo.test.domain.QProduct;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BasicsTest extends AbstractJDOTest {

    private static final JDOQLTemplates templates = new JDOQLTemplates();

    private final QBook book = QBook.book;

    private final QProduct product = QProduct.product;

    private final QProduct product2 = new QProduct("product2");

    @Test
    public void Serialization() throws IOException{
        JDOQuery query = query();

        assertEquals("FROM com.querydsl.jdo.test.domain.Product", query.from(product).toString());
        assertEquals("FROM com.querydsl.jdo.test.domain.Product" +
            "\nVARIABLES com.querydsl.jdo.test.domain.Product product2",
            query.from(product2).toString());

        query.where(product.ne(product2)).list(product, product2);
        query.close();
    }

    @Test
    public void SubQuerySerialization() throws IOException{
        JDOSubQuery query = sub();

        assertEquals("FROM com.querydsl.jdo.test.domain.Product", query.from(product).toString());
        assertEquals("FROM com.querydsl.jdo.test.domain.Product" +
            "\nVARIABLES com.querydsl.jdo.test.domain.Product product2",
            query.from(product2).toString());

    }

    @Test
    public void Delete() {
        long count = query().from(product).count();
        assertEquals(0, delete(product).where(product.name.eq("XXX")).execute());
        assertEquals(count, delete(product).execute());
    }

    @Test
    public void CountTests() {
        assertEquals("count", 2, query().from(product).count());
    }

    @Test
    public void List_Distinct() {
        query().from(product).distinct().list(product);
    }
    
    @Test
    public void List_Distinct_Two_Sources() {
        query().from(product, product2).distinct().list(product, product2);
    }
    
    @Test
    public void Single_Result() {
        query().from(product).singleResult(product);
    }
    
    @Test
    public void Single_Result_With_Array() {
        query().from(product).singleResult(new Expression<?>[]{product});
    }

    @Test
    public void FactoryExpression_In_GroupBy() {
        Expression<Product> productBean = Projections.bean(Product.class, product.name, product.description);
        assertFalse(query().from(product).groupBy(productBean).list(productBean).isEmpty());
    }

    @Test(expected=NonUniqueResultException.class)
    public void Unique_Result_Throws_Exception_On_Multiple_Results() {
        query().from(product).uniqueResult(product);
    }

    @Test
    public void SimpleTest() throws IOException{
        JDOQuery query = new JDOQuery(pm, templates, false);
        assertEquals("Sony Discman", query.from(product).where(product.name.eq("Sony Discman"))
                .uniqueResult(product.name));
        query.close();
    }

    @Test
    public void ProjectionTests() {
        assertEquals("Sony Discman", query().from(product).where(product.name.eq("Sony Discman"))
                .uniqueResult(product.name));
    }

    @Test
    public void BasicTests() {
        assertEquals("list", 2, query().from(product).list(product).size());
        assertEquals("list", 2, query().from(product).list(product.name,product.description).size());
        assertEquals("list", 1, query().from(book).list(book).size());
        assertEquals("eq", 1, query(product, product.name.eq("Sony Discman")).size());
        assertEquals("instanceof ", 1, query(product,product.instanceOf(Book.class)).size());
    }

    @Test
    @Ignore
    public void DetachedResults() {
        for (Product p : detachedQuery().from(product).list(product)) {
            System.out.println(p);
        }
    }

    @Test
    public void Empty_BooleanBuilder() {
        assertEquals("empty boolean builder", 2, query(product, new BooleanBuilder()).size());
    }

    @Test
    public void And() {
        assertEquals("and", 1, query(product, product.name.eq("Sony Discman").and(product.price.loe(300.00))).size());
    }

    @Test
    public void Or() {
        assertEquals("or", 2,  query(product, product.name.eq("Sony Discman").or(product.price.loe(300.00))).size());
    }

    @Test
    public void Not() {
        assertEquals("not", 2, query(product, product.name.eq("Sony MP3 player").not()).size());
    }

    @Test
    public void NumericTests() {
        // numeric
        // TODO +
        // TODO -
        // TODO *
        // TODO /
        // TODO %
        // TODO Math.abs
        // TODO Math.sqrt
    }

    @Test
    public void Eq() {
        assertEquals("eq", 1, query(product, product.price.eq(200.00)).size());
        assertEquals("eq", 0, query(product, product.price.eq(100.00)).size());
    }

    @Test
    public void Ne() {
        assertEquals("ne", 2, query(product, product.price.ne(100.00)).size());
    }

    @Test
    public void In_Empty() {
        assertEquals(0, query(product, product.name.in(ImmutableList.<String>of())).size());
    }

    @Test
    public void Not_In_Empty() {
        int count = query(product, product.name.isNotNull()).size();
        assertEquals(count, query(product, product.name.notIn(ImmutableList.<String>of())).size());
    }

    @Test
    public void Lt() {
        assertEquals("lt", 2, query(product, product.price.lt(300.00)).size());
    }

    @Test
    public void Gt() {
        assertEquals("gt", 1, query(product, product.price.gt(100.00)).size());
    }

    @Test
    public void Goe() {
        assertEquals("goe", 1, query(product, product.price.goe(100.00)).size());
    }

    @Test
    public void Loe() {
        assertEquals("loe", 2, query(product, product.price.loe(300.00)).size());
    }

    @Test
    public void Starts_With() {
        assertEquals("startsWith", 1, query(product,product.name.startsWith("Sony Discman")).size());
    }

    @Test
    public void Matches() {
        assertEquals("matches", 1, query(product,product.name.matches("Sony.*")).size());
    }

    @Test
    public void Like() {
        assertEquals("matches", 1, query(product,product.name.like("Sony%")).size());
    }

    @Test
    public void Ends_With() {
        assertEquals("endsWith", 1, query(product,product.name.endsWith("Discman")).size());
    }

    @Test
    public void To_LowerCase() {
        assertEquals("toLowerCase", 1, query(product,product.name.lower().eq("sony discman")).size());
    }

    @Test
    public void To_UpperCase() {
        assertEquals("toUpperCase", 1, query(product,product.name.upper().eq("SONY DISCMAN")).size());
    }

    @Test
    public void Index_Of() {
        assertEquals("indexOf", 1, query(product,product.name.indexOf("S").eq(0)).size());
    }

    @Test
    public void Substring1() {
        assertEquals("substring", 1, query(product,product.name.substring(5).eq("Discman")).size());
    }

    @Test
    public void Substring2() {
        assertEquals("substring", 1, query(product,product.name.substring(0, 4).eq("Sony")).size());
    }

    @BeforeClass
    public static void doPersist() {
        // Persistence of a Product and a Book.
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.makePersistent(new Product("Sony Discman","A standard discman from Sony", 200.00, 3));
            pm.makePersistent(new Book("Lord of the Rings by Tolkien","The classic story", 49.99, 5, "JRR Tolkien", "12345678","MyBooks Factory"));
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        System.out.println("");

    }

}
