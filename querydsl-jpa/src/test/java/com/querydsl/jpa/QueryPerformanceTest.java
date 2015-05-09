package com.querydsl.jpa;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.querydsl.core.Target;
import com.querydsl.core.Tuple;
import com.querydsl.core.testutil.Performance;
import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
@Ignore
@Category(Performance.class)
public class QueryPerformanceTest implements JPATest {

    private static final int iterations = 1000;

    private EntityManager entityManager;

    @BeforeClass
    public static void setUpClass() {
        Mode.mode.set("h2perf");
        Mode.target.set(Target.H2);
    }

    @AfterClass
    public static void tearDownClass() {
        Mode.mode.remove();
        Mode.target.remove();
    }

    private JPAQuery<?> query() {
        return new JPAQuery<Void>(entityManager);
    }

    @Before
    public void setUp() {
        if (query().from(QCat.cat).fetchCount() == 0) {
            for (int i = 0; i < iterations; i++) {
                entityManager.persist(new Cat(String.valueOf(i), i + 100));
            }
            entityManager.flush();
        }
    }

    @Test
    public void ById_Raw() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Cat cat = (Cat)entityManager.createQuery("select cat from Cat cat where id = ?")
                .setParameter(1, i + 100).getSingleResult();
            assertNotNull(cat);
        }
        System.err.println("by id - raw" + (System.currentTimeMillis() - start));
    }

    @Test
    public void ById_Qdsl() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            QCat cat = QCat.cat;
            Cat c = query().from(cat).where(cat.id.eq(i+100)).select(cat).fetchOne();
            assertNotNull(c);
        }
        System.err.println("by id - dsl" + (System.currentTimeMillis() - start));
    }

    @Test
    public void ById_TwoCols_Raw() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Object[] row = (Object[])entityManager.createQuery(
                    "select cat.id, cat.name from Cat cat where id = ?")
                .setParameter(1, i + 100).getSingleResult();
            assertNotNull(row);
        }
        System.err.println("by id - 2 cols - raw" + (System.currentTimeMillis() - start));
    }

    @Test
    public void ById_TwoCols_Qdsl() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            QCat cat = QCat.cat;
            Tuple row = query().from(cat).where(cat.id.eq(i+100)).select(cat.id, cat.name).fetchOne();
            assertNotNull(row);
        }
        System.err.println("by id - 2 cols - dsl" + (System.currentTimeMillis() - start));
    }


    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


}
