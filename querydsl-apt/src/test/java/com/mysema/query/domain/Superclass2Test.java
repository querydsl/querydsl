package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.junit.Test;

public class Superclass2Test {

    @MappedSuperclass
    public static class CommonPersistence {

        @Column(name = "created_on")
        private Date createdOn;

        @PrePersist
        protected void onCreate() {
            createdOn = new Date();
        }

        public Date getCreatedOn() {
            return createdOn;
        }

    }

    @Entity
    public static class Subtype extends CommonPersistence{

    }

    @Test
    public void test(){
        assertNotNull(QSuperclass2Test_Subtype.subtype.createdOn);
    }

}
