package com.mysema.query.mongodb;

import static junit.framework.Assert.assertEquals;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

public class MongoDBTest {
    
    
    private String dbname = "testdb";
    
   
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
        
    public class QTestUser extends EntityPathBase<TestUser> {

        private static final long serialVersionUID = -4872833626508344081L;

        public QTestUser(String var) {
            super(TestUser.class, PathMetadataFactory.forVariable(var));
        }
        
        public final PString id = createString("id");
        public final PString firstName = createString("firstName");
        public final PString lastName = createString("lastName");

    }
    
    
    @Test
    public void firstNameQuery() {
        

        Morphia morphia = new Morphia().map(TestUser.class);
        Datastore ds = morphia.createDatastore(dbname);
        
        //Clear out
        ds.delete(ds.createQuery(TestUser.class));
        
        
        TestUser u1 = new TestUser("Juuso", "Juhlava");
        ds.save(u1);
        
        u1 = new TestUser("Laila", "Laiha");
        ds.save(u1);

        QTestUser u = new QTestUser("u");
        
        MongodbQuery<TestUser> query = new MongodbQuery<TestUser>(ds, u);
        query.where(u.firstName.eq("Juuso"));
        
        
        assertEquals(1, query.count());
        
        
    }
    

}
