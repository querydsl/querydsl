package com.mysema.query.sql.dml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Test;

import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.types.Path;

public class DefaultMapperTest extends AbstractMapperTest {
    
    private static final QEmployee emp = QEmployee.employee;
    
    @Test
    public void Extract() {        
        Map<Path<?>, Object> values = DefaultMapper.DEFAULT.createMap(emp, employee);        
        assertEquals(employee.getDatefield(), values.get(emp.datefield));
        assertEquals(employee.getFirstname(), values.get(emp.firstname));
        assertEquals(employee.getLastname(), values.get(emp.lastname));
        assertEquals(employee.getSalary(), values.get(emp.salary));
        assertEquals(employee.getSuperiorId(), values.get(emp.superiorId));
        assertEquals(employee.getTimefield(), values.get(emp.timefield));
    }
    
    @Test
    public void Extract2() {
        Map<Path<?>, Object> values = DefaultMapper.DEFAULT.createMap(emp, new EmployeeX());
        assertTrue(values.isEmpty());
    }

    @Test
    public void PreservedColumnOrder() {
        final Map<String, Path<?>> columns = DefaultMapper.DEFAULT.getColumns(emp);
        final List<String> expectedKeySet = Arrays.asList("id", "firstname", "lastname",
                "salary", "datefield", "timefield", "superiorId");
        assertEquals(expectedKeySet, new ArrayList<String>(columns.keySet()));
    }

}
