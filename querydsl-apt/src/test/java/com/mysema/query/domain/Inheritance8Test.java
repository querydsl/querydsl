package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.path.PNumber;

public class Inheritance8Test {

    @QueryEntity
    public static class SimpleSubclass extends CommonPersistence {
    }

    @QueryEntity
    public static class GenericSubclass extends CommonIdentifiable<Long> {
    }

    @Test
    public void simple_subclass_should_contain_fields_from_external_superclass() {
        assertEquals(PNumber.class, QInheritance8Test_SimpleSubclass.simpleSubclass.version.getClass());
    }

    @Test
    public void generic_subclass_should_contain_fields_from_external_superclass() {
        assertEquals(PNumber.class, QInheritance8Test_GenericSubclass.genericSubclass.version.getClass());
    }

}