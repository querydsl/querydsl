package com.querydsl.example.jap.springboot.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "location",
       uniqueConstraints = @UniqueConstraint(columnNames = { "longitude", "latitude"}))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location extends BaseEntity {

    private Double longitude;

    private Double latitude;

    public Location() {
    }

    public Location(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

}
