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
package com.querydsl.jpa;

import static com.querydsl.core.support.Expressions.numberOperation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.types.OperatorImpl;
import com.querydsl.jpa.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
public class JPAQueryMutability2Test implements JPATest {

    private EntityManager entityManager;

    private final OperatorImpl<Integer> customOperator = new OperatorImpl<Integer>("CUSTOM", "SIGN");

    private final JPQLTemplates customTemplates = new HQLTemplates() {{
            add(customOperator, "sign({0})");
    }};

    protected JPAQuery query() {
        return new JPAQuery(entityManager);
    }

    protected JPAQuery query(JPQLTemplates templates) {
        return new JPAQuery(entityManager, templates);
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

    @Test
    public void Clone_Custom_Templates() {
        QCat cat = QCat.cat;
        JPAQuery query = query().from(cat);
        //attach using the custom templates
        query.clone(entityManager, customTemplates)
                .uniqueResult(numberOperation(Integer.class, customOperator, cat.floatProperty));
    }

    @Test
    public void Clone_Keep_Templates() {
        QCat cat = QCat.cat;
        JPAQuery query = query(customTemplates).from(cat);
        //keep the original templates
        query.clone()
                .uniqueResult(numberOperation(Integer.class, customOperator, cat.floatProperty));
    }

    @Test(expected = IllegalArgumentException.class)
    public void Clone_Lose_Templates() {
        QCat cat = QCat.cat;
        JPAQuery query = query(customTemplates).from(cat);
        //clone using the entitymanager's default templates
        query.clone(entityManager)
                .uniqueResult(numberOperation(Integer.class, customOperator, cat.floatProperty));
    }

    private void assertProjectionEmpty(JPAQuery query) {
        assertTrue(query.getMetadata().getProjection().isEmpty());
    }

}
