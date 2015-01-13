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
package com.querydsl.jdo.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.jdo.models.fitness.QGym;
import com.querydsl.jdo.models.fitness.Wardrobe;

public class ContainerTest extends AbstractTest{

    private QGym gym = QGym.gym1;

    private Wardrobe wrd = new Wardrobe(), wrd1 = new Wardrobe(), wrd2 = new Wardrobe();

    @Before
    public void setUp() {
        wrd.setModel("model");
    }

    @Test
    public void NotContainsValuesInMapFields() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsValue(wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.fitness.Gym " +
          "WHERE !this.wardrobes.containsValue(a1) " +
          "PARAMETERS com.querydsl.jdo.models.fitness.Wardrobe a1",

          serialize(query().from(gym)
                  .where(gym.wardrobes.containsValue(wrd).not()).list(gym)));

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsValue(wrd) && !this.wardrobes.containsValue(wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");

        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.fitness.Gym " +
          "WHERE !this.wardrobes.containsValue(a1) && !this.wardrobes.containsValue(a2) " +
          "PARAMETERS com.querydsl.jdo.models.fitness.Wardrobe a1, com.querydsl.jdo.models.fitness.Wardrobe a2",

          serialize(query().from(gym)
                  .where(gym.wardrobes.containsValue(wrd).not(), gym.wardrobes.containsValue(wrd2).not())
                  .list(gym)));
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsValue(wrd) && !this.wardrobes.containsValue(wrd2) && this.wardrobes.containsValue(wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");

        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.fitness.Gym " +
          "WHERE !this.wardrobes.containsValue(a1) && !this.wardrobes.containsValue(a2) && this.wardrobes.containsValue(a3) " +
          "PARAMETERS com.querydsl.jdo.models.fitness.Wardrobe a1, com.querydsl.jdo.models.fitness.Wardrobe a2, com.querydsl.jdo.models.fitness.Wardrobe a3",

        serialize(query().from(gym)
                .where(
                    gym.wardrobes.containsValue(wrd).not(),
                    gym.wardrobes.containsValue(wrd1).not(),
                    gym.wardrobes.containsValue(wrd2))
                .list(gym)));
    }

    @Test 
    public void NotContainsKeysInMapFields() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes2.containsKey(wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.fitness.Gym " +
          "WHERE !this.wardrobes2.containsKey(a1) " +
          "PARAMETERS com.querydsl.jdo.models.fitness.Wardrobe a1",

          serialize(query().from(gym)
                   .where(gym.wardrobes2.containsKey(wrd).not()).list(gym)));
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes2.containsKey(wrd) && !this.wardrobes2.containsKey(wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.fitness.Gym " +
          "WHERE !this.wardrobes2.containsKey(a1) && !this.wardrobes2.containsKey(a2) " +
          "PARAMETERS com.querydsl.jdo.models.fitness.Wardrobe a1, com.querydsl.jdo.models.fitness.Wardrobe a2",

          serialize(query().from(gym)
                   .where(
                       gym.wardrobes2.containsKey(wrd).not(),
                       gym.wardrobes2.containsKey(wrd2).not())
                   .list(gym)));
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes2.containsKey(wrd) && !this.wardrobes2.containsKey(wrd2) && this.wardrobes2.containsKey(wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.fitness.Gym " +
          "WHERE !this.wardrobes2.containsKey(a1) && !this.wardrobes2.containsKey(a2) && this.wardrobes2.containsKey(a3) " +
          "PARAMETERS com.querydsl.jdo.models.fitness.Wardrobe a1, com.querydsl.jdo.models.fitness.Wardrobe a2, com.querydsl.jdo.models.fitness.Wardrobe a3",

        serialize(query().from(gym)
                 .where(
                     gym.wardrobes2.containsKey(wrd).not(),
                     gym.wardrobes2.containsKey(wrd2).not(),
                     gym.wardrobes2.containsKey(wrd1))
                 .list(gym)));
    }

    @Test 
    public void NotContainsEntryInMapFields() {
        // NOTE : containsEntry is not supported in Querydsl

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsEntry(wrd.model,wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsEntry(wrd.model,wrd) && !this.wardrobes.containsEntry(wrd2.model,wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsEntry(wrd.model,wrd) && !this.wardrobes.containsEntry(wrd2.model,wrd2) && this.wardrobes.containsEntry(wrd1.model,wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");

    }

    @Test 
    public void GetInMapFields() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE this.wardrobes.get(wrd.model) == wrd "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.fitness.Gym " +
          "WHERE this.wardrobes.get(a1) == a2 " +
          "PARAMETERS java.lang.String a1, com.querydsl.jdo.models.fitness.Wardrobe a2",

          serialize(query().from(gym)
                   .where(gym.wardrobes.get(wrd.getModel()).eq(wrd)).list(gym)));
    }

    @Test 
    public void GetInOrderingInMapFields() {
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//        .setOrdering("this.wardrobes.get(wrd.model).model ascending");
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.fitness.Gym " +
          "PARAMETERS java.lang.String a1 " +
          "ORDER BY this.wardrobes.get(a1).model ASC",

          serialize(query().from(gym)
                   .orderBy(gym.wardrobes.get(wrd.getModel()).model.asc()).list(gym)));
    }

}
