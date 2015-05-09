/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import static org.junit.Assert.*;

import java.io.*;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.testutil.Serialization;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
public class SerializationBase implements JPATest {

    private QCat cat = QCat.cat;

    private EntityManager entityManager;

    @Test
    public void test() throws IOException, ClassNotFoundException{
        // create query
        JPAQuery<?> query = query();
        query.from(cat).where(cat.name.eq("Kate")).select(cat).fetch();

        QueryMetadata metadata = query.getMetadata();
        assertFalse(metadata.getJoins().isEmpty());
        assertTrue(metadata.getWhere() != null);
        assertTrue(metadata.getProjection() != null);
        QueryMetadata metadata2 = Serialization.serialize(metadata);

        // validate it
        assertEquals(metadata.getJoins(), metadata2.getJoins());
        assertEquals(metadata.getWhere(), metadata2.getWhere());
        assertEquals(metadata.getProjection(), metadata2.getProjection());

        // create new query
        JPAQuery<?> query2 = new JPAQuery<Void>(entityManager, metadata2);
        assertEquals("select cat\nfrom Cat cat\nwhere cat.name = ?1", query2.toString());
        query2.select(cat).fetch();
    }

    @Test
    public void Any_Serialized() throws Exception {
        Predicate where = cat.kittens.any().name.eq("Ruth234");
        Predicate where2 = Serialization.serialize(where);

        assertEquals(0, query().from(cat).where(where).fetchCount());
        assertEquals(0, query().from(cat).where(where2).fetchCount());
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
            assertEquals(0, query().from(cat).where(where).fetchCount());
        } else {
            // deserialize predicate on second run
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            Predicate where2 = (Predicate) in.readObject();
            in.close();
            assertEquals(0, query().from(cat).where(where2).fetchCount());
        }
    }

    private JPAQuery<?> query() {
        return new JPAQuery<Void>(entityManager);
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
