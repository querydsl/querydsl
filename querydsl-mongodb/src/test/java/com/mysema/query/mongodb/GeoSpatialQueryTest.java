package com.mysema.query.mongodb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.BasicDBObject;
import com.mysema.query.mongodb.domain.City;
import com.mysema.query.mongodb.domain.GeoEntity;
import com.mysema.query.mongodb.domain.QGeoEntity;
import com.mysema.query.mongodb.domain.User;
import com.mysema.query.mongodb.morphia.MorphiaQuery;

public class GeoSpatialQueryTest {

    private final String dbname = "geodb";
    private final Morphia morphia = new Morphia().map(GeoEntity.class);
    private final Datastore ds = morphia.createDatastore(dbname);
    private final QGeoEntity geoEntity = new QGeoEntity("geoEntity");

    User u1, u2, u3, u4;
    City tampere, helsinki;

    @Before
    public void before() {
        ds.delete(ds.createQuery(GeoEntity.class));
        ds.getCollection(GeoEntity.class).ensureIndex(new BasicDBObject("location","2d"));;
    }

    @Test
    public void Near(){
        ds.save(new GeoEntity(10.0, 50.0));
        ds.save(new GeoEntity(20.0, 50.0));
        ds.save(new GeoEntity(30.0, 50.0));

        List<GeoEntity> entities = query().where(geoEntity.location.near(50.0, 50.0)).list();
        assertEquals(30.0, entities.get(0).getLocation()[0].doubleValue(), 0.1);
        assertEquals(20.0, entities.get(1).getLocation()[0].doubleValue(), 0.1);
        assertEquals(10.0, entities.get(2).getLocation()[0].doubleValue(), 0.1);
    }

    private MongodbQuery<GeoEntity> query() {
        return new MorphiaQuery<GeoEntity>(morphia, ds, geoEntity);
    }

}
