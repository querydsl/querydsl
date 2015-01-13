package com.querydsl.apt.domain;

import java.lang.annotation.Annotation;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.junit.Ignore;

@Ignore
public class AnnotationTypeTest {

    @MappedSuperclass
    public static abstract class BaseObject<T extends Annotation> {
        
    }

    @Entity
    public static class Person extends BaseObject<javax.persistence.Id> {
        @Id
        private Long id;
    }

    @Embeddable
    public static class Address extends BaseObject<javax.persistence.EmbeddedId> {
        @EmbeddedId
        private String street;
    }
    
}
