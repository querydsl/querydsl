package com.mysema.query.mongodb.domain;


public final class City {

    public City(){}
    
    public City(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public String name;
    
    public Double latitude;
    
    public Double longitude;
    
}
