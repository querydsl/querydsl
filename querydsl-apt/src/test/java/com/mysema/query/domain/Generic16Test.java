package com.mysema.query.domain;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.util.Set;

import org.junit.Test;

public class Generic16Test {

    @Entity
    public static abstract class HidaBez<B extends HidaBez<B, G>, G extends HidaBezGruppe<G, B>> extends CapiBCKeyedByGrundstueck {

    }

    @Entity
    public static abstract class HidaBezGruppe<G extends HidaBezGruppe<G, B>, B extends HidaBez<B, G>> extends CapiBCKeyedByGrundstueck {

        Set<HidaBez> bez;

    }

    @MappedSuperclass
    public static abstract class CapiBCKeyedByGrundstueck extends CapiBusinessClass {

    }

    @MappedSuperclass
    public static abstract class CapiBusinessClass implements ICapiBusinessClass {

    }

    public interface ICapiBusinessClass extends Comparable<ICapiBusinessClass> {


    }

    @Test
    public void test() {

    }

}
