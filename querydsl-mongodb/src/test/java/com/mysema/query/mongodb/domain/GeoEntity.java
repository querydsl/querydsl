package com.mysema.query.mongodb.domain;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class GeoEntity {

    private @Id ObjectId id;
    
    private double[] location1;
    
//    private Double[] location2;

    public double[] getLocation1() {
        return location1;
    }

    public void setLocation1(double[] location1) {
        this.location1 = location1;
    }

//    public Double[] getLocation2() {
//        return location2;
//    }
//
//    public void setLocation2(Double[] location2) {
//        this.location2 = location2;
//    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    
    
}
