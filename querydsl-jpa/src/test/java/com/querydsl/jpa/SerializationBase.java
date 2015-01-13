/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.jpa;

import javax.persistence.EntityManager;
import java.io.*;

import com.querydsl.core.QueryMetadata;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.testutil.JPATestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(JPATestRunner.class)
public class SerializationBase implements JPATest {
    
    private QCat cat = QCat.cat;
    
    private EntityManager entityManager;
    
    @Test
    public void test() throws IOException, ClassNotFoundException{
        // create querydsl
        JPAQuery query = query();
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
        
        // create new querydsl
        JPAQuery query2 = new JPAQuery(entityManager, metadata2);
        assertEquals("select cat\nfrom Cat cat\nwhere cat.name = ?1", query2.toString());
        query2.list(cat);        
    }

    @Test
    public void Any_Serialized() throws Exception {
        Predicate where = cat.kittens.any().name.eq("Ruth234");

        // serialize predicate
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(where);
        out.close();

        // deserialize predicate
        ByteArrayInputStream bain = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bain);
        Predicate where2 = (Predicate) in.readObject();
        in.close();

        assertEquals(0, query().from(cat).where(where).count());
        assertEquals(0, query().from(cat).where(where2).count());
    }

    @Test
    public void Any_Serialized2() throws Exception {
        Predicate where = cat.kittens.any().name.eq("Ruth234");

        File file = new File("target", "predicate.ser");
        if (!file.exists()) {
            // serialize predicate on first run
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(where);
            out.close();
            assertEquals(0, query().from(cat).where(where).count());
        } else {
            // deserialize predicate on second run
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            Predicate where2 = (Predicate) in.readObject();
            in.close();
            assertEquals(0, query().from(cat).where(where2).count());
        }
    }

    private JPAQuery query() {
        return new JPAQuery(entityManager);
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
