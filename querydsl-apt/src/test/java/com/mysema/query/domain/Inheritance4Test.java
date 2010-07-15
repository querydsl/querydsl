/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;

public class Inheritance4Test extends AbstractTest{

    @QueryEntity
    public class EntityWithComparable {
        private Comparable<?> field;

        public Comparable<?> getField() {
            return field;
        }

    }

    @QueryEntity
    public class EntityWithNumber extends EntityWithComparable{
        private Long field;

        public Long getField() {
            return field;
        }

    }

    @QueryEntity
    public class EntityWithString extends EntityWithComparable{
        private String field;

        public String getField() {
            return field;
        }

    }

    @Test
    public void test() throws SecurityException, NoSuchFieldException{
        cl = QInheritance4Test_EntityWithComparable.class;
        match(PSimple.class, "field");

        cl = QInheritance4Test_EntityWithNumber.class;
        match(PNumber.class, "field");

        cl = QInheritance4Test_EntityWithString.class;
        match(PString.class, "field");

    }
}
