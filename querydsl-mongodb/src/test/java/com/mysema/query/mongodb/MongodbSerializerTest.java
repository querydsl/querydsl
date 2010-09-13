package com.mysema.query.mongodb;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mysema.query.types.Expr;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathBuilder;

public class MongodbSerializerTest {
    
    private PathBuilder<Object> entityPath;
    private PString title;
    private PNumber<Integer> year;
    private PNumber<Double> gross;

    private PNumber<Long> longField;
    private PNumber<Short> shortField;
    private PNumber<Byte> byteField;
    private PNumber<Float> floatField;
    
    private MongodbSerializer serializer;
    
    @Before
    public void before() {
        serializer = new MongodbSerializer();
        entityPath = new PathBuilder<Object>(Object.class, "obj");
        title = entityPath.getString("title");
        year = entityPath.getNumber("year", Integer.class);
    }
    
    @Test
    public void testEquals() {
        assertQuery(title.eq("A"), dbo("title","A"));
        assertQuery(year.eq(1), dbo("year",1));
    }
    
    private void assertQuery(Expr<?> e, BasicDBObject expected) {
        BasicDBObject result = (BasicDBObject) serializer.handle(e);
        assertEquals(expected, result);
    }

    private static BasicDBObject dbo(String key, Object value) {
        return new BasicDBObject(key, value);
    }
    
    
    

}
