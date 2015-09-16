package com.querydsl.apt.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

import com.querydsl.core.types.dsl.ComparablePath;

public class Generic17Test extends AbstractTest {

    @MappedSuperclass
    public abstract class GenericEntity<K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>> implements Serializable, Comparable<E> {

        public abstract K getId();

        public abstract void setId(K id);
    }

    @Entity
    public class LongEntity extends GenericEntity<Long, LongEntity> {

        @Override
        public Long getId() {
            return 0L;
        }

        @Override
        public void setId(Long id) {
        }

        @Override
        public int compareTo(LongEntity o) {
            return this.getId().compareTo(o.getId());
        }

    }

    @Test
    public void test() throws NoSuchFieldException {
        start(QGeneric17Test_GenericEntity.class, QGeneric17Test_GenericEntity.genericEntity);
        match(ComparablePath.class, "id");
    }
}
