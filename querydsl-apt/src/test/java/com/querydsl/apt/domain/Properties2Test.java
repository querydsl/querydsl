package com.querydsl.apt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import org.junit.Test;

import com.querydsl.apt.domain.QProperties2Test_ConcreteX;

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

    @Test(expected=NoSuchFieldException.class)
    public void test() throws NoSuchFieldException {
        QProperties2Test_ConcreteX.class.getDeclaredField("id");
    }
}
