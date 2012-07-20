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
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.hibernate.ScrollableResultsIterator;
import com.mysema.testutil.HibernateTestRunner;

/**
 * @author tiwe
 *
 */
@RunWith(HibernateTestRunner.class)
public class HibernateBase extends AbstractStandardTest {
    
    @Rule
    public static MethodRule targetRule = new TargetRule();
    
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
    public void WithComment() {
        query().from(QCat.cat).setComment("my comment").list(QCat.cat);
    }

    @Test
    public void LockMode(){
        query().from(QCat.cat).setLockMode(QCat.cat, LockMode.PESSIMISTIC_WRITE).list(QCat.cat);
    }
    
    @Test
    public void FlushMode() {
        query().from(QCat.cat).setFlushMode(org.hibernate.FlushMode.AUTO).list(QCat.cat);
    }

    @Test
    public void Scroll() throws IOException{
        CloseableIterator<Cat> cats = new ScrollableResultsIterator<Cat>(query().from(QCat.cat)
                .createQuery(QCat.cat).scroll());
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
                .createQuery(QCat.cat.name, QCat.cat.birthdate).scroll(), true);
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
