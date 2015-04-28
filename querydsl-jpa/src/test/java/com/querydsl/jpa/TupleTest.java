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

import static com.querydsl.jpa.JPAExpressions.select;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.jpa.domain.QCat;

public class TupleTest extends AbstractQueryTest {
        
    @Test
    @Ignore // FIXME
    public void test() {
        QCat cat = QCat.cat;

        SubQueryExpression<?> subQuery = select(cat.birthdate, cat.name, cat.mate).from(cat)
        .where(select(cat.mate, cat.birthdate.max())
                .from(cat)
                .groupBy(cat.mate)
                .contains(Projections.tuple(cat.mate, cat.birthdate)));
        
        assertToString(
                "(select cat.birthdate, cat.name, cat.mate from Cat cat " +
                "where (cat.mate, cat.birthdate) in " +
                    "(select cat.mate, max(cat.birthdate) from Cat cat group by cat.mate))", subQuery);
    }
    
}
