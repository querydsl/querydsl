package com.querydsl.jpa.domain5;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.querydsl.core.annotations.QueryInit;

@Entity
public class MyEntity extends MyMappedSuperclass {

    @Id
    private int id;

    @Embedded
    @QueryInit("*")
    private MyEmbeddedAttribute embeddedAttribute;
}