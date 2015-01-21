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
package com.querydsl.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.Expression;

public abstract class AbstractQueryTest {

    protected final Cat c1 = new Cat("Kitty");

    protected final Cat c2 = new Cat("Bob");

    protected final Cat c3 = new Cat("Alex");

    protected final Cat c4 = new Cat("Francis");

    protected final QCat cat = new QCat("cat");

    protected final QCat kitten = new QCat("kitten");

    protected final QCat offspr = new QCat("offspr");

    protected final QCat otherCat = new QCat("otherCat");
    
    protected final QCat mate = new QCat("mate");
    
    protected List<Cat> cats = Arrays.asList(c1, c2, c3, c4);

    protected List<Integer> ints = new ArrayList<Integer>();

    protected List<Integer> myInts = new ArrayList<Integer>();

    protected TestQuery last;

    
    @Before
    public void setUp() {
        myInts.addAll(Arrays.asList(1, 2, 3, 4));
        Alias.resetAlias();
    }

    protected List<Cat> cats(int size) {
        List<Cat> cats = new ArrayList<Cat>(size);
        for (int i = 0; i < size / 2; i++) {
            cats.add(new Cat("Kate" + (i + 1)));
            cats.add(new Cat("Bob" + (i + 1)));
        }
        return cats;
    }

    protected TestQuery query() {
        last = new TestQuery();
        return last;
    }

    static class TestQuery extends AbstractCollQuery<TestQuery> {

        List<Object> res = new ArrayList<Object>();

        public TestQuery() {
            super(new DefaultQueryMetadata(), DefaultQueryEngine.getDefault());
        }
        
        @Override
        public QueryMetadata getMetadata() {
            return queryMixin.getMetadata();
        }
        
        @Override
        public <RT> List<RT> list(Expression<RT> projection) {
            boolean array = projection.getType().isArray();
            List<RT> rv = super.list(projection);
            for (Object o : rv) {
                //System.out.println(array ? Arrays.toString((Object[])o) : o);
                res.add(o);
            }
            //System.out.println();
            return rv;
        }

    }
}
