package com.querydsl.apt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;
import com.querydsl.apt.domain.QAbstractProperties3Test_CompoundContainer;

@Ignore
public class AbstractProperties3Test {

    @MappedSuperclass
    public static class BaseEntity {

    }

    @Entity
    public static class Compound extends BaseEntity {

        String name;

    }

    @Entity
    @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
    public static abstract class Containable extends BaseEntity implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "containable_seq_gen")
        @SequenceGenerator(name = "containable_seq_gen", sequenceName = "seq_containable")
        @Column(name = "id")
        Long id;

        @QueryType(PropertyType.ENTITY)
        public abstract Compound getCompound();

    }

    @MappedSuperclass
    @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    public static abstract class CompoundContainer extends BaseEntity implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "compound_container_seq_gen")
        @SequenceGenerator(name = "compound_container_seq_gen", sequenceName = "seq_compound_container", allocationSize = 1000)
        @Column(name = "compound_container_id")
        Long id;

        @JoinColumn(name = "containable_id")
        @OneToOne(fetch = FetchType.EAGER)
        Containable containable;

    }
    
    @Test
    public void test() {
        QAbstractProperties3Test_CompoundContainer.compoundContainer.containable.compound.name.isNotNull();
    }

}
