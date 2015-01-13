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
package com.querydsl.core.types.query;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.types.PathImpl;

public class ListSubQueryTest {

    @Test
    public void As() {
        ListSubQuery<Date> subQuery = new ListSubQuery<Date>(Date.class,new DefaultQueryMetadata()); 
        assertNotNull(subQuery.as(new PathImpl<Date>(Date.class,"a")));
        assertNotNull(subQuery.as(new PathImpl<Date>(Date.class,"a")));
    }
 
    @Test
    public void Count() {
        DefaultQueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, new PathImpl<Object>(Object.class, "path"));
        ListSubQuery<Date> subQuery = new ListSubQuery<Date>(Date.class, md);
        assertNotNull(subQuery.count().toString());
    }
    
    @Test
    public void Count_Distinct() {
        DefaultQueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, new PathImpl<Object>(Object.class, "path"));
        ListSubQuery<Date> subQuery = new ListSubQuery<Date>(Date.class, md);
        assertNotNull(subQuery.count().toString());
    }
    
}
