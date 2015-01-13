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

import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.hibernate.HibernateSubQuery;
import com.querydsl.core.types.QTuple;
import com.querydsl.core.types.SubQueryExpression;
import org.junit.Ignore;
import org.junit.Test;

public class TupleTest extends AbstractQueryTest {
        
    @Test
    @Ignore // FIXME
    public void test() {
        QCat cat = QCat.cat;
        
        SubQueryExpression<?> subQuery = subQuery().from(cat)
        .where(subQuery()
           .from(cat)
           .groupBy(cat.mate)
           .list(new QTuple(cat.mate, cat.birthdate.max()))
             .contains(new QTuple(cat.mate, cat.birthdate)))
        .list(new QTuple(cat.birthdate, cat.name, cat.mate));
        
        assertToString(
                "(select cat.birthdate, cat.name, cat.mate from Cat cat " +
                "where (cat.mate, cat.birthdate) in " +
                    "(select cat.mate, max(cat.birthdate) from Cat cat group by cat.mate))", subQuery);
    }

    private HibernateSubQuery subQuery() {
        return new HibernateSubQuery();
    }
    
}
