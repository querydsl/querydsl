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
package com.mysema.query.types.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.JavaTemplates;
import com.mysema.query.types.Templates;
import com.mysema.query.types.ToStringVisitor;

public class SubQueryTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        Templates templates = new JavaTemplates();
        QueryMetadata metadata = new DefaultQueryMetadata();
        List<ExtendedSubQueryExpression> subQueries = Arrays.<ExtendedSubQueryExpression>asList(
                new BooleanSubQuery(metadata),
                new ComparableSubQuery(Date.class,metadata),
                new DateSubQuery(Date.class,metadata),
                new DateTimeSubQuery(Date.class,metadata),
                new ListSubQuery(Date.class,metadata),
                new NumberSubQuery(Integer.class,metadata),
                new SimpleSubQuery(String.class,metadata),
                new StringSubQuery(metadata),
                new TimeSubQuery(Date.class,metadata)
        );
        ExtendedSubQueryExpression prev = null;
        for (ExtendedSubQueryExpression sq : subQueries) {
            assertNotNull(sq);
            assertNotNull(sq.exists());
            assertNotNull(sq.getMetadata());
            assertNotNull(sq.notExists());
            assertEquals(sq, sq);
            if (prev != null) {
                assertEquals(sq, prev);
            }
            //assertEquals(sq.getType().hashCode(), sq.hashCode());
            sq.accept(ToStringVisitor.DEFAULT, templates);
            prev = sq;
        }
    }
    
}
