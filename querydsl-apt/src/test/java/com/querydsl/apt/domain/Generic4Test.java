package com.querydsl.apt.domain;

import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class Generic4Test {

    @MappedSuperclass
    public abstract static class CapiBCKeyedByGrundstueck {

    }

    @MappedSuperclass
    public abstract static class HidaBez<B extends HidaBez<B, G>, G extends HidaBezGruppe<G, B>> extends CapiBCKeyedByGrundstueck {

    }

    @MappedSuperclass
    public abstract static class HidaBezGruppe<G extends HidaBezGruppe<G, B>, B extends HidaBez<B, G>> extends
            CapiBCKeyedByGrundstueck {

    }

    @Test
    public void test() {

    }

}
