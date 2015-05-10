package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class Generic16Test extends AbstractTest {

    @Entity
    public abstract static class HidaBez<B extends HidaBez<B, G>, G extends HidaBezGruppe<G, B>> extends CapiBCKeyedByGrundstueck {

    }

    @Entity
    public abstract static class HidaBezGruppe<G extends HidaBezGruppe<G, B>, B extends HidaBez<B, G>> extends CapiBCKeyedByGrundstueck {

        SortedSet<B> bez = new TreeSet<B>();

    }

    @MappedSuperclass
    public abstract static class CapiBCKeyedByGrundstueck extends CapiBusinessClass {

    }

    @MappedSuperclass
    public abstract static class CapiBusinessClass implements ICapiBusinessClass {

    }

    public interface ICapiBusinessClass extends Comparable<ICapiBusinessClass> {


    }

    @Test
    public void test() {
        assertNotNull(QGeneric16Test_HidaBez.hidaBez);
        assertNotNull(QGeneric4Test_HidaBezGruppe.hidaBezGruppe);
        assertTrue(QGeneric16Test_HidaBezGruppe.hidaBezGruppe.bez.getElementType().equals(HidaBez.class));
    }

}
