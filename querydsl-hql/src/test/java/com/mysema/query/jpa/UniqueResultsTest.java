/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import static com.mysema.query.hql.domain.QCat.cat;
import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.jpa.HQLQuery;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.testutil.HibernateConfig;
import com.mysema.testutil.HibernateTestRunner;

@RunWith(HibernateTestRunner.class)
@HibernateConfig("hsqldb.properties")
public class UniqueResultsTest {

    private Session session;

    @Test
    public void test(){
        session.save(new Cat("Bob1", 1));
        session.save(new Cat("Bob2", 2));
        session.save(new Cat("Bob3", 3));

        assertEquals(Integer.valueOf(1), query().from(cat).orderBy(cat.name.asc()).offset(0).limit(1).uniqueResult(cat.id));
        assertEquals(Integer.valueOf(2), query().from(cat).orderBy(cat.name.asc()).offset(1).limit(1).uniqueResult(cat.id));
        assertEquals(Integer.valueOf(3), query().from(cat).orderBy(cat.name.asc()).offset(2).limit(1).uniqueResult(cat.id));

        assertEquals(Long.valueOf(3), query().from(cat).uniqueResult(cat.count()));
    }

    private HQLQuery query(){
        return new HibernateQuery(session);
    }

    public void setSession(Session session) {
        this.session = session;
    }

}
