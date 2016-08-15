/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.mongodb.domain;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.geo.GeoJson;
import org.mongodb.morphia.geo.Point;

@Entity
public class GeoEntity extends AbstractEntity {

    private Double[] location;
    private Point locationAsGeoJson;

    public GeoEntity(double l1, double l2) {
        this.location = new Double[]{l1, l2};
        this.locationAsGeoJson = GeoJson.point(location[0], location[1]);
    }

    public GeoEntity() { }

    public Double[] getLocation() {
        return location;
    }

    public Point getLocationAsGeoJson() {
        return locationAsGeoJson;
    }

    public void setLocation(double l1, double l2) {
        this.location = new Double[]{l1, l2};
        this.locationAsGeoJson = GeoJson.point(location[0], location[1]);
    }

}
