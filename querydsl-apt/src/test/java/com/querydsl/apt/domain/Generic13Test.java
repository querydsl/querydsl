package com.querydsl.apt.domain;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;

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
        start(QGeneric13Test_GenericBase.class, QGeneric13Test_GenericBase.Constants.genericBase);
        matchType(AnotherClass.class, "t");

        start(QGeneric13Test_GenericBaseSubclass.class, QGeneric13Test_GenericBaseSubclass.Constants.genericBaseSubclass);
        matchType(Object.class, "p");

        start(QGeneric13Test_Subclass.class, QGeneric13Test_Subclass.Constants.subclass);
        matchType(Number.class, "p");
    }
}
