package com.mysema.query.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OneToOneTest {

    public interface PhoneNumber {

    }

    @Entity
    public static class PhoneNumberImpl {

    }

    @Entity
    public static class Person {

        @OneToOne(targetEntity=PhoneNumberImpl.class)
        PhoneNumber phone;
    }

    @Test
    public void test() {
        assertEquals(PhoneNumberImpl.class, QOneToOneTest_Person.person.phone.getType());
    }
}
