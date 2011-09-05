package com.mysema.query.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.junit.Ignore;

@Ignore
public class HierarchyTest {

    // public static void main(String[] args) {
    // QB2 qb2 = QA2.a2.b();
    // }
    // }

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

}
