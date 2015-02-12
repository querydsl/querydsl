package com.querydsl.mongodb.domain;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
class Person {
   @Id
   public ObjectId id;

   public String name;

   // manual reference to an address
   public ObjectId addressId;
}