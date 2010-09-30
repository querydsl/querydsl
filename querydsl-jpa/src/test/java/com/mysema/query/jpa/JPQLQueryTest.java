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
    public void InnerJoinPEntityOfPPEntityOfP() {
        query.innerJoin(cat.mate, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void InnerJoinPathOfQextendsCollectionOfPPathOfP() {
        query.innerJoin(cat.kittens, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void JoinPEntityOfPPEntityOfP() {
        query.join(cat.mate, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void JoinPathOfQextendsCollectionOfPPathOfP() {
        query.join(cat.kittens, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void LeftJoinPEntityOfPPEntityOfP() {
        query.leftJoin(cat.mate, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void LeftJoinPathOfQextendsCollectionOfPPathOfP() {
        query.leftJoin(cat.kittens, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void FullJoinPEntityOfPPEntityOfP() {
        query.fullJoin(cat.mate, cat.mate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void FullJoinPathOfQextendsCollectionOfPPathOfP() {
        query.fullJoin(cat.kittens, cat.mate);
    }


}
