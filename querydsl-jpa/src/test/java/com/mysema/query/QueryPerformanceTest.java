package com.mysema.query;

import static org.junit.Assert.assertNotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
public class QueryPerformanceTest {

    private static final int iterations = 1000;
    
    private EntityManager entityManager;
    
    @BeforeClass
    public static void setUpClass() {
        Mode.mode.set("h2");
        Mode.target.set(Target.H2);
    }
    
    private JPAQuery query() {
        return new JPAQuery(entityManager);
    }
        
    @Before
    public void setUp() {
        if (query().from(QCat.cat).notExists()) {
            for (int i = 0; i < iterations; i++) {
                entityManager.persist(new Cat(String.valueOf(i), i + 100));
            }         
            entityManager.flush();    
        }        
    }
    
    @Test
    public void test() {
        Map<String, Long> results = new LinkedHashMap<String, Long>();
        
        // by id - raw
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Cat cat = (Cat)entityManager.createQuery("select cat from Cat cat where id = ?")
                .setParameter(1, i + 100).getSingleResult();
            assertNotNull(cat);            
        }
        results.put("by id - raw", System.currentTimeMillis() - start);
        
        // by - dsl
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            QCat cat = QCat.cat;
            Cat c = query().from(cat).where(cat.id.eq(i+100)).uniqueResult(cat);
            assertNotNull(c);
        }
        results.put("by id - dsl", System.currentTimeMillis() - start);
        
        // by id - raw
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Object[] row = (Object[])entityManager.createQuery("select cat.id, cat.name from Cat cat where id = ?")
                .setParameter(1, i + 100).getSingleResult();
            assertNotNull(row);            
        }
        results.put("by id - 2 cols - raw", System.currentTimeMillis() - start);
        
        // by id - dsl
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            QCat cat = QCat.cat;
            Tuple row = query().from(cat).where(cat.id.eq(i+100)).uniqueResult(cat.id, cat.name);
            assertNotNull(row);
        }
        results.put("by id - 2 cols - dsl", System.currentTimeMillis() - start);
        
        
        for (Map.Entry<String, Long> entry : results.entrySet()) {
            System.err.println(entry.getKey() + " " + entry.getValue());
        }
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    
}
