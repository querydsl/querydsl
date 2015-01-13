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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.BooleanBuilder;

public class BooleanOperationsTest extends AbstractQueryTest {

    @Test
    public void BooleanOperations_Or() {
        assertToString("cust is null or cat is null", cust.isNull().or(cat.isNull()));
    }
    
    @Test
    public void BooleanOperations_And() {
        assertToString("cust is null and cat is null", cust.isNull().and(cat.isNull()));
    }
    
    
    @Test
    public void BooleanOperations_Not() {
        assertToString("not (cust is null)", cust.isNull().not());
    }
    
    
    @Test
    public void BooleanOperations2_And() {
        cat.name.eq(cust.name.firstName).and(cat.bodyWeight.eq(kitten.bodyWeight));
    }
    
    @Test
    public void BooleanOperations2_Or() {
        cat.name.eq(cust.name.firstName).or(cat.bodyWeight.eq(kitten.bodyWeight));
    }

    @Test
    public void LogicalOperations_Or() {
        assertToString("cat = kitten or kitten = cat", cat.eq(kitten).or(kitten.eq(cat)));
    }
    
    @Test
    public void LogicalOperations_And() {
        assertToString("cat = kitten and kitten = cat", cat.eq(kitten).and(kitten.eq(cat)));
    }
    
    @Test
    public void LogicalOperations_And2() {
        assertToString("cat is null and (kitten is null or kitten.bodyWeight > ?1)",
                cat.isNull().and(kitten.isNull().or(kitten.bodyWeight.gt(10))));
    }

    @Test
    public void BooleanBuilder1() {
        BooleanBuilder bb1 = new BooleanBuilder();
        bb1.and(cat.eq(cat));

        BooleanBuilder bb2 = new BooleanBuilder();
        bb2.or(cat.eq(cat));
        bb2.or(cat.eq(cat));

        assertToString("cat = cat and (cat = cat or cat = cat)", bb1.and(bb2));
    }

    @Test
    public void BooleanBuilder2() {
        BooleanBuilder bb1 = new BooleanBuilder();
        bb1.and(cat.eq(cat));

        BooleanBuilder bb2 = new BooleanBuilder();
        bb2.or(cat.eq(cat));
        bb2.or(cat.eq(cat));

        assertToString("cat = cat and (cat = cat or cat = cat)", bb1.and(bb2.getValue()));
    }

    @Test
    public void BooleanBuilder_With_Null_In_Where() {
        assertEquals("select cat\nfrom Cat cat", sub().from(cat).where(new BooleanBuilder()).toString());
    }
    
    @Test
    public void BooleanBuilder_With_Null_In_Having() {
        assertEquals("select cat\nfrom Cat cat\ngroup by cat.name",
                sub().from(cat).groupBy(cat.name).having(new BooleanBuilder()).toString());
    }

}
