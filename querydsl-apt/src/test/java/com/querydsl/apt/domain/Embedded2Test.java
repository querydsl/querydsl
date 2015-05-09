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

import java.io.Serializable;

import javax.persistence.*;

import org.junit.Test;

public class Embedded2Test {

    @MappedSuperclass
    public static class EntityCode {

        @Column(name = "code", unique = true)
        String code;

    }

    @MappedSuperclass
    public abstract static class AbstractEntity<C extends EntityCode> {

        @Embedded
        @Column(name = "code", nullable = false, unique = true)
        C code;

    }

    @MappedSuperclass
    public static class AbstractMultilingualEntity<C extends EntityCode> extends AbstractEntity<C> {

    }

    @MappedSuperclass
    public abstract static class AbstractNamedEntity<C extends EntityCode> extends AbstractMultilingualEntity<C> {

        @Column(name = "name_en", nullable = false)
        String nameEn;

        @Column(name = "name_nl")
        String nameNl;

    }

    @javax.persistence.Entity
    public static class Brand extends AbstractNamedEntity<BrandCode> {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "brand_id")
        Long id;

    }

    public interface Entity<T> extends Serializable {

        boolean sameIdentityAs(T other);

    }

    @Embeddable
    public static class BrandCode extends EntityCode {

    }

    @Test
    public void test() {
        // TODO
    }



}
