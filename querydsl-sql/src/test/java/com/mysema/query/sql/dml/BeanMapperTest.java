package com.mysema.query.sql.dml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.types.Path;

public class BeanMapperTest extends AbstractMapperTest {
    
    @Test
    public void Extract() {
        Map<Path<?>, Object> values = BeanMapper.DEFAULT.createMap(QEmployee.employee, employee);
        QEmployee emp = QEmployee.employee;
        assertEquals(employee.getDatefield(), values.get(emp.datefield));
        assertEquals(employee.getFirstname(), values.get(emp.firstname));
        assertEquals(employee.getLastname(), values.get(emp.lastname));
        assertEquals(employee.getSalary(), values.get(emp.salary));
        assertEquals(employee.getSuperiorId(), values.get(emp.superiorId));
        assertEquals(employee.getTimefield(), values.get(emp.timefield));
    }
    
    @Test
    public void Extract2() {
        Map<Path<?>, Object> values = BeanMapper.DEFAULT.createMap(QEmployee.employee, new EmployeeX());
        assertTrue(values.isEmpty());
    }

}
