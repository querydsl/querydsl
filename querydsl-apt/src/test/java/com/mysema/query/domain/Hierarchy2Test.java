package com.mysema.query.domain;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.junit.Ignore;

@Ignore
public class Hierarchy2Test {

    @MappedSuperclass
    public abstract class SomeMappedSuperClassHavingMyEmbeddable {

        @Embedded
        MyEmbeddable embeddable;
    }

    @Entity
    class A {

        @OneToOne
        SomeEntity entry;

        @Embedded
        MyEmbeddable myEmbeddable;
    }

    @Entity
    class SomeEntity extends SomeMappedSuperClassHavingMyEmbeddable {
    }

    @Embeddable
    public class MyEmbeddable implements Comparable<MyEmbeddable> {

        @Basic
        int foo;

        public int compareTo(MyEmbeddable individualToCompare) {
            return -1;
        }
    }

}
