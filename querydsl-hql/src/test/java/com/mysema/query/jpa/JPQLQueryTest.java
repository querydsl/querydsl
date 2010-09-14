package com.mysema.query.jpa;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.hibernate.HibernateQuery;

public class JPQLQueryTest {

    private QCat cat = QCat.cat;
    
    private JPQLQuery query = new HibernateQuery();
    
    @Before
    public void setUp(){
        query.from(cat);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInnerJoinPEntityOfPPEntityOfP() {
        query.innerJoin(cat.mate, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInnerJoinPathOfQextendsCollectionOfPPathOfP() {
        query.innerJoin(cat.kittens, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testJoinPEntityOfPPEntityOfP() {
        query.join(cat.mate, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testJoinPathOfQextendsCollectionOfPPathOfP() {
        query.join(cat.kittens, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLeftJoinPEntityOfPPEntityOfP() {
        query.leftJoin(cat.mate, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLeftJoinPathOfQextendsCollectionOfPPathOfP() {
        query.leftJoin(cat.kittens, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFullJoinPEntityOfPPEntityOfP() {
        query.fullJoin(cat.mate, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFullJoinPathOfQextendsCollectionOfPPathOfP() {
        query.fullJoin(cat.kittens, cat.mate);
    }


}
