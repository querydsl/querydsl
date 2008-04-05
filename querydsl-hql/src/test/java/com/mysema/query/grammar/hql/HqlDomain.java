package com.mysema.query.grammar.hql;


import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import com.mysema.query.annotations.DTO;



/**
 * HqlDomain provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlDomain {
    
    @Entity
    public static class Account {
        @Id long id;
        @ManyToOne Person owner;
    }
    
    @Entity
    public static class Animal {
        boolean alive;
        java.util.Date birthdate;
        int bodyWeight, weight, toes;
        Color color;
        @Id int id;    
        String name;
    }
    
    @Entity
    public static class AuditLog {
        @Id int id;
        @ManyToOne Item item;
    }
    
    @Entity
    public static class Bar {
        java.util.Date date;
        @Id int id;
    }
    
    @Entity
    public static class Calendar {
        @CollectionOfElements Map<String,java.util.Date> holidays;
        @Id int id;
    }
    
    @Entity
    public static class Cat extends Animal{
        int breed;
        Color eyecolor;   
        @OneToMany List<Cat> kittens;
        @ManyToOne Cat mate;
    }
    
    @Entity
    public static class Catalog {
        Date effectiveDate;
        @Id int id;
        @OneToMany Collection<Price> prices;
    }
    
    public enum Color {
        BLACK, TABBY
    }
    
    @Entity
    public static class Company {
        @ManyToOne Employee ceo;
        @OneToMany @IndexColumn(name="_index") List<Department> departments;
        @Id int id;
        String name;
    }
    
    @Entity
    public static class Customer {
        @ManyToOne Order currentOrder;
        @Id int id;    
        @ManyToOne Name name;
    }
    
    @Entity
    public static class Department {
        @ManyToOne Company company;
        @OneToMany @IndexColumn(name="_index") List<Employee> employees; 
        @Id int id; 
        String name;
    }
    
    @Entity
    public static class Document {
        @Id int id;
        String name;
        Date validTo;
    }
    
    @Entity
    public static class DomesticCat extends Cat{

    }
    
    @Entity
    public static class doofus{
        String gob;
        @Id long id;
    }
    
    @Entity
    public static class Employee {
        @ManyToOne Company company;
        String firstName, lastName; 
        @Id int id;
    }
    
    @Entity
    public static class EvilType {
        @ManyToOne @JoinColumn(name="_asc") EvilType asc;
        @ManyToOne @JoinColumn(name="_desc") EvilType desc;
        @Id int id;
        @ManyToOne EvilType isnull, isnotnull, get, getType, getMetadata;
        @ManyToOne EvilType toString, hashCode, getClass, notify, notifyAll, wait;
    }
    
    @DTO
    public static class Family {
        public Family(Cat mother, Cat mate, Cat offspr){
            
        }
    }
    
    @Entity
    @DTO
    public static class Foo {
        String bar;
        @Id int id;
        @CollectionOfElements List<String> names;
        java.util.Date startDate;
        public Foo(){}
        public Foo(long l){}
        public Foo(long l, long r){}
    }
    
    @Entity
    public static class Formula {
        @Id int id;
        @ManyToOne Parameter parameter;
    }
    
    @Entity
    public static class Item {
        @Id long id;
        @ManyToOne Product product;    
    }
    
    @Entity
    public static class Location {
        @Id long id;
        String name;
    }
    
    @Entity
    public static class Name {
        String firstName, lastName, nickName;
        @Id long id;    
    }
    
    @Entity
    public static class Named {
        @Id long id;
        String name;
    }
    
    @Entity
    public static class NameList{
        @Id long id;
        @CollectionOfElements Collection<String> names;
    }
    
    @Entity
    public static class Nationality {
        @ManyToOne Calendar calendar;
        @Id long id;
    }
    
    @Entity
    public static class Order {
        @ManyToOne Customer customer;
        @CollectionOfElements List<Integer> deliveredItemIndices;    
        @Id long id;
        @OneToMany @IndexColumn(name="_index") List<Item> items, lineItems;
        boolean paid;
    }
    
    @Entity
    public static class Parameter {
        @Id long id;
    }
    
    @Entity
    public static class Payment extends Item{
        @ManyToOne Status currentStatus, status;
        PaymentStatus name;
        @OneToMany Collection<StatusChange> statusChanges;
    }
    
    public enum PaymentStatus{
        AWAITING_APPROVAL
    }
    
    @Entity
    public static class Person {
        java.util.Date birthDay;
        @Id long i;
        @ManyToOne PersonId id;
        String name;
        @ManyToOne Nationality nationality;
    }
    
    @Entity
    public static class PersonId {
        String country;
        @Id long id;
        int medicareNumber;
    }
    
    @Entity
    public static class Player{
        @Id long id;
        @CollectionOfElements List<Integer> scores;
    }
    
    @Entity
    public static class Price {
        long amount;
        @Id long id;
        @ManyToOne Product product;
    }
    
    @Entity
    public static class Product extends Item{
//        @Id long id;
        String name;
    }
    
    @Entity
    public static class Show {
        @CollectionOfElements Map<String,String> acts;
        @Id int id;
    }
    
    @Entity
    public static class Status {
        @Id long id;
        String name;
    }
    
    @Entity
    public static class StatusChange {
        @Id long id;
        java.util.Date timeStamp;
    }
    
    @Entity
    public static class Store {
        @OneToMany List<Customer> customers;
        @Id long id;
        @ManyToOne Location location;
    }
    
    @Entity
    public static class User {
        @ManyToOne Company company;
        @Id long id;        
        String userName, firstName, lastName;    
    }

}
