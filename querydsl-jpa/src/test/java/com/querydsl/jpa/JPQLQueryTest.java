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

import org.junit.Before;
import org.junit.Test;

import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.hibernate.HibernateQuery;

public class JPQLQueryTest {

    private QCat cat = QCat.cat;
    
    private HibernateQuery query = new HibernateQuery();
    
    @Before
    public void setUp() {
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
