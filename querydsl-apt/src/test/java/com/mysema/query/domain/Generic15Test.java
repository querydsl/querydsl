package com.mysema.query.domain;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class Generic15Test {

    @MappedSuperclass
    public static abstract class Compound<T extends Containable> {
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
        QGeneric15Test_MyContainable.myContainable.compound
    }
}
