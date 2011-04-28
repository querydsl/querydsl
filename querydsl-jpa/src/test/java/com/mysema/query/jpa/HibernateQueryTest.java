package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jpa.domain.QEmployee;
import com.mysema.query.jpa.domain.QUser;
import com.mysema.query.jpa.hibernate.HibernateQuery;

public class HibernateQueryTest {

    @Test
    public void InnerJoin(){
        JPQLQuery hqlQuery = new HibernateQuery();
        QEmployee employee = QEmployee.employee;
        hqlQuery.from(employee);
        hqlQuery.innerJoin(employee.user, QUser.user);
        assertEquals("from Employee employee\n  inner join employee.user as user", hqlQuery.toString());
    }
    
}
