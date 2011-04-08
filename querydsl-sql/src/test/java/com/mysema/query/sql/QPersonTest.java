package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.QBean;

public class QPersonTest {

    public static class Person {

        private int id;

        private String firstname, securedid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getSecuredid() {
            return securedid;
        }

        public void setSecuredid(String securedid) {
            this.securedid = securedid;
        }

    }

    @Test
    public void Populate(){
        QPerson person = QPerson.person;
        QBean<Person> personProjection = new QBean<Person>(Person.class, person.id, person.firstname, person.securedid);
        Person p = personProjection.newInstance(3, "X", "Y");
        assertEquals(3, p.getId());
        assertEquals("X", p.getFirstname());
        assertEquals("Y", p.getSecuredid());
    }

}
