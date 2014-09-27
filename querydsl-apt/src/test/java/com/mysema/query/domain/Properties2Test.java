package com.mysema.query.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import org.junit.Test;

public class Properties2Test {

    public static abstract class BaseX<T extends Serializable> implements Serializable {

        public abstract T getId();
    }

    @SuppressWarnings("serial")
    @Entity
    @Table(name = "X")
    public static class ConcreteX extends BaseX<String> {

        @Id
        @Column(name = "name", nullable = false)
        private String name;

        @Override
        public String getId() {
            return name;
        }
    }

    @Test
    public void test() {
        // TODO
    }
}
