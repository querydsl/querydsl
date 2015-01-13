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
package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;

import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Entity;

import org.junit.Assert;
import org.junit.Test;

import com.querydsl.apt.domain.QKeywordsTest_Distinct;
import com.querydsl.apt.domain.QKeywordsTest_From;
import com.querydsl.apt.domain.QKeywordsTest_NonKeyword;
import com.querydsl.apt.domain.QKeywordsTest_Order;

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
        Assert.assertEquals("order1", QKeywordsTest_Order.order.toString());
        Assert.assertEquals("from1", QKeywordsTest_From.from.toString());
        Assert.assertEquals("nonKeyword", QKeywordsTest_NonKeyword.nonKeyword.toString());
        Assert.assertEquals("distinct1", QKeywordsTest_Distinct.distinct1.toString());
    }

}
