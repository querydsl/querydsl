package com.querydsl.mongodb;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.querydsl.core.testutil.MongoDB;
import com.querydsl.core.types.Predicate;
import com.querydsl.mongodb.domain.*;
import com.querydsl.mongodb.morphia.MorphiaQuery;

@Category(MongoDB.class)
public class PolymorphicCollectionTest {

    private final Morphia morphia;
    private final Datastore ds;
    private final Fish f1 = new Fish("f1");
    private final Fish f2 = new Fish("f2");
    private final Chips c1 = new Chips("c1");

    public PolymorphicCollectionTest() throws UnknownHostException, MongoException {
        final Mongo mongo = new Mongo();
        morphia = new Morphia().map(Food.class);
        ds = morphia.createDatastore(mongo, "testdb");
    }

    @Before
    public void before() throws UnknownHostException, MongoException {
        ds.delete(ds.createQuery(Food.class));
        ds.save(f1, f2, c1);
    }

    @Test
    public void basicCount() {
        assertEquals(where().fetchCount(), 3);
    }

    @Test
    public void countFishFromName() {
        assertEquals(where(QFood.food.name.eq("f1")).fetchCount(), 1);
    }

    @Test
    public void countFishFromNameAndBreed() {
        assertEquals(where(QFood.food.name.eq("f1")
            .and(QFish.fish.breed.eq("unknown"))).fetchCount(), 1);
    }

    @Test
    public void countFishFromNameAndBreedWithCast() {
        assertEquals(where(QFood.food.name.eq("f1")
                .and(QFood.food.as(QFish.class).breed.eq("unknown"))).fetchCount(), 1);
    }

    @Test
    public void countFishes() {
        assertEquals(where(isFish()).fetchCount(), 2);
    }

    private Predicate isFish() {
        return QFood.food.name.startsWith("f");
    }

    private MorphiaQuery<Food> query() {
        return new MorphiaQuery<Food>(morphia, ds, QFood.food);
    }

    private MorphiaQuery<Food> where(final Predicate... e) {
        return query().where(e);
    }
}
