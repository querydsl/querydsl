/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.hibernate.ScrollableResultsIterator;

/**
 * @author tiwe
 *
 */
public abstract class AbstractHibernateTest extends AbstractStandardTest{

    private Session session;

    protected HibernateQuery query(){
        return new HibernateQuery(session, getTemplates());
    }

    protected JPQLTemplates getTemplates(){
        return HQLTemplates.DEFAULT;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    protected void save(Object entity) {
        session.save(entity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void QueryExposure(){
        save(new Cat());

        List results = query().from(QCat.cat).createQuery(QCat.cat).list();
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void LockMode(){
        query().from(QCat.cat).setLockMode(QCat.cat, LockMode.PESSIMISTIC_WRITE).list(QCat.cat);
    }

    @Test
    public void Scroll() throws IOException{
        CloseableIterator<Cat> cats = new ScrollableResultsIterator<Cat>(query().from(QCat.cat).createQuery(QCat.cat).scroll());
        assertTrue(cats.hasNext());
        while (cats.hasNext()){
            assertNotNull(cats.next());
        }
        cats.close();
    }

    @Test
    public void ScrollArray() throws IOException{
        CloseableIterator<Object[]> rows = new ScrollableResultsIterator<Object[]>(query()
                .from(QCat.cat)
                .createQuery(QCat.cat.name, QCat.cat.birthdate).scroll(),true);
        assertTrue(rows.hasNext());
        while (rows.hasNext()){
            Object[] row = rows.next();
            assertEquals(2, row.length);
            assertNotNull(row[0]);
            assertNotNull(row[1]);
        }
        rows.close();
    }
}
