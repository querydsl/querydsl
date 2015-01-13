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

import com.querydsl.jpa.domain.Cat;

public class CollectionTest extends AbstractQueryTest{

    @Test
    public void Constant_InElements_Set() {
        assertToString("?1 in elements(cat.kittensSet)", cat.kittensSet.contains(new Cat()));
    }
    
    @Test
    public void Constant_InElements_List() {
        assertToString("?1 in elements(cat.kittens)", cat.kittens.contains(new Cat()));
    }

    @Test
    public void Path_InElements_List() {
        assertToString("cat in elements(cat1.kittens)", cat.in(cat1.kittens));
    }
    
    @Test
    public void Path_InElements_Set() {
        assertToString("cat in elements(cat1.kittensSet)", cat.in(cat1.kittensSet));
    }

    
    @Test
    public void CollectionOperations() {
        // HQL functions that take collection-valued path expressions: size(),
        // minelement(), maxelement(), minindex(), maxindex(), along with the
        // special elements() and indices functions which may be quantified
        // using some, all, exists, any, in.
        cat.kittens.size();
//        minelement(cat.kittens);
//        maxelement(cat.kittens);
//        minindex(cat.kittens);
//        maxindex(cat.kittens);
        assertToString("cat.kittens[0]", cat.kittens(0));
        assertToString("cat.kittens[0]", cat.kittens.get(0));

        // some, all, exists, any, in.
    }
}
