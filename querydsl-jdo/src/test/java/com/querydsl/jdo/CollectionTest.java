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
import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.jdo.test.domain.Book;
import com.querydsl.jdo.test.domain.Product;
import com.querydsl.jdo.test.domain.QProduct;
import com.querydsl.jdo.test.domain.QStore;

public class CollectionTest extends AbstractJDOTest {
    
    private final QStore store = QStore.store;
    
    @Test
    public void Contains_Key() {
        query(store, store.productsByName.containsKey("XXX"));
    }
    
    @Test
    public void Contains_Value() {
        Product product = query().from(QProduct.product).list(QProduct.product).get(0);
        query(store, store.productsByName.containsValue(product));
    }
    
    @Test
    @Ignore
    public void Get() {
        query(store, store.products.get(0).name.isNotNull());
    }
    
    @Test
    public void isEmpty() {
        query(store, store.products.isEmpty());
    }    

    @Test
    public void isNotEmpty() {
        query(store, store.products.isNotEmpty());
    }
    
    @Test
    public void Size() {
        query(store, store.products.size().gt(0));
    }
    
    @Test
    public void Collection_Any() {
        query(store, store.products.any().name.eq("Sony Discman"));
    }
    
    @Test
    public void Collection_Any_And() {
        query(store, store.products.any().name.eq("Sony Discman").and(store.products.any().price.gt(10.0)));
    }

    @Test
    public void Collection_Any_Count() {
        query().from(store).where(store.products.any().name.eq("Sony Discman")).count();
    }
    
    @Test
    @Ignore // Not supported
    public void Collection_Any_In_Projection() {
        query().from(store).list(store.products.any());
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
