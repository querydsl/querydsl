/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import static com.querydsl.core.types.dsl.Expressions.numberOperation;
import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.querydsl.core.types.Operator;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
public class JPAQueryMutability2Test implements JPATest {

    private EntityManager entityManager;

    private final Operator customOperator = new Operator() {
        public String name() { return "custom"; }
        public String toString() { return name(); }
        public Class<?> getType() { return Object.class; }
    };

    private final JPQLTemplates customTemplates = new HQLTemplates() {{
            add(customOperator, "sign({0})");
    }};

    protected JPAQuery<?> query() {
        return new JPAQuery<Void>(entityManager);
    }

    protected JPAQuery<?> query(JPQLTemplates templates) {
        return new JPAQuery<Void>(entityManager, templates);
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    public void test() {
        QCat cat = QCat.cat;
        JPAQuery<?> query = query().from(cat);

        query.fetchCount();
        query.distinct().fetchCount();

        query.select(cat).iterate();
        query.select(cat, cat).iterate();
        query.distinct().select(cat).iterate();
        query.distinct().select(cat, cat).iterate();

        query.select(cat).fetch();
        query.select(cat, cat).fetch();
        query.distinct().select(cat).fetch();
        query.distinct().select(cat, cat).fetch();

        query.select(cat).fetchResults();
        query.distinct().select(cat).fetchResults();
    }

    @Test
    public void Clone() {
        QCat cat = QCat.cat;
        JPAQuery<?> query = query().from(cat).where(cat.name.isNotNull());
        JPAQuery<?> query2 = query.clone(entityManager);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.select(cat).fetch();
    }

    @Test
    public void Clone_Custom_Templates() {
        QCat cat = QCat.cat;
        JPAQuery<?> query = query().from(cat);
        //attach using the custom templates
        query.clone(entityManager, customTemplates)
                .select(numberOperation(Integer.class, customOperator, cat.floatProperty)).fetchOne();
    }

    @Test
    public void Clone_Keep_Templates() {
        QCat cat = QCat.cat;
        JPAQuery<?> query = query(customTemplates).from(cat);
        //keep the original templates
        query.clone()
                .select(numberOperation(Integer.class, customOperator, cat.floatProperty)).fetchOne();
    }

    @Test(expected = IllegalArgumentException.class)
    public void Clone_Lose_Templates() {
        QCat cat = QCat.cat;
        JPAQuery<?> query = query(customTemplates).from(cat);
        //clone using the entitymanager's default templates
        query.clone(entityManager)
                .select(numberOperation(Integer.class, customOperator, cat.floatProperty)).fetchOne();
    }

}
