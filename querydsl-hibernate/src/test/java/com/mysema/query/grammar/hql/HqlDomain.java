package com.mysema.query.grammar.hql;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import com.mysema.query.dto.DTO;

/**
 * HqlDomain provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlDomain {
    
    @Entity
    public class Account {
        Person owner;
    }
    
    @Entity
    public class Animal {
        boolean alive;
        java.util.Date birthdate;
        int bodyWeight, id, weight, toes;
        Color color;    
        String name;
    }
    
    @Entity
    public class AuditLog {
        Item item;
    }
    
    @Entity
    public class Bar {
        java.util.Date date;
    }
    
    @Entity
    public class Calendar {
        Map<String,java.util.Date> holidays;
    }
    
    @Entity
    public class Cat extends Animal{
        int breed;
        Color eyecolor;   
        Collection<Cat> kittens;
        Cat mate;
    }
    
    @Entity
    public class Catalog {
        Date effectiveDate;
        Collection<Price> prices;
    }
    
    public enum Color {
        BLACK, TABBY
    }
    
    @Entity
    public class Company {
        Employee ceo;
        List<Department> departments;
        long id;
        String name;
    }
    
    @Entity
    public class Customer {
        Name name;    
    }
    
    @Entity
    public class Department {
        Company company; 
        List<Employee> employees; 
        String name;
    }
    
    @Entity
    public class Document {
        String name;
        Date validTo;
    }
    
    @Entity
    public class DomesticCat extends Cat{

    }
    
    @Entity
    public class Employee {
        Company company; 
        String firstName, lastName;
    }
    
    @Entity
    public class EvilType {
        EvilType isnull, isnotnull, asc, desc, get, getType, getMetadata;
        EvilType toString, hashCode, getClass, notify, notifyAll, wait;
    }
    
    @DTO
    public class Family {
        public Family(Cat mother, Cat mate, Cat offspr){
            
        }
    }
    
    @Entity
    @DTO
    public class Foo {
        String bar;
        java.util.Date startDate;
        public Foo(){}
        public Foo(long l){}
    }
    
    @Entity
    public class Formula {
        Parameter parameter;
    }
    
    @Entity
    public class Item {
        long id;
        Product product;    
    }
    
    @Entity
    public class Location {
        String name;
    }
    
    @Entity
    public class Name {
        String firstName, lastName;    
    }
    
    @Entity
    public class Named {
        String name;
    }
    
    @Entity
    public class Nationality {
        Calendar calendar;
    }
    
    @Entity
    public class Order {
        Customer customer;
        List<Integer> deliveredItemIndices;    
        long id;
        List<Item> items, lineItems;
        boolean paid;
    }
    
    @Entity
    public class Parameter {

    }
    
    @Entity
    public class Payment extends Item{

    }
    
    @Entity
    public class Person {
        java.util.Date birthDay;
        PersonId id;
        Nationality nationality;
    }
    
    @Entity
    public class PersonId {
        String country;
        int medicareNumber;
    }
    
    @Entity
    public class Price {
        long amount;
        Product product;
    }
    
    @Entity
    public class Product {
        String name;
    }
    
    @Entity
    public class Store {
        List<Customer> customers;
        Location location;
    }
    
    @Entity
    public class User {
        Company company;
        long id;
        String userName, firstName, lastName;    
    }

}
