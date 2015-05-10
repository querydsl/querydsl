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
package com.querydsl.collections;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QTuple;

public class SerializationTest extends AbstractQueryTest{

    // TODO : order

    // TODO : subqueries

    private QTuple tuple = Projections.tuple(cat, otherCat);

    @Test
    public void OneSource_list() {
        query().from(cat, cats).select(cat).fetch();
    }

    public List<Cat> oneSource_list(List<Cat> cats) {
        return cats;
    }

    @Test
    public void TwoSources_list() {
        query().from(cat,cats).from(otherCat, cats).select(cat).fetch();
    }

    public List<Cat> twoSources_list(List<Cat> cats, List<Cat> otherCats) {
        return cats;
    }

    @Test
    public void OneSource_filteredList() {
        query().from(cat, cats).where(cat.name.eq("Kitty")).select(cat).fetch();
    }

    public List<Cat> oneSource_filteredList(List<Cat> cats) {
        List<Cat> rv = new ArrayList<Cat>();
        for (Cat cat : cats) {                   // from
            if (cat.getName().equals("Kitty")) { // where
                rv.add(cat);                     // list
            }
        }
        return rv;
    }

    @Test
    public void OneSource_projectedList() {
        query().from(cat, cats).select(cat.name).fetch();
    }

    public List<String> oneSource_projectedList(List<Cat> cats) {
        List<String> rv = new ArrayList<String>();
        for (Cat cat : cats) {                   // from
            rv.add(cat.getName());               // list
        }
        return rv;
    }

    @Test
    public void OneSource_filtered_projectedList() {
        query().from(cat, cats).where(cat.name.eq("Kitty")).select(cat.name).fetch();
    }

    public List<String> oneSource_filtered_projectedList(List<Cat> cats) {
        List<String> rv = new ArrayList<String>();
        for (Cat cat : cats) {                   // from
            if (cat.getName().equals("Kitty")) { // where
                rv.add(cat.getName());           // list
            }
        }
        return rv;
    }

    @Test
    public void OneSource_filtered_projectedUnique() {
        query().from(cat, cats).where(cat.name.eq("Kitty")).select(cat.name).fetchOne();
    }

    public String oneSource_filtered_projectedUnique(List<Cat> cats) {
        for (Cat cat : cats) {                   // from
            if (cat.getName().equals("Kitty")) { // where
                return cat.getName();            // unique
            }
        }
        throw new IllegalArgumentException();
    }

    @Test
    @Ignore
    public void Join_list() {
        query().from(cat, cats)
               .innerJoin(cat.kittens, kitten).where(kitten.name.eq("Kitty"))
               .select(cat).fetch();
    }

    public List<Cat> join_list(List<Cat> cats) {
        List<Cat> rv = new ArrayList<Cat>();
        for (Cat cat : cats) {                          // from
            for (Cat kitten : cat.getKittens()) {       // inner join
                if (kitten.getName().equals("Kitty")) { // where
                    rv.add(cat);                        // list
                }
            }
        }
        return rv;
    }

    public List<Object[]> pairs(List<Cat> cats, List<Cat> otherCats) {
        query().from(cat, cats)
               .from(otherCat, otherCats)
               .where(cat.name.eq(otherCat.name))
               .select(cat, otherCat).fetch();

        List<Object[]> rv = new ArrayList<Object[]>();
        for (Cat cat : cats) {                                  // from
            for (Cat otherCat : otherCats) {                    // from
                if (cat.getName().equals(otherCat.getName())) { // where
                    rv.add(new Object[]{cat,otherCat});         // list
                }
            }
        }
        return rv;
    }

    public List<Tuple> pairsAsTuple(List<Cat> cats, List<Cat> otherCats) {
        query().from(cat, cats).from(otherCat, cats)
               .where(cat.name.eq(otherCat.name))
               .select(Projections.tuple(cat, otherCat)).fetch();

        List<Tuple> rv = new ArrayList<Tuple>();
        for (Cat cat : cats) {                                  // from
            for (Cat otherCat : otherCats) {                    // from
                if (cat.getName().equals(otherCat.getName())) { // where
                    rv.add(tuple.newInstance(cat, otherCat));   // list
                }
            }
        }
        return rv;
    }

}
