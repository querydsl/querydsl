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
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import org.junit.Test;

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

        private String stringField1;

        private String stringField2;

        public String getStringfield1() {
            return stringField1;
        }

        public String getStringField2() {
            return stringField2;
        }
    }

    @PersistenceCapable
    public static class JDOEntity3 {

        private Integer integerField;

        private String stringField;

        @PrimaryKey
        public Integer getId() {
            return integerField;
        }

        @Persistent
        public String getName() {
            return stringField;
        }
    }

    @Test
    public void test() throws SecurityException, NoSuchFieldException {
        start(QJDOTest_JDOEntity.class, QJDOTest_JDOEntity.jDOEntity);
        match(StringPath.class, "prop");
        assertMissing("skipped");
        assertMissing("skippedEntity");

        start(QJDOTest_JDOEntity2.class, QJDOTest_JDOEntity2.jDOEntity2);
        match(StringPath.class, "stringField1");
        assertMissing("stringfield1");
        match(StringPath.class, "stringField2");

        cl = QJDOTest_JDOEntity3.class;
        match(NumberPath.class, "id");
        match(StringPath.class, "name");
    }

}
