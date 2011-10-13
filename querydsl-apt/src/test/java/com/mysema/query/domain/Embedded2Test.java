package com.mysema.query.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class Embedded2Test {
    
    @MappedSuperclass
    public class EntityCode {

        @Column(name = "code", unique = true)
        String code;

    }
    
    @MappedSuperclass
    public abstract class AbstractEntity<C extends EntityCode> {

        @Embedded
        @Column(name = "code", nullable = false, unique = true)
        C code;
        
    }
    
    @MappedSuperclass
    public class AbstractMultilingualEntity<C extends EntityCode> extends AbstractEntity<C> {

    }
    
    @MappedSuperclass
    public abstract class AbstractNamedEntity<C extends EntityCode> extends AbstractMultilingualEntity<C> {

        @Column(name = "name_en", nullable = false)
        String nameEn;

        @Column(name = "name_nl")
        String nameNl;

    }
        
    @javax.persistence.Entity
    public class Brand extends AbstractNamedEntity<BrandCode> {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "brand_id")
        Long id;

    }
        
    public interface Entity<T> extends Serializable {

        boolean sameIdentityAs(T other);

    }
        
    public class BrandCode extends EntityCode {

    }
    
    @Test
    public void test() {
        // TODO
    }



}
