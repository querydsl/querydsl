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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Test;

import com.querydsl.core.SearchResults;
import com.querydsl.core.Tuple;
import com.querydsl.jdo.test.domain.Product;
import com.querydsl.jdo.test.domain.QProduct;

public class OrderingTest extends AbstractJDOTest {

    private QProduct product = QProduct.product;

    @Test
    public void Order_Asc() {
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
    public void Order_Desc() {
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
        List<Tuple> rows = query().from(product).orderBy(product.name.asc())
                .list(product.name, product.description);
        assertEquals(30, rows.size());
        for (Tuple row : rows) {
            assertEquals(row.get(0, String.class).substring(1), 
                    row.get(1, String.class).substring(1));
        }
    }

    @Test
    public void Limit_Order_Asc() {
        assertEquals(Arrays.asList("A0", "A1"), 
            query().from(product).orderBy(product.name.asc()).limit(2).list(product.name));
    }
    
    @Test
    public void Limit_Order_Desc() {
        assertEquals(Arrays.asList("C9", "C8"), 
            query().from(product).orderBy(product.name.desc()).limit(2).list(product.name));
    }
    
    public void Limit_and_Offset() {
        assertEquals(Arrays.asList("A2", "A3", "A4"), 
            query().from(product).orderBy(product.name.asc()).offset(2).limit(3).list(product.name));    
    }

    @Test
    public void SearchResults() {
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
