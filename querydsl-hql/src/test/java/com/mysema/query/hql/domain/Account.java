/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import static org.junit.Assert.fail;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.junit.Test;


/**
 * The Class Account.
 */
@Entity
public class Account implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -8890536547873474437L;
    
    @Transient
    public int transientField;

    @Id
    long id;
    
    @ManyToOne
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