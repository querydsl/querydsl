package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;

@SuppressWarnings("serial")
public class AbstractClassesTest {

    public interface Archetype<PK extends Serializable, DO extends Serializable> extends Serializable, Comparable<DO>  {

        PK getId();
    }


    @MappedSuperclass
    public static abstract class BaseArchetype<PK extends Serializable, DO extends Serializable> implements Archetype<PK, DO> {

        @Id
        @GeneratedValue
        private PK id;
        private String name;
        private String description;


        public BaseArchetype() {}


        public PK getId() {
            return id;
        }

        public void setId(PK id) {
            this.id = id;
        }

        public int compareTo(BaseArchetype o) {
            return 0;
        }
    }

    @Entity
    public static class Grant<P extends Party, S extends Party> extends BaseArchetype<P, S> {

        public int compareTo(S o) {
            return 0;
        }

    }

    @Entity
    public static class Party extends BaseArchetype<Long, Party> {

        public Party() {
        }

        public int compareTo(Party o) {
            return 0;
        }

    }

    @Test
    public void Grant(){
        assertEquals(QAbstractClassesTest_Party.class, QAbstractClassesTest_Grant.grant.id.getClass());
        assertEquals(Party.class, QAbstractClassesTest_Grant.grant.id.getType());
    }

    @Test
    public void Party(){
        assertEquals(NumberPath.class, QAbstractClassesTest_Party.party.id.getClass());
        assertEquals(Long.class, QAbstractClassesTest_Party.party.id.getType());
    }

}
