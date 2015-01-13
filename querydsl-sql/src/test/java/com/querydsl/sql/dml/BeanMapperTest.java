package com.querydsl.sql.dml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.querydsl.sql.domain.QEmployee;
import com.querydsl.core.types.Path;

public class BeanMapperTest extends AbstractMapperTest {
    
    private static final QEmployee emp = QEmployee.employee;
    
    @Test
    public void Extract() {
        Map<Path<?>, Object> values = BeanMapper.DEFAULT.createMap(emp, employee);        
        assertEquals(employee.getDatefield(), values.get(emp.datefield));
        assertEquals(employee.getFirstname(), values.get(emp.firstname));
        assertEquals(employee.getLastname(), values.get(emp.lastname));
        assertEquals(employee.getSalary(), values.get(emp.salary));
        assertEquals(employee.getSuperiorId(), values.get(emp.superiorId));
        assertEquals(employee.getTimefield(), values.get(emp.timefield));
    }
    
    @Test
    public void Extract2() {
        Map<Path<?>, Object> values = BeanMapper.DEFAULT.createMap(emp, new EmployeeX());
        assertTrue(values.isEmpty());
    }

}
