package com.querydsl.apt.domain;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.apt.domain.QEmbeddable3Test_EmbeddableClass;
import com.querydsl.core.support.Expressions;
import org.junit.Test;

public class Embeddable3Test {

    @QueryEmbeddable
    public static class EmbeddableClass {
        private Integer embeddedProperty;

        public EmbeddableClass() { }

        @QueryProjection
        public EmbeddableClass(Integer embeddedProperty) {
            super();
            this.embeddedProperty = embeddedProperty;
        }

        public Integer getEmbeddedProperty() {
            return embeddedProperty;
        }

        public void setEmbeddedProperty(Integer embeddedProperty) {
            this.embeddedProperty = embeddedProperty;
        }

    }

    @Test
    public void test() {
        QEmbeddable3Test_EmbeddableClass.create(Expressions.path(Integer.class, "num"));
    }

}
