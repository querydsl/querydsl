package com.querydsl.sql.spatial;

import org.geolatte.geom.Geometry;

public class Shapes {

    private int id;

    private Geometry geometry;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

}
