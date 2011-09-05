package com.mysema.query.domain;

import static org.junit.Assert.*;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryType;


public class HierarchyTest {

    @Entity
    class A {

        B b;

        A(B b) {
            this.b = b;
        }

        B getB() {
            return b;
        }
    }

    @Entity
    class A2 extends A {

        // XXX: uncomment @Comment to break generation - QA2.a2.b() will then
        // return B instead of B2
        @Column
        int foo;

        A2(B2 b2) {
            super(b2);
        }

        @Override
        @QueryType(PropertyType.ENTITY)
        B2 getB() {
            return (B2) super.getB();
        }
    }

    @Entity
    class B {
    }

    @Entity
    class B2 extends B {
    }

    @Test
    public void test() {
        QHierarchyTest_B2 qb2 = QHierarchyTest_A2.a2.b;
        assertNotNull(qb2);
    }
}
