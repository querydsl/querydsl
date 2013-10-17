package com.mysema.query.domain;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEntity;

@Ignore
public class Generic13Test {

    @QueryEntity
    public static class GenericBase<T extends AnotherClass> {
        T t;
    }

    @QueryEntity
    public static class GenericBaseSubclass<P> extends GenericBase<AnotherClass> {
        P p;
    }

    @QueryEntity
    public static class Subclass extends GenericBaseSubclass<Object> {
    }

    public static class AnotherClass {
    }

}
