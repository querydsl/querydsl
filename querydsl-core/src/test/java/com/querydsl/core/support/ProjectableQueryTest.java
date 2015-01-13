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

import static org.junit.Assert.*;

import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.QBean;

@SuppressWarnings("unchecked")
public class ProjectableQueryTest {
        
    @Test
    public void UniqueResult_Of_Array() {
       QueryMixin queryMixin = new QueryMixin();
       DummyProjectable projectable = new DummyProjectable(queryMixin);
       projectable.uniqueResult(new Expression[0]);
       assertEquals(Long.valueOf(2), queryMixin.getMetadata().getModifiers().getLimit());
    }
    
    @Test
    public void UniqueResult() {
       QueryMixin queryMixin = new QueryMixin();
       DummyProjectable projectable = new DummyProjectable(queryMixin);
       projectable.uniqueResult(new QBean(Object.class));
       assertEquals(Long.valueOf(2), queryMixin.getMetadata().getModifiers().getLimit());
    }

}
