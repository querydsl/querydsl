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
        query().from(product).where(query().from(other).select(other).exists()).select(product).fetch();
    }

    @Test
    public void List_NotExists() {
        query().from(product).where(query().from(other).select(other).notExists()).select(product).fetch();
    }

    @Test
    public void List_Contains() {
        query().from(product).where(product.name.in(query().from(other).select(other.name))).select(product).fetch();
    }

    @Test
    public void Gt_Subquery() {
        for (double price : query().from(product)
                .where(product.price.gt(query().from(other).select(other.price.avg())))
                .select(product.price).fetch()) {
            System.out.println(price);
        }
    }

    @Test
    public void Gt_Subquery_with_Condition() {
        for (double price : query().from(product)
                .where(product.price.gt(query().from(other).where(other.name.eq("XXX")).select(other.price.avg())))
                .select(product.price).fetch()) {
            System.out.println(price);
        }
    }

    @Test
    public void Eq_Subquery() {
        for (double price : query().from(product)
                .where(product.price.eq(query().from(other).select(other.price.avg())))
                .select(product.price).fetch()) {
            System.out.println(price);
        }
    }


    @Test
    public void In_Subquery() {
        for (double price : query().from(product)
                .where(product.price.in(
                        query().from(other).where(other.name.eq("Some name")).select(other.price)))
                .select(product.price).fetch()) {
            System.out.println(price);
        }
    }

    @Test
    public void Count() {
        for (double price : query().from(product)
                .where(query().from(other).where(other.price.gt(product.price)).select(other.count()).gt(0L))
                .select(product.price).fetch()) {
            System.out.println(price);
        }
    }

    @Test
    public void Exists() {
        for (double price : query().from(product)
                .where(query().from(other).where(other.price.gt(product.price)).exists())
                .select(product.price).fetch()) {
            System.out.println(price);
        }
    }

    @Test
    public void Not_Exists() {
        for (double price : query().from(product)
                .where(query().from(other).where(other.price.gt(product.price)).notExists())
                .select(product.price).fetch()) {
            System.out.println(price);
        }
    }

    @Test
    public void In_List() {
        query().from(product)
               .where(product.name.in(
                       query().from(other).where(other.description.eq("AAA")).select(other.name)),
                      product.description.eq("BBB"))
               .select(product).fetch();
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
