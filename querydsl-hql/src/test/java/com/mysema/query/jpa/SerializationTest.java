package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.QueryMetadata;
import com.mysema.query.hql.domain.QCat;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
@JPAConfig("hsqldb")
public class SerializationTest {
    
    private QCat cat = QCat.cat;
    
    private EntityManager entityManager;
    
    @Test
    public void test() throws IOException, ClassNotFoundException{
        // create query
        JPAQuery query = new JPAQuery(entityManager);
        query.from(cat).where(cat.name.eq("Kate")).list(cat);
        
        // get metadata
        QueryMetadata metadata = query.getMetadata();
        assertFalse(metadata.getJoins().isEmpty());
        assertTrue(metadata.getWhere() != null);
        assertTrue(metadata.getProjection().isEmpty());
        
        // serialize metadata
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(metadata);
        out.close();
        
        // deserialize metadata
        ByteArrayInputStream bain = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bain);
        QueryMetadata  metadata2 = (QueryMetadata) in.readObject();
        in.close();
        
        // validate it
        assertEquals(metadata.getJoins(), metadata2.getJoins());
        assertEquals(metadata.getWhere(), metadata2.getWhere());
        assertEquals(metadata.getProjection(), metadata2.getProjection());
        
        // create new query
        JPAQuery query2 = new JPAQuery(entityManager, metadata2);
        assertEquals("from Cat cat\nwhere cat.name = :a1", query2.toString());
        query2.list(cat);        
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
