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
import com.querydsl.core.types.path.DateTimePath;
import com.querydsl.core.types.path.NumberPath;

/**
 * Test multiple level superclasses with generics.
 */
public class Inheritance6Test {

    /*
     * Top superclass.
     */
    @QuerySupertype
    public static class CommonIdentifiable<ID extends Serializable> {
        @SuppressWarnings("unused")
        private ID id;

        @SuppressWarnings("unused")
        private Date createdOn;
    }

    /*
     * Intermediate superclass, equivalent to @MappedSuperclass.
     */
    @QuerySupertype
    public abstract static class Translation<T extends Translation<T, K>, K extends TranslationKey<T, K>>
            extends CommonIdentifiable<Long> {
        @SuppressWarnings("unused")
        private String value;
    }

    /*
     * Intermediate superclass, equivalent to @MappedSuperclass.
     */
    @QuerySupertype
    public abstract static class TranslationKey<T extends Translation<T, K>, K extends TranslationKey<T, K>>
            extends CommonIdentifiable<Long> {
    }

    @QueryEntity
    public static class Gloss extends Translation<Gloss, GlossKey> {
    }

    @QueryEntity
    public static class GlossKey extends TranslationKey<Gloss, GlossKey> {
    }

    @Test
    public void Gloss_subtype_should_contain_fields_from_superclass() {
        assertEquals(String.class, QInheritance6Test_Gloss.gloss.value.getType());
    }

    @Test
    public void Intermediate_superclass_should_contain_fields_from_top_superclass() {
         QInheritance6Test_Translation translation = QInheritance6Test_Gloss.gloss._super;
         assertEquals(DateTimePath.class, translation.createdOn.getClass());
    }

    @Test
    public void Gloss_subtype_should_contain_fields_from_top_superclass() {
        assertEquals(DateTimePath.class, QInheritance6Test_Gloss.gloss.createdOn.getClass());
    }

    @Test
    public void Gloss_subtype_should_contain_id_from_top_superclass() {
        assertEquals(NumberPath.class, QInheritance6Test_Gloss.gloss.id.getClass());
    }

}
