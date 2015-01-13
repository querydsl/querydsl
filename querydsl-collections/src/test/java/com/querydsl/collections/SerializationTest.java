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
package com.querydsl.collections;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.QTuple;

public class SerializationTest extends AbstractQueryTest{

    // TODO : order

    // TODO : subqueries

    private QTuple tuple = new QTuple(cat, otherCat);

    @Test
    public void OneSource_list() {
        query().from(cat, cats).list(cat);
    }

    public List<Cat> oneSource_list(List<Cat> cat_) {
        return cat_;
    }

    @Test
    public void TwoSources_list() {
        query().from(cat,cats).from(otherCat, cats).list(cat);
    }

    public List<Cat> twoSources_list(List<Cat> cat_, List<Cat> otherCat_) {
        return cat_;
    }

    @Test
    public void OneSource_filteredList() {
        query().from(cat, cats).where(cat.name.eq("Kitty")).list(cat);
    }

    public List<Cat> oneSource_filteredList(List<Cat> cat_) {
        List<Cat> rv = new ArrayList<Cat>();
        for (Cat cat : cats) {                   // from
            if (cat.getName().equals("Kitty")) { // where
                rv.add(cat);                    // list
            }
        }
        return rv;
    }

    @Test
    public void OneSource_projectedList() {
        query().from(cat, cats).list(cat.name);
    }

    public List<String> oneSource_projectedList(List<Cat> cat_) {
        List<String> rv = new ArrayList<String>();
        for (Cat cat : cat_) {                   // from
            rv.add(cat.getName());              // list
        }
        return rv;
    }

    @Test
    public void OneSource_filtered_projectedList() {
        query().from(cat, cats).where(cat.name.eq("Kitty")).list(cat.name);
    }

    public List<String> oneSource_filtered_projectedList(List<Cat> cat_) {
        List<String> rv = new ArrayList<String>();
        for (Cat cat : cat_) {                   // from
            if (cat.getName().equals("Kitty")) { // where
                rv.add(cat.getName());          // list
            }
        }
        return rv;
    }

    @Test
    public void OneSource_filtered_projectedUnique() {
        query().from(cat, cats).where(cat.name.eq("Kitty")).uniqueResult(cat.name);
    }

    public String oneSource_filtered_projectedUnique(List<Cat> cat_) {
        for (Cat cat : cat_) {                   // from
            if (cat.getName().equals("Kitty")) { // where
                return cat.getName();           // unique
            }
        }
        throw new IllegalArgumentException();
    }

    @Test
    @Ignore
    public void Join_list() {
        query().from(cat, cats).innerJoin(cat.kittens, kitten).where(kitten.name.eq("Kitty")).list(cat);
    }

    public List<Cat> join_list(List<Cat> cat_) {
        List<Cat> rv = new ArrayList<Cat>();
        for (Cat cat : cat_) {                          // from
            for (Cat kitten : cat.getKittens()) {       // inner join
                if (kitten.getName().equals("Kitty")) { // where
                    rv.add(cat);                       // list
                }
            }
        }
        return rv;
    }

    public List<Object[]> pairs(List<Cat> cat_, List<Cat> otherCat_) {
        query().from(cat, cats).from(otherCat, cats).where(cat.name.eq(otherCat.name)).list(cat, otherCat);

        List<Object[]> rv = new ArrayList<Object[]>();
        for (Cat cat : cat_) {                                  // from
            for (Cat otherCat : otherCat_) {                    // from
                if (cat.getName().equals(otherCat.getName())) { // where
                    rv.add(new Object[]{cat,otherCat});        // list
                }
            }
        }
        return rv;
    }

    public List<Tuple> pairsAsTuple(List<Cat> cat_, List<Cat> otherCat_) {
        query().from(cat, cats).from(otherCat, cats).where(cat.name.eq(otherCat.name)).list(new QTuple(cat, otherCat));

        List<Tuple> rv = new ArrayList<Tuple>();
        for (Cat cat : cat_) {                                  // from
            for (Cat otherCat : otherCat_) {                    // from
                if (cat.getName().equals(otherCat.getName())) { // where
                    rv.add(tuple.newInstance(cat, otherCat));  // list
                }
            }
        }
        return rv;
    }

}
