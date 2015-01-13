package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.querydsl.apt.domain.QGeneric16Test_HidaBez;
import com.querydsl.apt.domain.QGeneric16Test_HidaBezGruppe;
import com.querydsl.apt.domain.QGeneric4Test_HidaBezGruppe;

public class Generic16Test extends AbstractTest {

    @Entity
    public static abstract class HidaBez<B extends HidaBez<B, G>, G extends HidaBezGruppe<G, B>> extends CapiBCKeyedByGrundstueck {

    }

    @Entity
    public static abstract class HidaBezGruppe<G extends HidaBezGruppe<G, B>, B extends HidaBez<B, G>> extends CapiBCKeyedByGrundstueck {

        SortedSet<B> bez = new TreeSet<B>();

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
        assertNotNull(QGeneric16Test_HidaBez.hidaBez);
        assertNotNull(QGeneric4Test_HidaBezGruppe.hidaBezGruppe);
        assertTrue(QGeneric16Test_HidaBezGruppe.hidaBezGruppe.bez.getElementType().equals(HidaBez.class));
    }

}
