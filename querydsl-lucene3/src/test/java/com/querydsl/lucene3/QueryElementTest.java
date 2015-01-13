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
package com.querydsl.lucene3;

import static org.junit.Assert.assertEquals;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.junit.Ignore;
import org.junit.Test;

public class QueryElementTest {

    @Test
    @Ignore
    public void test() {
        QueryElement element = new QueryElement(new TermQuery(new Term("str","text")));
        assertEquals("str:text",element.toString());
        //assertEquals(element.getQuery().hashCode(), element.hashCode());

        QueryElement element2 = new QueryElement(new TermQuery(new Term("str","text")));
        assertEquals(element2, element);
    }
}
