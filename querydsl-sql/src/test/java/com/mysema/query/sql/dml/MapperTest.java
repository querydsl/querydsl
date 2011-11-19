package com.mysema.query.sql.dml;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.domain.*;
import com.mysema.query.types.Path;

public class MapperTest {

    private Employee e;
    
    @Before
    public void setUp() {
        e = new Employee();
        e.setDatefield(new Date(0));
        e.setFirstname("A");
        e.setLastname("B");
        e.setSalary(new BigDecimal(1.0));
        e.setSuperiorId(2);
        e.setTimefield(new Time(0));
    }
    
    @Test
    public void Extract_With_Default() {
        Map<Path<?>, Object> values = DefaultMapper.DEFAULT.createMap(QEmployee.employee, e);
        QEmployee emp = QEmployee.employee;
        assertEquals(e.getDatefield(), values.get(emp.datefield));
        assertEquals(e.getFirstname(), values.get(emp.firstname));
        assertEquals(e.getLastname(), values.get(emp.lastname));
        assertEquals(e.getSalary(), values.get(emp.salary));
        assertEquals(e.getSuperiorId(), values.get(emp.superiorId));
        assertEquals(e.getTimefield(), values.get(emp.timefield));
    }
    
    @Test
    public void Extract_With_BeanMapper() {
        Map<Path<?>, Object> values = BeanMapper.DEFAULT.createMap(QEmployee.employee, e);
        QEmployee emp = QEmployee.employee;
        assertEquals(e.getDatefield(), values.get(emp.datefield));
        assertEquals(e.getFirstname(), values.get(emp.firstname));
        assertEquals(e.getLastname(), values.get(emp.lastname));
        assertEquals(e.getSalary(), values.get(emp.salary));
        assertEquals(e.getSuperiorId(), values.get(emp.superiorId));
        assertEquals(e.getTimefield(), values.get(emp.timefield));
    }
    
}
