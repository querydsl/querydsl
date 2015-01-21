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
package com.querydsl.core.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.types.path.NumberPath;

public class NumberExpressionTest {
    
    private NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "int");
    
    @Test
    public void Between_Start_Given() {
        assertEquals(intPath.goe(1L), intPath.between(1L, null));
    }
    
    @Test
    public void Between_End_Given() {
        assertEquals(intPath.loe(3L), intPath.between(null, 3L));
    }

}
