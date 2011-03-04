package com.mysema.query.mongodb.domain;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class GeoEntity {

    private @Id ObjectId id;

    private Double[] location;

    public GeoEntity(double l1, double l2) {
        location = new Double[]{l1, l2};
    }

    public GeoEntity() {}

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }


}
