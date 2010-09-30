package com.mysema.query.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.SearchResults;
import com.mysema.query.jdo.sql.JDOSQLQuery;
import com.mysema.query.jdo.test.domain.Product;
import com.mysema.query.jdo.test.domain.sql.SProduct;
import com.mysema.query.sql.HSQLDBTemplates;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.expr.BooleanExpression;

public class JDOSQLQueryTest extends AbstractJDOTest{
    
    private final SQLTemplates sqlTemplates = new HSQLDBTemplates();
    
    private JDOSQLQuery sql(){
        return new JDOSQLQuery(pm, sqlTemplates);
    }
    

    @Test
    public void Count(){
        SProduct product = SProduct.product;
        
        // total
        assertEquals(30l, sql().from(product).count());
        
        // startsWith
        assertEquals(10l, sql().from(product).where(product.name.startsWith("A")).count());
        assertEquals(10l, sql().from(product).where(product.name.startsWith("B")).count());
        assertEquals(10l, sql().from(product).where(product.name.startsWith("C")).count());
        
        // eq
        for (int i = 0; i < 10; i++) {
            assertEquals(1l, sql().from(product).where(product.name.eq("A"+i)).count());
            assertEquals(1l, sql().from(product).where(product.name.eq("B"+i)).count());
            assertEquals(1l, sql().from(product).where(product.name.eq("C"+i)).count());
        }
        
    }
    
    

    @Test
    public void ScalarQueries(){
        SProduct product = SProduct.product;
        BooleanExpression filter = product.name.startsWith("A");
        
        // count
        assertEquals(10l, sql().from(product).where(filter).count());

        // countDistinct
        assertEquals(10l, sql().from(product).where(filter).countDistinct());

        // list
        assertEquals(10, sql().from(product).where(filter).list(product.name).size());

        // list with limit
        assertEquals(3, sql().from(product).limit(3).list(product.name).size());

        // list with offset
//        assertEquals(7, sql().from(product).offset(3).list(product.name).size());

        // list with limit and offset
        assertEquals(3, sql().from(product).offset(3).limit(3).list(product.name).size());

        // list multiple
        for (Object[] row : sql().from(product).list(product.productId, product.name, product.amount)){
            assertNotNull(row[0]);
            assertNotNull(row[1]);
            assertNotNull(row[2]);
        }

        // listResults
        SearchResults<String> results = sql().from(product).limit(3).listResults(product.name);
        assertEquals(3, results.getResults().size());
        assertEquals(30l, results.getTotal());

    }

    @Test
    public void EntityProjections(){
        SProduct product = SProduct.product;

        List<Product> products = sql()
            .from(product)
            .list(ConstructorExpression.create(Product.class, product.name, product.description, product.price, product.amount));
        assertEquals(30, products.size());
        for (Product p : products){
            assertNotNull(p.getName());
            assertNotNull(p.getDescription());
            assertNotNull(p.getPrice());
            assertNotNull(p.getAmount());
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
                pm.makePersistent(new Product("C" + i, "F", 200.00, 2));
                pm.makePersistent(new Product("B" + i, "E", 400.00, 4));
                pm.makePersistent(new Product("A" + i, "D", 600.00, 6));
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
