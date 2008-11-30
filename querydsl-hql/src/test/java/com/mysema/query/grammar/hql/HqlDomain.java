/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.hql;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.*;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import com.mysema.query.annotations.DTO;



/**
 * HqlDomain provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HqlDomain {
    
    @MappedSuperclass
    public static class Superclass{
        String superclassProperty;
    }
    
    @Entity
    public static class InheritatedProperties extends Superclass{
        @Id long id;
        String classProperty;
    }
    
    @Entity
    public static class SimpleTypes{
        @Id long id;
        BigDecimal bigDecimal;
        Byte bbyte;
        byte bbyte2;
        Character cchar;
        char cchar2;
        Double ddouble;
        double ddouble2;
        Float ffloat;
        float ffloat2;
        Integer iint;
        int iint2;
        Locale llocale;
        Long llong;
        long llong2;
        String sstring;
    }
    
    /**
     * The Class Account.
     */
    @Entity
    public static class Account {
        @Id long id;
        @ManyToOne Person owner;        
    }
    
    /**
     * The Class Animal.
     */
    @Entity
    public static class Animal {
        boolean alive;
        java.util.Date birthdate;
        int bodyWeight, weight, toes;
        Color color;
        @Id int id;    
        String name;
    }
    
    /**
     * The Class AuditLog.
     */
    @Entity
    public static class AuditLog {
        @Id int id;
        @ManyToOne Item item;
    }
    
    /**
     * The Class Bar.
     */
    @Entity
    public static class Bar {
        java.util.Date date;
        @Id int id;
    }
    
    /**
     * The Class Calendar.
     */
    @Entity
    public static class Calendar {
        @CollectionOfElements Map<String,java.util.Date> holidays;
        @Id int id;
    }
    
    /**
     * The Class Cat.
     */
    @Entity
    public static class Cat extends Animal{
        int breed;
        Color eyecolor;   
        @OneToMany List<Cat> kittens;
        @ManyToOne Cat mate;
    }
    
    /**
     * The Class Catalog.
     */
    @Entity
    public static class Catalog {
        Date effectiveDate;
        @Id int id;
        @OneToMany Collection<Price> prices;
    }
    
    /**
     * The Enum Color.
     */
    public enum Color {
        BLACK, TABBY
    }
    
    /**
     * The Class Company.
     */
    @Entity
    public static class Company {
        @ManyToOne Employee ceo;
        @OneToMany @IndexColumn(name="_index") List<Department> departments;
        @Id int id;
        String name;
    }
    
    /**
     * The Class Customer.
     */
    @Entity
    public static class Customer {
        @ManyToOne Order currentOrder;
        @Id int id;    
        @ManyToOne Name name;
    }
    
    /**
     * The Class Department.
     */
    @Entity
    public static class Department {
        @ManyToOne Company company;
        @OneToMany @IndexColumn(name="_index") List<Employee> employees; 
        @Id int id; 
        String name;
    }
    
    /**
     * The Class Document.
     */
    @Entity
    public static class Document {
        @Id int id;
        String name;
        Date validTo;
    }
    
    /**
     * The Class DomesticCat.
     */
    @Entity
    public static class DomesticCat extends Cat{

    }
    
    /**
     * The Class doofus.
     */
    @Entity
    public static class doofus{
        String gob;
        @Id long id;
    }
    
    /**
     * The Class Employee.
     */
    @Entity
    public static class Employee {
        @ManyToOne Company company;
        String firstName, lastName; 
        @Id int id;
    }
    
    /**
     * The Class EvilType.
     */
    @Entity
    public static class EvilType {
        @ManyToOne @JoinColumn(name="_asc") EvilType asc;
        @ManyToOne @JoinColumn(name="_desc") EvilType desc;
        @Id int id;
        @ManyToOne EvilType isnull, isnotnull, get, getType, getMetadata;
        @ManyToOne EvilType toString, hashCode, getClass, notify, notifyAll, wait;
    }
    
    /**
     * The Class Family.
     */
    @DTO
    public static class Family {
        public Family(Cat mother, Cat mate, Cat offspr){
            
        }
    }
    
    /**
     * The Class Foo.
     */
    @Entity
    public static class Foo {
        String bar;
        @Id int id;
        @CollectionOfElements List<String> names;
        java.util.Date startDate;
    }
    
    @DTO
    public static class FooDTO {
        String bar;
        @Id int id;
        @CollectionOfElements List<String> names;
        java.util.Date startDate;
        public FooDTO(){}
        public FooDTO(long l){}
        public FooDTO(long l, long r){}
    }
    
    /**
     * The Class Formula.
     */
    @Entity
    public static class Formula {
        @Id int id;
        @ManyToOne Parameter parameter;
    }
    
    /**
     * The Class Item.
     */
    @Entity
    public static class Item {
        @Id long id;
        @ManyToOne Product product;    
    }
    
    /**
     * The Class Location.
     */
    @Entity
    public static class Location {
        @Id long id;
        String name;
    }
    
    /**
     * The Class Name.
     */
    @Entity
    public static class Name {
        String firstName, lastName, nickName;
        @Id long id;    
    }
    
    /**
     * The Class Named.
     */
    @Entity
    public static class Named {
        @Id long id;
        String name;
    }
    
    /**
     * The Class NameList.
     */
    @Entity
    public static class NameList{
        @Id long id;
        @CollectionOfElements Collection<String> names;
    }
    
    /**
     * The Class Nationality.
     */
    @Entity
    public static class Nationality {
        @ManyToOne Calendar calendar;
        @Id long id;
    }
    
    /**
     * The Class Order.
     */
    @Entity
    public static class Order {
        @ManyToOne Customer customer;
        @CollectionOfElements List<Integer> deliveredItemIndices;    
        @Id long id;
        @OneToMany @IndexColumn(name="_index") List<Item> items, lineItems;
        boolean paid;
    }
    
    /**
     * The Class Parameter.
     */
    @Entity
    public static class Parameter {
        @Id long id;
    }
    
    /**
     * The Class Payment.
     */
    @Entity
    public static class Payment extends Item{
        @ManyToOne Status currentStatus, status;
        PaymentStatus name;
        @OneToMany Collection<StatusChange> statusChanges;
    }
    
    /**
     * The Enum PaymentStatus.
     */
    public enum PaymentStatus{
        AWAITING_APPROVAL
    }
    
    /**
     * The Class Person.
     */
    @Entity
    public static class Person {
        java.util.Date birthDay;
        @Id long i;
        @ManyToOne PersonId id;
        String name;
        @ManyToOne Nationality nationality;
    }
    
    /**
     * The Class PersonId.
     */
    @Entity
    public static class PersonId {
        String country;
        @Id long id;
        int medicareNumber;
    }
    
    /**
     * The Class Player.
     */
    @Entity
    public static class Player{
        @Id long id;
        @CollectionOfElements List<Integer> scores;
    }
    
    /**
     * The Class Price.
     */
    @Entity
    public static class Price {
        long amount;
        @Id long id;
        @ManyToOne Product product;
    }
    
    /**
     * The Class Product.
     */
    @Entity
    public static class Product extends Item{
//        @Id long id;
        String name;
    }
    
    /**
     * The Class Show.
     */
    @Entity
    public static class Show {
        @CollectionOfElements Map<String,String> acts;
        @Id int id;
    }
    
    /**
     * The Class Status.
     */
    @Entity
    public static class Status {
        @Id long id;
        String name;
    }
    
    /**
     * The Class StatusChange.
     */
    @Entity
    public static class StatusChange {
        @Id long id;
        java.util.Date timeStamp;
    }
    
    /**
     * The Class Store.
     */
    @Entity
    public static class Store {
        @OneToMany List<Customer> customers;
        @Id long id;
        @ManyToOne Location location;
    }
    
    /**
     * The Class User.
     */
    @Entity
    public static class User {
        @ManyToOne Company company;
        @Id long id;        
        String userName, firstName, lastName;    
    }

}
