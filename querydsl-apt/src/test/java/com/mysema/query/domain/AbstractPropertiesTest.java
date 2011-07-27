package com.mysema.query.domain;

import java.io.Serializable;

import javax.persistence.Entity;

import org.junit.Ignore;

@Ignore
public class AbstractPropertiesTest {

    public abstract class GenericEntity<K extends Serializable & Comparable<K>> {

        private static final long serialVersionUID = -3988499137919577054L;

        public abstract K getId();

        public abstract void setId(K id);

    }

    @Entity
    public class TestEntity extends GenericEntity<Integer> {

        private static final long serialVersionUID = 1803671157183603979L;

        private Integer id;

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public void setId(Integer id) {
            this.id = id;
        }

    }

}
