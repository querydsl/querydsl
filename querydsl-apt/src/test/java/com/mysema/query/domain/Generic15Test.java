package com.mysema.query.domain;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Generic15Test {

    @MappedSuperclass
    public static abstract class Compound<T extends Containable> {

        private Set<T> containables = new HashSet<T>();
    }

    @MappedSuperclass
    public static abstract class Containable<T extends Compound> {

        private T compound;
    }

    @Entity
    public static class MyCompound extends Compound<MyContainable>{
    }

    @Entity
    public static class MyContainable extends Containable<MyCompound> {

        private String additionalField;
    }

    @Test
    public void test() {
        // QMyContainable
        assertEquals(MyCompound.class, QGeneric15Test_MyContainable.myContainable.compound.getType());
    }
}
