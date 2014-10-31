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
package com.mysema.query.domain;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class JDOTest extends AbstractTest {

    @PersistenceCapable
    public static class JDOEntity {

        String prop;

        @NotPersistent
        String skipped;

        @NotPersistent
        JDOEntity skippedEntity;
    }

    @PersistenceCapable
    public static class JDOEntity2 {

        @SuppressWarnings("unused")
        private String stringField1;

        private String stringField2;

        public String getStringfield1() {
            return stringField1;
        }

        public String getStringField2() {
            return stringField2;
        }
    }

    @Test
    public void test() throws SecurityException, NoSuchFieldException {
        cl = QJDOTest_JDOEntity.class;
        match(StringPath.class, "prop");
        assertMissing("skipped");
        assertMissing("skippedEntity");

        cl = QJDOTest_JDOEntity2.class;
        match(StringPath.class, "stringField1");
        assertMissing("stringfield1");
        match(StringPath.class, "stringField2");
    }

}
