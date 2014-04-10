package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.junit.Test;

public class ManyToManyTest {

    public interface PhoneNumber {

    }

    @Entity
    public static class PhoneNumberImpl {

    }

    @Entity
    public static class Person {

        @ManyToMany(targetEntity=PhoneNumberImpl.class)
        Set<PhoneNumber> phones;
    }

    @Test
    public void test() {
        assertEquals(PhoneNumberImpl.class, QManyToManyTest_Person.person.phones.getElementType());
    }

}
