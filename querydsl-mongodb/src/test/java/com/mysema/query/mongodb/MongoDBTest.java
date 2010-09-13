package com.mysema.query.mongodb;

import static junit.framework.Assert.assertEquals;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.bson.BSON;
import org.bson.BSONDecoder;
import org.bson.BSONEncoder;
import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.junit.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.query.Query;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mysema.commons.lang.Assert;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Param;
import com.mysema.query.types.SerializerBase;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.Templates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

public class MongoDBTest {
    
    
    private String dbname = "testdb";
    private String collname = "testcoll";
    
    //@Test
    public void insertData() throws UnknownHostException, MongoException {
        
        
        
        Mongo m = new Mongo();
//        Mongo m = new Mongo( "localhost" );
//        Mongo m = new Mongo( "localhost" , 27017 );

        DB db = m.getDB(dbname);
        
        Set<String> colls = db.getCollectionNames();

        for (String s : colls) {
            System.out.println(s);
        }

        DBCollection coll = null;
        
        if (db.collectionExists(collname))
            coll = db.createCollection(collname, null);
        else
            coll = db.getCollection(collname);
        
        BasicDBObject doc = new BasicDBObject();

        doc.put("name", "MongoDB");
        doc.put("type", "database");
        doc.put("count", 1);


        coll.insert(doc);
        
        
        System.out.println(coll.findOne());
        
        
    }
    
    @Entity
    public static class TestUser {
        
        @Id ObjectId id;
        
        String firstName;
        String lastName;
        
        public TestUser() {
        }

        public TestUser(String firstName, String lastName) {
            this.firstName = firstName; this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "TestUser [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
                    + "]";
        }
        
    }
    
    
    @Test
    public void morphiaTest() {
        
        Morphia m = new Morphia().map(TestUser.class);
        Datastore ds = m.createDatastore(dbname);
        
        TestUser u = new TestUser("Juuso", "Juhlava");
        ds.save(u);
        
        u = new TestUser("Laila", "Laiha");
        ds.save(u);

        for( TestUser user : ds.find(TestUser.class)) {
            
            System.out.println(user);
            
        }
    
    
    }
    
    public class QTestUser extends EntityPathBase<TestUser> {

        private static final long serialVersionUID = -4872833626508344081L;

        public QTestUser(String var) {
            super(TestUser.class, PathMetadataFactory.forVariable(var));
        }
        
        public final PString id = createString("id");
        public final PString firstName = createString("firstName");
        public final PString lastName = createString("lastName");

    }
    
    
    public static class MongodbQuery<K> implements SimpleQuery<MongodbQuery<K>>, SimpleProjectable<K> {
        
        private final QueryMixin<MongodbQuery<K>> queryMixin;
        private final EntityPath<K> ePath;
        private final Datastore ds;
        private final DBCollection coll;
        private final MongodbSerializer serializer = new MongodbSerializer(new MongodbTemplates());

        public MongodbQuery(Datastore datastore, DBCollection collection, EntityPath<K> entityPath) {
            queryMixin = new QueryMixin<MongodbQuery<K>>(this);
            //ds = datastore;
            coll = collection;
            ePath = entityPath;
            ds = datastore;
        }
        
        @Override
        public MongodbQuery<K> where(EBoolean... e) {
           return queryMixin.where(e);
        }

        @Override
        public MongodbQuery<K> limit(long limit) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public MongodbQuery<K> offset(long offset) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public MongodbQuery<K> restrict(QueryModifiers modifiers) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public MongodbQuery<K> orderBy(OrderSpecifier<?>... o) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public <T> MongodbQuery<K> set(Param<T> param, T value) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<K> list() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<K> listDistinct() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public K uniqueResult() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SearchResults<K> listResults() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SearchResults<K> listDistinctResults() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long count() {
            return createMorphiaQuery().countAll();
            //return coll.count(createQuery());
            
        }

        @Override
        public long countDistinct() {
            // TODO Auto-generated method stub
            return 0;
        }
        

        private Query<? extends K> createMorphiaQuery() {
            QueryMetadata metadata = queryMixin.getMetadata();
            Assert.notNull(metadata.getWhere(), "where needs to be set");
          
            
            return ds.createQuery(ePath.getType()).filter("firstName", "Juuso"); //where(" firstName : \"Juuso\" ");
        }
        
        private DBObject createQuery(){
            QueryMetadata metadata = queryMixin.getMetadata();
            Assert.notNull(metadata.getWhere(), "where needs to be set");
            
            //Query<? extends K> q = ds.createQuery(ePath.getType());
                        
            serializer.handle(metadata.getWhere());
            
            
            //String q = "{" + serializer.toString() +"}";
            
            DBObject query = new BasicDBObject().append("firstName", "Juuso");
            
              
            return query;
            
//            org.apache.lucene.search.Query query = serializer.toQuery(metadata.getWhere(), metadata);
//
//            FullTextQuery fullTextQuery = session.createFullTextQuery(query, path.getType());
//
//            // order
//            if (!metadata.getOrderBy().isEmpty() && !forCount){
//                fullTextQuery.setSort(serializer.toSort(metadata.getOrderBy()));
//            }
//
//            // paging
//            QueryModifiers modifiers = metadata.getModifiers();
//            if (modifiers != null && modifiers.isRestricting() && !forCount){
//                if (modifiers.getLimit() != null){
//                    fullTextQuery.setMaxResults(modifiers.getLimit().intValue());
//                }
//                if (modifiers.getOffset() != null){
//                    fullTextQuery.setFirstResult(modifiers.getOffset().intValue());
//                }
//            }
//            return fullTextQuery;
        }
        
    }
    
    
    
    public static class MongodbSerializer extends SerializerBase<MongodbSerializer> {

        public MongodbSerializer(Templates templates) {
            super(templates);
        }

        @Override
        public Void visit(SubQueryExpression<?> expr, Void context) {
            throw new NotImplementedException();
        }
                
    }
    
    public static class MongodbTemplates extends Templates {
        
        public MongodbTemplates() {
            add(Ops.EQ_PRIMITIVE, "{0} : {1}");
            add(Ops.EQ_OBJECT, "{0} : {1}");
        }
        
    }
    
    
    
    //@Test
    public void firstQueryDslTryout() throws UnknownHostException, MongoException {
        

        Morphia morphia = new Morphia().map(TestUser.class);
        Datastore ds = morphia.createDatastore(dbname);
        
        //Clear out
        ds.delete(ds.createQuery(TestUser.class));
        
        
        Mongo m = new Mongo();
        DB db = m.getDB(dbname);
        DBCollection coll = db.getCollection(collname);
        //coll.remove(new BasicDBObject());
        
        
        TestUser u1 = new TestUser("Juuso", "Juhlava");
        //coll.insert(morphia.toDBObject(u1));
        ds.save(u1);
        
        u1 = new TestUser("Laila", "Laiha");
        //coll.insert(morphia.toDBObject(u1));
        ds.save(u1);

        QTestUser u = new QTestUser("u");
        
        MongodbQuery<TestUser> query = new MongodbQuery<TestUser>(ds, coll, u);
        query.where(u.firstName.eq("Juuso"));
        
        
        assertEquals(1, query.count());
        
        
    }
    

}
