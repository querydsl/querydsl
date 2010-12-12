package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;

public class AbstractClasses2Test {
    
    public interface Archetype<PK extends Serializable, DO extends Serializable> extends Serializable, Comparable<DO>  {

    }

    @MappedSuperclass
    public abstract class BaseArchetype<PK extends Serializable, DO extends Serializable> implements Archetype<PK, DO> {

        @Id
        @GeneratedValue
        PK id;
        String name;
        String description;

        public BaseArchetype() {}


        public int compareTo(BaseArchetype o) {
            return 0;
        }
    }
    
    @Entity
    public class Grant<P extends Party, S extends Party> extends BaseArchetype<P, S> {
        public int compareTo(S o) {
            return 0;
        }
    }
    
    @Entity
    public class Party extends BaseArchetype<Long, Party> {
        @OneToMany()
        Set<PartyRole> roles = new HashSet<PartyRole>();

        public Party() {
        }

        public int compareTo(Party o) {
            return 0;
        }
    }
    
    @Entity
    public class PartyRole<P extends Party> extends BaseArchetype<Long, PartyRole<P>> {
        @ManyToOne()
        P party;

        public PartyRole(){}

        public int compareTo(PartyRole o) {
            return 0;
        }
    }
    
    @Test
    public void Grant(){
        assertEquals(QAbstractClasses2Test_Party.class, QAbstractClasses2Test_Grant.grant.id.getClass());
        assertEquals(Party.class, QAbstractClasses2Test_Grant.grant.id.getType());
    }

    @Test
    public void Party(){
        assertEquals(NumberPath.class, QAbstractClasses2Test_Party.party.id.getClass());
        assertEquals(Long.class, QAbstractClasses2Test_Party.party.id.getType());
    }
}
