package com.querydsl.apt.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.querydsl.apt.domain.QOneToOneTest_Person;

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
        Assert.assertEquals(PhoneNumberImpl.class, QOneToOneTest_Person.person.phone.getType());
    }
}
