package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.types.path.PNumber;

public class Inheritance5Test {

    @QuerySupertype
    public static class CommonPersistence {

        private Date createdOn;

        public Date getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(Date createdOn) {
            this.createdOn = createdOn;
        }
        
    }

    @QuerySupertype
    public static class CommonIdentifiable<ID extends Serializable> extends CommonPersistence {

        private ID id;

        public ID getId() {
            return id;
        }

        public void setId(ID id) {
            this.id = id;
        }
        
    }

    @QueryEntity
    public class Entity extends CommonIdentifiable<Long> {
        
    }
    
    @Test
    @Ignore
    public void test(){
        assertEquals(PNumber.class, QInheritance5Test_Entity.entity.id.getType());
    }
    
}
