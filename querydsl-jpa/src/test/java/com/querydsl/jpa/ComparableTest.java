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

import org.junit.Test;

public class ComparableTest extends AbstractQueryTest{

    @Test
    public void Eq() {
        assertToString("cat.bodyWeight = kitten.bodyWeight",  cat.bodyWeight.eq(kitten.bodyWeight));
    }
    
    @Test
    public void Goe() {
        assertToString("cat.bodyWeight >= kitten.bodyWeight", cat.bodyWeight.goe(kitten.bodyWeight));
    }
    
    @Test
    public void Gt() {
        assertToString("cat.bodyWeight > kitten.bodyWeight",  cat.bodyWeight.gt(kitten.bodyWeight));
    }
    
    @Test
    public void Loe() {
        assertToString("cat.bodyWeight <= kitten.bodyWeight", cat.bodyWeight.loe(kitten.bodyWeight));
    }
    
    @Test
    public void Lt() {
        assertToString("cat.bodyWeight < kitten.bodyWeight",  cat.bodyWeight.lt(kitten.bodyWeight));
    }
    
    @Test
    public void Ne() {
        assertToString("cat.bodyWeight <> kitten.bodyWeight", cat.bodyWeight.ne(kitten.bodyWeight));
    }




}
