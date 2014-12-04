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
package com.mysema.query.jpa;

import static org.junit.Assert.*;

import java.io.*;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.JPATest;
import com.mysema.query.QueryMetadata;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
public class SerializationBase implements JPATest {
    
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
        assertEquals("select cat\nfrom Cat cat\nwhere cat.name = ?1", query2.toString());
        query2.list(cat);        
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
