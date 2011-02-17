/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.domain.Employee;
import com.mysema.query.sql.domain.QEmployee;

public abstract class BeanPopulationBaseTest extends AbstractBaseTest{
    
    private QEmployee e = new QEmployee("e");
    
    @Test
    public void Insert_Update_Query_and_Delete(){       
        // Insert
        Employee employee = new Employee();
        employee.setFirstname("John");
        Integer id = insert(e).populate(employee).executeWithKey(e.id);
        employee.setId(id);

        // Update
        employee.setLastname("Smith");
        assertEquals(1l, update(e).populate(employee).where(e.id.eq(employee.getId())).execute());

        // Query
        Employee smith = query().from(e).where(e.lastname.eq("Smith")).limit(1).uniqueResult(e);
        assertEquals("John", smith.getFirstname());
        
        // Delete (no changes needed)
        assertEquals(1l, delete(e).where(e.id.eq(employee.getId())).execute());
    }
    
    @Test
    public void CustomProjection(){
        // Insert
        Employee employee = new Employee();
        employee.setFirstname("John");
        Integer id = insert(e).populate(employee).executeWithKey(e.id);
        employee.setId(id);

        // Update
        employee.setLastname("Smith");
        assertEquals(1l, update(e).populate(employee).where(e.id.eq(employee.getId())).execute());

        // Query
        Employee smith = extQuery().from(e).where(e.lastname.eq("Smith"))
            .limit(1)
            .uniqueResult(Employee.class, e.lastname, e.firstname);
        assertEquals("John", smith.getFirstname());
        assertEquals("Smith", smith.getLastname());
        
        // Query with alias
        smith = extQuery().from(e).where(e.lastname.eq("Smith"))
            .limit(1)
            .uniqueResult(Employee.class, e.lastname.as("lastname"), e.firstname.as("firstname"));
        assertEquals("John", smith.getFirstname());
        assertEquals("Smith", smith.getLastname());
        
        // Query into custom type        
        OtherEmployee other = extQuery().from(e).where(e.lastname.eq("Smith"))
            .limit(1)
            .uniqueResult(OtherEmployee.class, e.lastname, e.firstname);
        assertEquals("John", other.getFirstname());
        assertEquals("Smith", other.getLastname());
        
        // Delete (no changes needed)
        assertEquals(1l, delete(e).where(e.id.eq(employee.getId())).execute());
    }
    

    protected ExtendedSQLQuery extQuery() {
        return new ExtendedSQLQuery(Connections.getConnection(), templates);
    }
            
}
