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
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.testutil.JPATestRunner;

@Ignore
@RunWith(JPATestRunner.class)
public class JPAQueryMutabilityTest implements JPATest {

    private EntityManager entityManager;

    protected JPAQuery query() {
        return new JPAQuery(entityManager);
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    public void test() {
        QCat cat = QCat.cat;
        JPAQuery query = query().from(cat);

        query.count();
        assertProjectionEmpty(query);
        query.distinct().count();
        assertProjectionEmpty(query);

        query.iterate(cat);
        assertProjectionEmpty(query);
        query.iterate(cat,cat);
        assertProjectionEmpty(query);
        query.distinct().iterate(cat);
        assertProjectionEmpty(query);
        query.distinct().iterate(cat,cat);
        assertProjectionEmpty(query);

        query.list(cat);
        assertProjectionEmpty(query);
        query.list(cat,cat);
        assertProjectionEmpty(query);
        query.distinct().list(cat);
        assertProjectionEmpty(query);
        query.distinct().list(cat,cat);
        assertProjectionEmpty(query);

        query.listResults(cat);
        assertProjectionEmpty(query);
        query.distinct().listResults(cat);
        assertProjectionEmpty(query);

        query.map(cat.name, cat);
        assertProjectionEmpty(query);
    }

    @Test
    public void Clone() {
        QCat cat = QCat.cat;
        JPAQuery query = query().from(cat).where(cat.name.isNotNull());
        JPAQuery query2 = query.clone(entityManager);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(cat);
    }

    private void assertProjectionEmpty(JPAQuery query) {
        assertTrue(query.getMetadata().getProjection().isEmpty());
    }

}
