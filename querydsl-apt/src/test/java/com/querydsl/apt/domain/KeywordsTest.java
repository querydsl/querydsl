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
package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;

import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Entity;

import org.junit.Test;

public class KeywordsTest {

    @Entity
    public static class Order {

    }

    @Entity
    public static class From {

        String from1;
    }

    @Entity
    public static class NonKeyword {

    }

    @PersistenceCapable
    public static class Distinct {

        String distinct;

    }

    @Test
    public void test() {
        assertEquals("order1", QKeywordsTest_Order.Constants.order.toString());
        assertEquals("from1", QKeywordsTest_From.Constants.from.toString());
        assertEquals("nonKeyword", QKeywordsTest_NonKeyword.Constants.nonKeyword.toString());
        assertEquals("distinct1", QKeywordsTest_Distinct.Constants.distinct1.toString());
    }

}
