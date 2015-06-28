package com.querydsl.example.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.querydsl.example.dto.Person;

public class PersonDaoTest extends AbstractDaoTest {
    
    @Resource PersonDao personDao;
    
    @Test
    public void FindAll() {
        List<Person> persons = personDao.findAll();
        assertFalse(persons.isEmpty());
    }
    
    @Test
    public void FindById() {
        assertNotNull(personDao.findById(1));
    }
    
    @Test
    public void Update() {
        Person person = personDao.findById(1);
        personDao.save(person);
    }
    
    @Test
    public void Delete() {
        Person person = new Person();
        person.setEmail("john@acme.com");        
        personDao.save(person);
        assertNotNull(person.getId());
        personDao.delete(person);
        assertNull(personDao.findById(person.getId()));
    }

}
