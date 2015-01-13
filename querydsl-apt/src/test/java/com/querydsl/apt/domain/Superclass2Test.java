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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.junit.Test;

import com.querydsl.apt.domain.QSuperclass2Test_CommonPersistence;
import com.querydsl.apt.domain.QSuperclass2Test_Subtype;

public class Superclass2Test {

    @MappedSuperclass
    public static class CommonPersistence {

        @Column(name = "created_on")
        private Date createdOn;

        @PrePersist
        protected void onCreate() {
            createdOn = new Date();
        }

        public Date getCreatedOn() {
            return new Date(createdOn.getTime());
        }

    }

    @Entity
    public static class Subtype extends CommonPersistence {

    }

    @Test
    public void DefaultInstance() {
        assertNotNull(QSuperclass2Test_CommonPersistence.commonPersistence);
    }
    
    @Test
    public void test() {
        assertNotNull(QSuperclass2Test_Subtype.subtype.createdOn);
    }
    
    

}
