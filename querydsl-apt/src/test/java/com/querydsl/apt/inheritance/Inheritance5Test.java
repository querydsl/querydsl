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
package com.querydsl.apt.inheritance;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.Date;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QuerySupertype;
import com.querydsl.core.types.path.NumberPath;

public class Inheritance5Test {

    @QuerySupertype
    public static class CommonPersistence {

        private Date createdOn;

        public Date getCreatedOn() {
            return new Date(createdOn.getTime());
        }

        public void setCreatedOn(Date createdOn) {
            this.createdOn = new Date(createdOn.getTime());
        }

    }

    @QuerySupertype
    public static class CommonIdentifiable<ID extends Serializable> extends CommonPersistence {

        private ID id;

        public ID getId() {
            return id;
        }

        public void setId(ID id) {
            this.id = id;
        }

    }

    @QueryEntity
    public class Entity extends CommonIdentifiable<Long> {

    }

    @Test
    public void test() {
        assertEquals(NumberPath.class, QInheritance5Test_Entity.entity.id.getClass());
    }

}
