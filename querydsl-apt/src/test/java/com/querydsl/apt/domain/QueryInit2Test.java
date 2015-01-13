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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryInit;
import com.querydsl.apt.domain.QQueryInit2Test_Activation;
import com.querydsl.apt.domain.QQueryInit2Test_Categorization;

public class QueryInit2Test {

    @QueryEntity
    public static class Categorization {

        @QueryInit("account.owner")
        Event event;
    }

    @QueryEntity
    public static class Event {

        Account account;
    }

    @QueryEntity
    public static class Activation extends Event { 

    }

    @QueryEntity
    public static class Account{

        Owner owner;
    }

    @QueryEntity
    public static class Owner{

    }

    @Test
    public void Long_Path() {
        assertNotNull(QQueryInit2Test_Categorization.categorization.event.account.owner);
        assertNotNull(QQueryInit2Test_Categorization.categorization.event.as(QQueryInit2Test_Activation.class).account.owner);
    }

}
