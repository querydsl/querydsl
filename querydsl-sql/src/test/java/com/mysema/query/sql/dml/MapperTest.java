package com.mysema.query.sql.dml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.Column;
import com.mysema.query.sql.domain.Employee;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.types.Path;

public class MapperTest {
    
    public class EmployeeNames {

        @Column("ID")
        Integer _id;
        
        @Column("FIRSTNAME")
        String _firstname;
        
        @Column("LASTNAME")
        String _lastname;        
    }

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
    public void Extract_With_AnnotationMapper_Success() {
        EmployeeNames names = new EmployeeNames();
        names._id = 9;
        names._firstname = "A";
        names._lastname = "B";
        
        QEmployee emp = QEmployee.employee;
        Map<Path<?>, Object> values = AnnotationMapper.DEFAULT.createMap(emp, names);
        assertEquals(3, values.size());
        assertEquals(names._id, values.get(emp.id));
        assertEquals(names._firstname, values.get(emp.firstname));
        assertEquals(names._lastname, values.get(emp.lastname));        
    }
    
    @Test
    public void Extract_With_AnnotationMapper_Failure() {
        QEmployee emp = QEmployee.employee;
        Map<Path<?>, Object> values = AnnotationMapper.DEFAULT.createMap(emp, e);
        assertTrue(values.isEmpty());
    }
    
    @Test
    public void Extract_With_Default() {
        QEmployee emp = QEmployee.employee;
        Map<Path<?>, Object> values = DefaultMapper.DEFAULT.createMap(emp, e);        
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
