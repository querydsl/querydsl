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
package com.mysema.query.jpa;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.domain.QEmployee;
import com.mysema.query.jpa.domain.QUser;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HibernateQueryTest {

    @Test
    public void Clone() {
        QCat cat = QCat.cat;
        BooleanBuilder emptyBooleanBuilder = new BooleanBuilder();
        HibernateQuery hq = new HibernateQuery().from(cat).where(cat.name.isNull().and(emptyBooleanBuilder));

        HibernateQuery hq2 = hq.clone();
        assertNotNull(hq2);
    }

    @Test
    public void InnerJoin() {
        HibernateQuery hqlQuery = new HibernateQuery();
        QEmployee employee = QEmployee.employee;
        hqlQuery.from(employee);
        hqlQuery.innerJoin(employee.user, QUser.user);
        assertEquals("select employee\nfrom Employee employee\n  inner join employee.user as user", hqlQuery.toString());
    }

}
