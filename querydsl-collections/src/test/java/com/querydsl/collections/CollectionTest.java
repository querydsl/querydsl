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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CollectionTest {
    
    private final QCat cat = new QCat("cat");
    
    private final QCat other = new QCat("other");
    
    private List<Cat> cats;
    
    @Before
    public void setUp() {
        Cat cat1 = new Cat("1");
        cat1.setKittens(Arrays.asList(cat1));
        Cat cat2 = new Cat("2");
        cat2.setKittens(Arrays.asList(cat1, cat2));
        Cat cat3 = new Cat("3");
        cat3.setKittens(Arrays.asList(cat1, cat2, cat3));
        Cat cat4 = new Cat("4");
        cat4.setKittens(Arrays.asList(cat1, cat2, cat3, cat4));
        
        cats = Arrays.asList(cat1, cat2, cat3, cat4);
    }
    
    @Test
    public void Join() {
        assertEquals("4", CollQueryFactory.from(cat, cats)
                .innerJoin(cat.kittens, other)
                .where(other.name.eq("4")).uniqueResult(cat.name));
    }
    
    @Test
    public void Join_From_Two_Sources() {
        QCat cat_kittens = new QCat("cat_kittens");
        QCat other_kittens = new QCat("other_kittens");
        assertEquals(30, CollQueryFactory
                .from(cat, cats).from(other, cats)
                .innerJoin(cat.kittens, cat_kittens)
                .innerJoin(other.kittens, other_kittens)
                .where(cat_kittens.eq(other_kittens)).count());
    }
    
    @Test
    public void Any_UniqueResult() {
        assertEquals("4", CollQueryFactory.from(cat, cats)
                .where(cat.kittens.any().name.eq("4")).uniqueResult(cat.name));
    }
    
    @Test
    public void Any_Count() {
        assertEquals(4, CollQueryFactory.from(cat, cats)
                .where(cat.kittens.any().name.isNotNull()).count());
    }
    
    @Test
    public void Any_Two_Levels() {
        assertEquals(4, CollQueryFactory.from(cat, cats).where( 
                cat.kittens.any().kittens.any().isNotNull()).count());
    }
    
    @Test
    public void Any_Two_Levels2() {
        assertEquals(4, CollQueryFactory.from(cat, cats).where(
                cat.kittens.any().name.isNotNull(), 
                cat.kittens.any().kittens.any().isNotNull()).count());
    }
    
    @Test
    public void Any_From_Two_Sources() {
        assertEquals(16, CollQueryFactory.from(cat, cats).from(other, cats).where(
                cat.kittens.any().name.eq(other.kittens.any().name)).count());
    }
    
    @Test
    public void List_Size() {
        assertEquals(4, CollQueryFactory.from(cat, cats).where(cat.kittens.size().gt(0)).count());
        assertEquals(2, CollQueryFactory.from(cat, cats).where(cat.kittens.size().gt(2)).count());
    }
    
    @Test
    public void List_Is_Empty() {
        assertEquals(0, CollQueryFactory.from(cat, cats).where(cat.kittens.isEmpty()).count());
    }
    
}
