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
package com.querydsl.core.support;

import static com.querydsl.core.alias.Alias.$;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.EntityPath;

public class DetachableQueryTest {
    
    private QueryMixin<?> query;
    
    private DetachableQuery<?> detachable;
    
    @Before
    @SuppressWarnings({"rawtypes", "unchecked"}) //not interested for testing purposes
    public void setUp() {
        query = new QueryMixin();
        detachable = new DetachableQuery(query);
    }
    
    @Test
    public void Projections() {
        DummyEntity e = Alias.alias(DummyEntity.class);
        query.from((EntityPath)$(e));
        assertNotNull(detachable.exists());
        assertNotNull(detachable.notExists());
        assertNotNull(detachable.list($(e)));
        assertNotNull(detachable.unique($(e)));
        assertNotNull(detachable.list($(e), $(e.getOther())));
        assertNotNull(detachable.unique($(e), $(e.getOther())));
    }

}
