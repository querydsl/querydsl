package com.mysema.query.mongodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mysema.query.mongodb.domain.Chips;
import com.mysema.query.mongodb.domain.Fish;
import com.mysema.query.mongodb.domain.Food;
import com.mysema.query.mongodb.domain.Item;
import com.mysema.query.mongodb.domain.QFish;
import com.mysema.query.mongodb.domain.QFood;
import com.mysema.query.mongodb.domain.QUser;
import com.mysema.query.mongodb.domain.User;
import com.mysema.query.mongodb.morphia.MorphiaQuery;
import com.mysema.query.types.Predicate;
import com.mysema.testutil.ExternalDB;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

@Category(ExternalDB.class)
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
        assertEquals(where().count(), 3);
    }

    @Test
    public void countFishFromName() {
        assertEquals(where(QFood.food.name.eq("f1")).count(), 1);
    }

    @Test
    public void countFishFromNameAndBreed() {
        assertEquals(where(QFood.food.name.eq("f1")
                .and(QFish.fish.breed.eq("unknown"))).count(), 1);
    }

    @Test
    public void countFishFromNameAndBreedWithCast() {
        assertEquals(where(QFood.food.name.eq("f1")
                .and(QFood.food.as(QFish.class).breed.eq("unknown"))).count(), 1);
    }

    @Test
    public void countFishes() {
        assertEquals(where(isFish()).count(), 2);
    }

    private Predicate isFish() {
        return QFood.food.name.startsWith("f");
    }

    private MongodbQuery<Food> query() {
        return new MorphiaQuery<Food>(morphia, ds, QFood.food);
    }

    private MongodbQuery<Food> where(final Predicate... e) {
        return query().where(e);
    }
}
