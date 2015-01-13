package com.querydsl.sql.dml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.querydsl.sql.domain.QEmployee;
import com.querydsl.core.types.Path;

public class AnnotationMapperTest extends AbstractMapperTest {
    
    private static final QEmployee emp = QEmployee.employee;
    
    @Test
    public void Extract_Success() {
        EmployeeNames names = new EmployeeNames();
        names._id = 9;
        names._firstname = "A";
        names._lastname = "B";
                
        Map<Path<?>, Object> values = AnnotationMapper.DEFAULT.createMap(emp, names);
        assertEquals(3, values.size());
        assertEquals(names._id, values.get(emp.id));
        assertEquals(names._firstname, values.get(emp.firstname));
        assertEquals(names._lastname, values.get(emp.lastname));        
    }
    
    @Test
    public void Extract_Failure() {        
        Map<Path<?>, Object> values = AnnotationMapper.DEFAULT.createMap(emp, employee);
        assertTrue(values.isEmpty());
    }
    
    @Test
    public void Extract2() {
        Map<Path<?>, Object> values = AnnotationMapper.DEFAULT.createMap(emp, new EmployeeX());
        assertTrue(values.isEmpty());
    }

}
