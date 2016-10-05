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
package com.querydsl.mongodb;

import com.github.fakemongo.junit.FongoRule;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.querydsl.core.testutil.MongoDB;
import com.querydsl.mongodb.domain.GeoEntity;
import com.querydsl.mongodb.domain.QGeoEntity;
import com.querydsl.mongodb.morphia.MorphiaQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.geo.GeoJson;
import org.mongodb.morphia.geo.Geometry;
import org.mongodb.morphia.query.Shape;

import java.net.UnknownHostException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Category(MongoDB.class)
public class GeoSpatialQueryTest {

    @Rule
    public final FongoRule fongoRule = new FongoRule();

    private final String dbname = "geodb";
    private final Morphia morphia;
    private final Datastore ds;
    private final QGeoEntity geoEntity = new QGeoEntity("geoEntity");

    public GeoSpatialQueryTest() throws UnknownHostException, MongoException {
        morphia = new Morphia().map(GeoEntity.class);
        ds = morphia.createDatastore(fongoRule.getFongo().getMongo(), dbname);
        ds.ensureIndexes(GeoEntity.class);
    }

    @Before
    public void before() {
        ds.delete(ds.createQuery(GeoEntity.class));
        ds.getCollection(GeoEntity.class).ensureIndex(new BasicDBObject("location","2d"));

        ds.save(new GeoEntity(10.0, 50.0));
        ds.save(new GeoEntity(20.0, 50.0));
        ds.save(new GeoEntity(30.0, 50.0));
    }

    @Test
    public void near() {
        List<GeoEntity> entities = query().where(geoEntity.location.near(50.0, 50.0)).fetch();
        assertEntities(entities);
    }

    @Test
    public void geojson_near() {
        List<GeoEntity> entities = query().where(geoEntity.locationAsGeoJson().near(GeoJson.point(50.0, 50.0))).fetch();
        assertEntities(entities);
    }

    @Test
    public void near_sphere() {
        List<GeoEntity> entities = query().where(geoEntity.location.nearSphere(50.0, 50.0)).fetch();
        assertEntities(entities);
    }

    @Test
    public void geojson_near_sphere() {
        List<GeoEntity> entities = query().where(geoEntity.locationAsGeoJson().nearSphere(GeoJson.point(50.0, 50.0))).fetch();
        assertEntities(entities);
    }

    @Test
    public void geojson_geoWithin() {
        Shape boundingBox = Shape.box(new Shape.Point(40, 10), new Shape.Point(60, 40));
        List<GeoEntity> entities = query().where(geoEntity.location.geoWithin(boundingBox)).fetch();
        Assert.assertEquals(3, entities.size());
    }

    @Test
    public void geojson_geoWithin_geometry() {
        Geometry geometry = GeoJson.polygon(GeoJson.point(40, 10), GeoJson.point(60, 10), GeoJson.point(60, 40), GeoJson.point(10, 40), GeoJson.point(40, 10));
        List<GeoEntity> entities = query().where(geoEntity.locationAsGeoJson().geoWithin(geometry)).fetch();
        Assert.assertEquals(3, entities.size());
    }

    private void assertEntities(List<GeoEntity> entities) {
        assertEquals(30.0, entities.get(0).getLocation()[0], 0.1);
        assertEquals(20.0, entities.get(1).getLocation()[0], 0.1);
        assertEquals(10.0, entities.get(2).getLocation()[0], 0.1);
    }

    private MorphiaQuery<GeoEntity> query() {
        return new MorphiaQuery<GeoEntity>(morphia, ds, geoEntity);
    }

}
