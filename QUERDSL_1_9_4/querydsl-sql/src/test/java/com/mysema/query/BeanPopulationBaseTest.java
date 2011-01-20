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
        Employee smith = query().from(e).where(e.lastname.eq("Smith")).uniqueResult(e);
        assertEquals("John", smith.getFirstname());
        
        // Delete (no changes needed)
        assertEquals(1l, delete(e).where(e.id.eq(employee.getId())).execute());
    }
    
}
