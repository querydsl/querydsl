/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import static org.junit.Assert.fail;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.junit.Test;

import com.mysema.query.annotations.QueryInit;

/**
 * The Class Account.
 */
@SuppressWarnings("serial")
@Entity
public class Account implements Serializable{

    @Transient
    public int transientField;

    @Id
    long id;

    @ManyToOne
    @QueryInit("pid")
    Person owner;

    @Embedded
    EmbeddedType embeddedData;

    @Test
    public void test(){
        try {
            QAccount.class.getField("serialVersionUID");
            fail("Got serialVersionUID");
        } catch (Exception e) {
            // expected
        }
        try {
            QAccount.class.getField("transientField");
            fail("Got transientField");
        } catch (Exception e) {
            // expected
        }
    }
}
