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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.*;
import com.querydsl.jdo.AbstractJDOTest;
import com.querydsl.jdo.test.domain.Product;
import com.querydsl.jdo.test.domain.QProduct;
import com.querydsl.jdo.test.domain.QStore;
import com.querydsl.jdo.test.domain.Store;
import com.querydsl.core.types.ArrayConstructorExpression;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QTuple;
import com.querydsl.core.types.expr.Param;

public class JDOQueryStandardTest extends AbstractJDOTest {

    public static class Projection {

        public Projection(String str) {}

    }

    private static final Date publicationDate;

    private static final java.sql.Date date;

    private static final java.sql.Time time;

    static{
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.MILLISECOND, 0);
        publicationDate = cal.getTime();
        date = new java.sql.Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }

    private static String productName = "ABCD";

    private static String otherName = "ABC0";

    @BeforeClass
    public static void doPersist() {
        // Persistence of a Product and a Book.
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < 10; i++) {
                // Product instances
                pm.makePersistent(new Product("ABC" + i, "F" + i, i * 200.00, 2, publicationDate));
                pm.makePersistent(new Product("DEF" + i, "E" + i, i * 200.00, 4, publicationDate));
                pm.makePersistent(new Product("GHI" + i, "D" + i, i * 200.00, 6, publicationDate));

                // Product of Store
                Product product = new Product(productName,"A",100.0,1, publicationDate);
                pm.makePersistent(product);

                // Store instances
                Store store = new Store();
                store.getProducts().add(product);
                store.getProductsByName().put(productName, product);
                pm.makePersistent(store);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        System.out.println("");

    }

    private final QueryExecution standardTest = new QueryExecution(Module.JDO, Target.H2) {
        @Override
        protected Pair<Projectable, Expression<?>[]> createQuery() {
            return Pair.of(
                (Projectable)query().from(store, product, otherProduct),
                new Expression<?>[]{store, product, otherProduct});
        }
        @Override
        protected Pair<Projectable, Expression<?>[]> createQuery(Predicate filter) {
            return Pair.of(
                (Projectable)query().from(store, product, otherProduct).where(filter),
                new Expression<?>[]{store, product, otherProduct});
        }
    };

    private final QProduct product = QProduct.product;

    private final QProduct otherProduct = new QProduct("otherProduct");

    private final QStore store = QStore.store;

    private final QStore otherStore = new QStore("otherStore");

    @Test
    public void StandardTest() {
        Product p = query().from(product).where(product.name.eq(productName)).limit(1).uniqueResult(product);
        Product p2 = query().from(product).where(product.name.startsWith(otherName)).limit(1).uniqueResult(product);
        standardTest.noProjections();
        standardTest.noCounts();

        standardTest.runBooleanTests(product.name.isNull(), otherProduct.price.lt(10.00));
        standardTest.runCollectionTests(store.products, otherStore.products, p, p2);
        standardTest.runDateTests(product.dateField, otherProduct.dateField, date);
        standardTest.runDateTimeTests(product.publicationDate, otherProduct.publicationDate, publicationDate);
        // NO list support in JDOQL
//        testData.listTests(store.products, otherStore.products, p);
        standardTest.runMapTests(store.productsByName, otherStore.productsByName, productName, p, "X", p2);
        standardTest.runNumericCasts(product.price, otherProduct.price, 200.0);
        standardTest.runNumericTests(product.amount, otherProduct.amount, 2);
        standardTest.runStringTests(product.name, otherProduct.name, productName);
        standardTest.runTimeTests(product.timeField, otherProduct.timeField, time);

        standardTest.report();
    }

    @Test
    public void TupleProjection() {
        List<Tuple> tuples = query().from(product).list(new QTuple(product.name, product.price));
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            assertNotNull(tuple);
            assertNotNull(tuple.get(product.name));
            assertNotNull(tuple.get(product.price));
            assertNotNull(tuple.get(0,String.class));
            assertNotNull(tuple.get(1,Double.class));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    @Ignore
    public void ArrayProjection() {
        // typed array not supported
        List<String[]> results = query().from(store)
                .list(new ArrayConstructorExpression<String>(String[].class, store.name));
        assertFalse(results.isEmpty());
        for (String[] result : results) {
            assertNotNull(result);
            assertNotNull(result[0]);
        }
    }

    @Test
    @Ignore
    public void ConstructorProjection() {
        List<Projection> results = query().from(store)
                .list(ConstructorExpression.create(Projection.class, store.name));
        assertFalse(results.isEmpty());
        for (Projection result : results) {
            assertNotNull(result);
        }
    }

    @Test
    public void Params() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("ABC0",query().from(product).where(product.name.eq(name)).set(name, "ABC0")
                .uniqueResult(product.name));
    }

    @Test
    public void Params_anon() {
        Param<String> name = new Param<String>(String.class);
        assertEquals("ABC0",query().from(product).where(product.name.eq(name)).set(name, "ABC0")
                .uniqueResult(product.name));
    }

    @Test(expected=ParamNotSetException.class)
    public void Params_not_set() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("ABC0",query().from(product).where(product.name.eq(name))
                .uniqueResult(product.name));
    }

    @Test
    public void Exists() {
        assertTrue(query().from(product).where(product.name.eq("ABC0")).exists());
    }

    @Test
    public void NotExists() {
        assertTrue(query().from(product).where(product.name.eq("XXX")).notExists());
    }
}
