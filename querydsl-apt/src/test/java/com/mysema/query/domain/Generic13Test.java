package com.mysema.query.domain;

import com.mysema.query.annotations.QueryEntity;

import org.junit.Test;

public class Generic13Test extends AbstractTest {

    @QueryEntity
    public static class GenericBase<T extends AnotherClass> {
        T t;
    }

    @QueryEntity
    public static class GenericBaseSubclass<P> extends GenericBase<AnotherClass> {
        P p;
    }

    @QueryEntity
    public static class Subclass extends GenericBaseSubclass<Number> {
    }

    public static class AnotherClass {
    }

    @Test
    public void test() throws IllegalAccessException, NoSuchFieldException {
        start(QGeneric13Test_GenericBase.class, QGeneric13Test_GenericBase.genericBase);
        matchType(AnotherClass.class, "t");

        start(QGeneric13Test_GenericBaseSubclass.class, QGeneric13Test_GenericBaseSubclass.genericBaseSubclass);
        matchType(Object.class, "p");

        start(QGeneric13Test_Subclass.class, QGeneric13Test_Subclass.subclass);
        matchType(Number.class, "p");
    }
}
