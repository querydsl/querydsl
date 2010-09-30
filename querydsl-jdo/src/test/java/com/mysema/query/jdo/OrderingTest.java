/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.SearchResults;
import com.mysema.query.jdo.test.domain.Product;
import com.mysema.query.jdo.test.domain.QProduct;

public class OrderingTest extends AbstractJDOTest {

    private QProduct product = QProduct.product;

    @Test
    public void OrderAsc() {
        List<String> namesAsc = query().from(product).orderBy(
                product.name.asc(), product.description.desc()).list(
                product.name);
        assertEquals(30, namesAsc.size());
        String prev = null;
        for (String name : namesAsc) {
            if (prev != null) {
                assertTrue(prev.compareTo(name) < 0);
            }
            prev = name;
        }
    }

    @Test
    public void OrderDesc() {
        List<String> namesDesc = query().from(product).orderBy(
                product.name.desc()).list(product.name);
        assertEquals(30, namesDesc.size());
        String prev = null;
        for (String name : namesDesc) {
            if (prev != null) {
                assertTrue(prev.compareTo(name) > 0);
            }
            prev = name;
        }
    }

    @Test
    public void TabularResults() {
        List<Object[]> rows = query().from(product).orderBy(product.name.asc())
                .list(product.name, product.description);
        assertEquals(30, rows.size());
        for (Object[] row : rows) {
            assertEquals(row[0].toString().substring(1), row[1].toString()
                    .substring(1));
        }
    }

    @Test
    public void paging() {
        assertEquals(Arrays.asList("A0", "A1"), query().from(product).orderBy(
                product.name.asc()).limit(2).list(product.name));
        assertEquals(Arrays.asList("A2", "A3", "A4"), query().from(product)
                .orderBy(product.name.asc()).offset(2).limit(3).list(product.name));
        assertEquals(Arrays.asList("C9", "C8"), query().from(product).orderBy(
                product.name.desc()).limit(2).list(product.name));
    }

    @Test
    public void searchResults() {
        SearchResults<String> results = query().from(product).orderBy(
                product.name.asc()).limit(2).listResults(product.name);
        assertEquals(Arrays.asList("A0", "A1"), results.getResults());
        assertEquals(30, results.getTotal());

    }

    @BeforeClass
    public static void doPersist() {
        // Persistence of a Product and a Book.
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < 10; i++) {
                pm.makePersistent(new Product("C" + i, "F" + i, i * 200.00, 2));
                pm.makePersistent(new Product("B" + i, "E" + i, i * 200.00, 4));
                pm.makePersistent(new Product("A" + i, "D" + i, i * 200.00, 6));
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

}
