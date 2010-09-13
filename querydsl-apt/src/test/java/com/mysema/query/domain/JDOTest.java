/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
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
        match(StringPath.class, "stringField2");
    }

}
