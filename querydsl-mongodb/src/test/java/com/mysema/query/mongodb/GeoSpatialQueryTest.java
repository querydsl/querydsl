package com.mysema.query.mongodb;

import org.junit.Before;
import org.junit.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
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
        GeoEntity entity = new GeoEntity();
        entity.setLocation(new Double[]{50.0, 50.0});
        ds.save(geoEntity);
    }
    
    @Test
    public void Near(){
        query().where(geoEntity.location.near(50.0, 55.0)).list();
    }
    
    private MongodbQuery<GeoEntity> query() {
        return new MorphiaQuery<GeoEntity>(morphia, ds, geoEntity);
    }
    
}
