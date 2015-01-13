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

import org.junit.BeforeClass;
import org.junit.Test;

import com.querydsl.jdo.test.domain.Product;
import com.querydsl.jdo.test.domain.QProduct;

public class SubqueriesTest extends AbstractJDOTest {
    
    private QProduct product = QProduct.product;

    private QProduct other = new QProduct("other");
    
    @Test
    public void List_Exists() {
        query().from(product).where(sub().from(other).list(other).exists()).list(product);
    }
    
    @Test
    public void List_NotExists() {
        query().from(product).where(sub().from(other).list(other).notExists()).list(product);
    }
    
    @Test
    public void List_Contains() {
        query().from(product).where(sub().from(other).list(other.name).contains(product.name)).list(product);
    }

    @Test
    public void Gt_Subquery() {
        for (double price : query().from(product)
                .where(product.price.gt(sub().from(other).unique(other.price.avg())))
                .list(product.price)) {
            System.out.println(price);
        }
    }
    
    @Test
    public void Gt_Subquery_with_Condition() {
        for (double price : query().from(product)
                .where(product.price.gt(sub().from(other).where(other.name.eq("XXX")).unique(other.price.avg())))
                .list(product.price)) {
            System.out.println(price);
        }
    }

    @Test
    public void Eq_Subquery() {
        for (double price : query().from(product)
                .where(product.price.eq(sub().from(other).unique(other.price.avg())))
                .list(product.price)) {
            System.out.println(price);
        }
    }
    

    @Test
    public void In_Subquery() {
        for (double price : query().from(product)
                .where(product.price.in(
                        sub().from(other).where(other.name.eq("Some name")).list(other.price)))
                .list(product.price)) {
            System.out.println(price);
        }
    }

    @Test
    public void Count() {
        for (double price : query().from(product)
                .where(sub().from(other).where(other.price.gt(product.price)).count().gt(0))
                .list(product.price)) {
            System.out.println(price);
        }
    }
    
    @Test
    public void Exists() {
        for (double price : query().from(product)
                .where(sub().from(other).where(other.price.gt(product.price)).exists())
                .list(product.price)) {
            System.out.println(price);
        }
    }
    
    @Test
    public void Not_Exists() {
        for (double price : query().from(product)
                .where(sub().from(other).where(other.price.gt(product.price)).notExists())
                .list(product.price)) {
            System.out.println(price);
        }
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
